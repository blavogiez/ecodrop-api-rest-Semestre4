const BASE_URL = '';

let state = {
  token: sessionStorage.getItem('token') || null,
  user: JSON.parse(sessionStorage.getItem('user') || 'null')
};

document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('login-form').addEventListener('submit', handleLogin);
  document.getElementById('deposit-form').addEventListener('submit', handleDeposit);
  document.getElementById('btn-logout').addEventListener('click', handleLogout);
  document.getElementById('btn-refresh').addEventListener('click', loadAll);

  const btnOverloaded = document.getElementById('btn-overloaded');
  if (btnOverloaded) {
    btnOverloaded.addEventListener('click', loadOverloadedPoints);
  }

  if (state.token && state.user) {
    applyLoggedInState();
  }
});

function authHeaders() {
  return state.token ? { 'Authorization': 'Bearer ' + state.token } : {};
}

async function handleLogin(e) {
  e.preventDefault();
  const loginInput = document.getElementById('login').value.trim();
  const passwordInput = document.getElementById('password').value;
  const msgEl = document.getElementById('login-msg');
  msgEl.textContent = '';

  try {
    const creds = btoa(loginInput + ':' + passwordInput);
    const res = await fetch(`${BASE_URL}/auth/token`, {
      headers: { 'Authorization': 'Basic ' + creds }
    });

    if (!res.ok) {
      msgEl.className = 'msg error';
      msgEl.textContent = 'Identifiants invalides (' + res.status + ')';
      return;
    }

    const token = (await res.text()).trim();

    const usersRes = await fetch(`${BASE_URL}/users`);
    const users = await usersRes.json();
    const user = users.find(u => u.login === loginInput) || { login: loginInput, role: 'USER', id: 0 };

    state.token = token;
    state.user = user;
    sessionStorage.setItem('token', token);
    sessionStorage.setItem('user', JSON.stringify(user));

    applyLoggedInState();
  } catch (err) {
    msgEl.className = 'msg error';
    msgEl.textContent = 'Erreur réseau : ' + err.message;
  }
}

function handleLogout() {
  state.token = null;
  state.user = null;
  sessionStorage.removeItem('token');
  sessionStorage.removeItem('user');

  document.getElementById('auth-section').classList.remove('hidden');
  document.getElementById('app-section').classList.add('hidden');
  document.getElementById('login-form').reset();
  document.getElementById('login-msg').textContent = '';
}

function applyLoggedInState() {
  document.getElementById('auth-section').classList.add('hidden');
  document.getElementById('app-section').classList.remove('hidden');

  document.getElementById('current-user').textContent =
    `${state.user.login} (${state.user.role}, id: ${state.user.id})`;

  const adminElements = document.querySelectorAll('.admin-only');
  adminElements.forEach(el => {
    if (state.user.role === 'ADMIN') {
      el.classList.remove('hidden');
    } else {
      el.classList.add('hidden');
    }
  });

  loadAll();
}

async function loadAll() {
  await Promise.all([
    loadPoints(),
    loadWasteTypes(),
    loadDeposits(),
    loadLeaderboard()
  ]);
}

async function loadPoints() {
  const tbody = document.querySelector('#points-table tbody');
  const select = document.getElementById('deposit-point');
  tbody.innerHTML = '<tr><td colspan="5">Chargement...</td></tr>';
  select.innerHTML = '<option value="">-- Choisir un point --</option>';

  try {
    const res = await fetch(`${BASE_URL}/points`);
    const points = await res.json();
    tbody.innerHTML = '';

    for (const point of points) {
      let tauxText = '-';
      try {
        const stRes = await fetch(`${BASE_URL}/points/${point.id}/status`);
        if (stRes.ok) {
          const st = await stRes.json();
          tauxText = st.taux.toFixed(1) + ' %' + (st.full ? ' (PLEIN)' : '');
        }
      } catch (_) {}

      const tr = document.createElement('tr');
      let actionHtml = '-';
      if (state.user && state.user.role === 'ADMIN') {
        actionHtml = `<button onclick="clearPoint(${point.id})">Vider</button>`;
      }

      tr.innerHTML = `
        <td>${point.id}</td>
        <td>${point.adresse}</td>
        <td>${point.capaciteMax} kg</td>
        <td>${tauxText}</td>
        <td>${actionHtml}</td>
      `;
      tbody.appendChild(tr);

      const opt = document.createElement('option');
      opt.value = point.id;
      opt.textContent = `#${point.id} - ${point.adresse}`;
      select.appendChild(opt);
    }
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="5" class="error">Erreur : ${err.message}</td></tr>`;
  }
}

async function loadOverloadedPoints() {
  const container = document.getElementById('overloaded-result');
  container.textContent = 'Chargement...';

  try {
    const res = await fetch(`${BASE_URL}/points/overloaded`, {
      headers: authHeaders()
    });
    if (!res.ok) {
      container.textContent = 'Erreur ' + res.status;
      return;
    }
    const points = await res.json();
    if (points.length === 0) {
      container.textContent = 'Aucun point surchargé (> 80%).';
      return;
    }
    container.innerHTML = points.map(p => `#${p.id} ${p.adresse} (max: ${p.capaciteMax}kg)`).join('<br>');
  } catch (err) {
    container.textContent = 'Erreur : ' + err.message;
  }
}

