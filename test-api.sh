#!/bin/bash

BASE="http://localhost:8080/ecodrop"
PASS=0
FAIL=0

# ── Helpers ────────────────────────────────────────────────────────────

green() { echo -e "\033[32m$*\033[0m"; }
red()   { echo -e "\033[31m$*\033[0m"; }
blue()  { echo -e "\033[34m$*\033[0m"; }
bold()  { echo -e "\033[1m$*\033[0m"; }

check() {
    local label="$1"
    local expected="$2"
    local actual="$3"
    if [ "$actual" -eq "$expected" ] 2>/dev/null; then
        green "  ✓ $label (HTTP $actual)"
        PASS=$((PASS + 1))
    else
        red "  ✗ $label — attendu HTTP $expected, obtenu HTTP $actual"
        FAIL=$((FAIL + 1))
    fi
}

req() {
    # req METHOD URL [extra curl args...]
    local method="$1"; shift
    local url="$1"; shift
    curl -s -o /tmp/ecodrop_body -w "%{http_code}" -X "$method" "$url" "$@"
}

body() { cat /tmp/ecodrop_body; echo; }

section() { echo; bold "══ $* ══"; }

# ── 0. Tokens ──────────────────────────────────────────────────────────
section "AUTH — récupération des tokens"

STATUS=$(req GET "$BASE/auth/token" -u "paulpaulpaul:paulpaulpaul")
check "GET /auth/token (admin valide)" 200 "$STATUS"
TOKEN_ADMIN=$(body)

STATUS=$(req GET "$BASE/auth/token" -u "podmanpodman:podmanpodman")
check "GET /auth/token (user valide)" 200 "$STATUS"
TOKEN_USER=$(body)

STATUS=$(req GET "$BASE/auth/token" -u "inconnu:mauvaismdp")
check "GET /auth/token (identifiants invalides)" 401 "$STATUS"

if [ -z "$TOKEN_ADMIN" ] || [ -z "$TOKEN_USER" ]; then
    red "Impossible de récupérer les tokens — abandon."
    exit 1
fi

# ── 1. Users ───────────────────────────────────────────────────────────
section "USERS"

STATUS=$(req GET "$BASE/users")
check "GET /users" 200 "$STATUS"

STATUS=$(req GET "$BASE/users/leaderboard")
check "GET /users/leaderboard" 200 "$STATUS"

