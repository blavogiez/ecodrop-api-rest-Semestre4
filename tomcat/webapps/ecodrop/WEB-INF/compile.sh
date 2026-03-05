#!/bin/bash

# Script de compilation Java
# Compile tous les fichiers .java dans src/ et place les .class dans classes/

# Couleurs pour les messages
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Répertoires
SRC_DIR="src"
CLASSES_DIR="classes"

echo "======================================"
echo "  Compilation des fichiers Java"
echo "======================================"

# Vérifier si le dossier src existe
if [ ! -d "$SRC_DIR" ]; then
    echo -e "${RED}Erreur: Le dossier '$SRC_DIR' n'existe pas!${NC}"
    exit 1
fi

# Créer le dossier classes s'il n'existe pas
if [ ! -d "$CLASSES_DIR" ]; then
    echo -e "${YELLOW}Création du dossier '$CLASSES_DIR'...${NC}"
    mkdir -p "$CLASSES_DIR"
fi

# Trouver tous les fichiers .java
JAVA_FILES=$(find "$SRC_DIR" -name "*.java")

# Vérifier s'il y a des fichiers Java
if [ -z "$JAVA_FILES" ]; then
    echo -e "${YELLOW}Aucun fichier .java trouvé dans '$SRC_DIR'${NC}"
    exit 0
fi

# Compter le nombre de fichiers
FILE_COUNT=$(echo "$JAVA_FILES" | wc -l)
echo -e "${YELLOW}Fichiers Java trouvés: $FILE_COUNT${NC}"
echo ""

# Compiler tous les fichiers Java
echo "Compilation en cours..."
javac -d "$CLASSES_DIR" -sourcepath "$SRC_DIR" $JAVA_FILES

# Vérifier le résultat de la compilation
if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✓ Compilation réussie!${NC}"
    echo -e "${GREEN}Les fichiers .class sont dans le dossier '$CLASSES_DIR'${NC}"
    exit 0
else
    echo ""
    echo -e "${RED}✗ Erreur lors de la compilation${NC}"
    exit 1
fi