async function clearPoint(pointId) {
  if (!confirm(`Confirmer le vidage du point #${pointId} ?`)) return;

  try {
    const res = await fetch(`${BASE_URL}/points/${pointId}/clear`, {
      method: 'DELETE',
      headers: authHeaders()
    });
    if (res.ok) {
      loadPoints();
      loadDeposits();
    } else {
      alert('Erreur lors du vidage : ' + res.status);
    }
  } catch (err) {
    alert('Erreur réseau : ' + err.message);
  }
}

async function loadWasteTypes() {
  const select = document.getElementById('deposit-waste');
  select.innerHTML = '<option value="">-- Choisir un type --</option>';

  try {
    const res = await fetch(`${BASE_URL}/waste-types`);
    const types = await res.json();
    for (const t of types) {
      const opt = document.createElement('option');
      opt.value = t.id;
      opt.textContent = `${t.nom} (${t.pointsPerKilo} pts/kg)`;
      select.appendChild(opt);
    }
  } catch (_) {}
}

async function handleDeposit(e) {
  e.preventDefault();
  const msgEl = document.getElementById('deposit-msg');
  msgEl.textContent = '';

  const pointId = parseInt(document.getElementById('deposit-point').value, 10);
  const wasteTypeId = parseInt(document.getElementById('deposit-waste').value, 10);
  const poids = parseFloat(document.getElementById('deposit-weight').value);

  if (!pointId || !wasteTypeId || isNaN(poids) || poids <= 0) {
    msgEl.className = 'msg error';
    msgEl.textContent = 'Veuillez remplir tous les champs correctement.';
    return;
  }

  const payload = {
    userId: state.user.id,
    pointId: pointId,
    wasteTypeId: wasteTypeId,
    poids: poids
  };

  try {
    const res = await fetch(`${BASE_URL}/deposits`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...authHeaders()
      },
      body: JSON.stringify(payload)
    });

    if (res.status === 201) {
      msgEl.className = 'msg success';
      msgEl.textContent = 'Dépôt enregistré avec succès.';
      document.getElementById('deposit-form').reset();
      loadPoints();
      loadDeposits();
      loadLeaderboard();
    } else if (res.status === 403) {
      msgEl.className = 'msg error';
      msgEl.textContent = 'Erreur 403 : Point de collecte plein.';
    } else {
      msgEl.className = 'msg error';
      msgEl.textContent = 'Erreur lors du dépôt : ' + res.status;
    }
  } catch (err) {
    msgEl.className = 'msg error';
    msgEl.textContent = 'Erreur réseau : ' + err.message;
  }
}

async function loadDeposits() {
  const tbody = document.querySelector('#deposits-table tbody');
  tbody.innerHTML = '<tr><td colspan="6">Chargement...</td></tr>';

  try {
    const res = await fetch(`${BASE_URL}/deposits`);
    const deposits = await res.json();
    tbody.innerHTML = '';

    if (deposits.length === 0) {
      tbody.innerHTML = '<tr><td colspan="6">Aucun dépôt enregistré.</td></tr>';
      return;
    }

    deposits.slice(0, 15).forEach(d => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${d.id}</td>
        <td>${d.datedepot || '-'}</td>
        <td>${d.adressePoint || ('Point #' + d.pointId)}</td>
        <td>${d.nomDechet || ('Type #' + d.wasteTypeId)}</td>
        <td>${d.poids} kg</td>
        <td>${d.collecte ? 'Oui' : 'Non'}</td>
      `;
      tbody.appendChild(tr);
    });
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="6" class="error">Erreur : ${err.message}</td></tr>`;
  }
}

async function loadLeaderboard() {
  const ol = document.getElementById('leaderboard-list');
  ol.innerHTML = '<li>Chargement...</li>';

  try {
    const res = await fetch(`${BASE_URL}/users/leaderboard`);
    const leaders = await res.json();
    ol.innerHTML = '';

    leaders.forEach(u => {
      const li = document.createElement('li');
      li.textContent = `${u.login} - ${u.score} points`;
      ol.appendChild(li);
    });
  } catch (err) {
    ol.innerHTML = `<li class="error">Erreur : ${err.message}</li>`;
  }
}