STATUS=$(req PUT "$BASE/users/4" \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{"id":4,"login":"temoin","password":"temointemoin","role":"USER"}')
check "PUT /users/4 (user valide)" 200 "$STATUS"

STATUS=$(req PUT "$BASE/users/9999" \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{"id":9999,"login":"fantome","password":"x","role":"USER"}')
check "PUT /users/9999 (inexistant)" 404 "$STATUS"

STATUS=$(req PUT "$BASE/users/4" \
  -H "Content-Type: application/json" \
  -d '{"id":4,"login":"temoin","password":"temointemoin","role":"USER"}')
check "PUT /users/4 (sans token)" 401 "$STATUS"

# ── 2. Waste Types ─────────────────────────────────────────────────────
section "WASTE TYPES"

STATUS=$(req GET "$BASE/waste-types")
check "GET /waste-types" 200 "$STATUS"

STATUS=$(req GET "$BASE/waste-types/2")
check "GET /waste-types/2" 200 "$STATUS"

STATUS=$(req GET "$BASE/waste-types/9999")
check "GET /waste-types/9999 (inexistant)" 404 "$STATUS"

STATUS=$(req POST "$BASE/waste-types" \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{"id":99,"nom":"Déchet test","pointsPerKilo":3.5}')
check "POST /waste-types (user)" 201 "$STATUS"

STATUS=$(req DELETE "$BASE/waste-types/99" \
  -H "Authorization: Bearer $TOKEN_USER")
check "DELETE /waste-types/99 (user — interdit)" 403 "$STATUS"

STATUS=$(req DELETE "$BASE/waste-types/99" \
  -H "Authorization: Bearer $TOKEN_ADMIN")
check "DELETE /waste-types/99 (admin)" 200 "$STATUS"

STATUS=$(req DELETE "$BASE/waste-types/9999" \
  -H "Authorization: Bearer $TOKEN_ADMIN")
check "DELETE /waste-types/9999 (inexistant)" 404 "$STATUS"

# ── 3. Collection Points ───────────────────────────────────────────────
section "COLLECTION POINTS"

STATUS=$(req GET "$BASE/points")
check "GET /points" 200 "$STATUS"

STATUS=$(req GET "$BASE/points/1")
check "GET /points/1" 200 "$STATUS"

STATUS=$(req GET "$BASE/points/999999")
check "GET /points/999999 (inexistant)" 404 "$STATUS"

STATUS=$(req GET "$BASE/points/1/status")
check "GET /points/1/status" 200 "$STATUS"

STATUS=$(req GET "$BASE/points/overloaded" \
  -H "Authorization: Bearer $TOKEN_ADMIN")
check "GET /points/overloaded (admin)" 200 "$STATUS"

STATUS=$(req GET "$BASE/points/overloaded" \
  -H "Authorization: Bearer $TOKEN_USER")
check "GET /points/overloaded (user — interdit)" 403 "$STATUS"

STATUS=$(req PATCH "$BASE/points/1" \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{"adresse":"1 Rue Modifiée"}')
check "PATCH /points/1 (user)" 200 "$STATUS"

# ── 4. Deposits ────────────────────────────────────────────────────────
section "DEPOSITS"

STATUS=$(req GET "$BASE/deposits")
check "GET /deposits" 200 "$STATUS"

STATUS=$(req GET "$BASE/deposits/2")
check "GET /deposits/2" 200 "$STATUS"

STATUS=$(req GET "$BASE/deposits/9999")
check "GET /deposits/9999 (inexistant)" 404 "$STATUS"

STATUS=$(req POST "$BASE/deposits" \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{"userId":2,"pointId":1,"wasteTypeId":1,"poids":5.0,"datedepot":"2026-03-28","collecte":"false"}')
check "POST /deposits (user)" 201 "$STATUS"
DEPOSIT_ID=$(body | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')

if [ -n "$DEPOSIT_ID" ]; then
    STATUS=$(req PATCH "$BASE/deposits/$DEPOSIT_ID" \
      -H "Authorization: Bearer $TOKEN_USER" \
      -H "Content-Type: application/json" \
      -d '{"poids":10.0}')
    check "PATCH /deposits/$DEPOSIT_ID (user)" 200 "$STATUS"
fi

# ── 5. Accepts ─────────────────────────────────────────────────────────
section "ACCEPTS"

STATUS=$(req GET "$BASE/accepts")
check "GET /accepts" 200 "$STATUS"

STATUS=$(req POST "$BASE/accepts" \
  -H "Authorization: Bearer $TOKEN_USER" \
  -H "Content-Type: application/json" \
  -d '{"pointsId":1,"wasteTypesId":2}')
# 201 si n'existait pas, 409 si déjà présent — les deux sont OK
if [ "$STATUS" -eq 201 ] || [ "$STATUS" -eq 409 ]; then
    green "  ✓ POST /accepts (HTTP $STATUS)"
    PASS=$((PASS + 1))
else
    red "  ✗ POST /accepts — attendu 201/409, obtenu HTTP $STATUS"
    FAIL=$((FAIL + 1))
fi

STATUS=$(req DELETE "$BASE/accepts/1/2" \
  -H "Authorization: Bearer $TOKEN_USER")
check "DELETE /accepts/1/2 (user — interdit)" 403 "$STATUS"

STATUS=$(req DELETE "$BASE/accepts/1/2" \
  -H "Authorization: Bearer $TOKEN_ADMIN")
# 200 si supprimé, 404 si n'existait pas — les deux sont OK
if [ "$STATUS" -eq 200 ] || [ "$STATUS" -eq 404 ]; then
    green "  ✓ DELETE /accepts/1/2 (admin — HTTP $STATUS)"
    PASS=$((PASS + 1))
else
    red "  ✗ DELETE /accepts/1/2 — attendu 200/404, obtenu HTTP $STATUS"
    FAIL=$((FAIL + 1))
fi

# ── Résumé ─────────────────────────────────────────────────────────────
echo
bold "══ RÉSUMÉ ══"
TOTAL=$((PASS + FAIL))
green "$PASS/$TOTAL tests passés"
[ $FAIL -gt 0 ] && red "$FAIL/$TOTAL tests échoués"
echo

[ $FAIL -eq 0 ] && exit 0 || exit 1
