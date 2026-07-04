# Sections - Hiérarchie documentaire

La rubrique **Sections** permet de gérer une arborescence de sections métier dans l'interface web, puis d'associer un emplacement documentaire à chaque section racine pour générer automatiquement des dossiers et des fichiers Markdown sous `volumes/docs`.

## Objectif et périmètre

La feature couvre deux usages complémentaires :

- organiser un référentiel hiérarchique de sections et de sous-sections
- définir, pour chaque section racine, un chemin relatif de documentation
- générer automatiquement une arborescence de dossiers et de fichiers `index.md`
- mettre à jour cette arborescence quand une section est renommée, déplacée, réordonnée ou reconfigurée

Cette documentation concerne l'usage web actuellement disponible dans `data-web`.

Hors périmètre actuel :

- édition directe du contenu Markdown depuis l'interface
- aperçu temps réel des fichiers générés dans le navigateur
- impression spécifique de la hiérarchie des sections

## Interface Web

### Pages disponibles

| Page | Chemin | Description |
|---|---|---|
| Gestion des sections | `/section/list` | Navigation hiérarchique, détail, création, modification et suppression |
| Paramètres docs | `/section/settings/docs` | Association d'un chemin relatif à chaque section racine |
| Création directe | `/section/create` | Création d'une section hors panneau principal |
| Détail d'une section | `/section/:id` | Consultation d'une section précise |
| Édition d'une section | `/section/:id/edit` | Modification d'une section précise |

### Parcours utilisateur standard

```text
Liste des sections (/section/list)
  ↓
Sélection d'une section dans l'arborescence MUI X Tree View
  ↓
Consultation du détail
  ↓
Modification / ajout d'un enfant / suppression
```

### Parcours documentaire

```text
Paramètres docs (/section/settings/docs)
  ↓
Choix d'une section racine
  ↓
Définition d'un chemin relatif sous volumes/docs
  ↓
Enregistrement
  ↓
Génération ou resynchronisation des dossiers et index.md
```

## Parcours utilisateur et opérations courantes

### Naviguer dans l'arborescence

1. Ouvrez `/section/list`
2. Utilisez le panneau **Navigation** à gauche
3. Parcourez les niveaux grâce au composant **MUI X Tree View**
4. Sélectionnez une section pour afficher son détail dans le panneau principal

L'arborescence affiche :

- le nom de la section
- sa description courte
- son niveau dans la hiérarchie
- son ordre d'affichage

### Créer une section racine

1. Depuis `/section/list`, cliquez **Nouvelle section**
2. Renseignez :
   - **Nom**
   - **Description**
   - **Ordre d'affichage**
   - **Section parente** : laissez vide pour créer une racine
3. Enregistrez

### Créer une sous-section

1. Sélectionnez une section existante dans l'arborescence
2. Cliquez l'action **Ajouter un enfant** sur la ligne de la section sélectionnée
3. Remplissez le formulaire
4. Vérifiez que la **section parente** est préremplie
5. Enregistrez

### Modifier une section

1. Sélectionnez la section dans l'arborescence
2. Ouvrez l'onglet **Édition**
3. Modifiez le nom, la description, l'ordre ou la parenté
4. Cliquez **Enregistrer**

Effets attendus :

- la hiérarchie affichée est mise à jour
- la génération documentaire est resynchronisée si la section concernée fait partie d'une racine configurée

### Supprimer une section

1. Sélectionnez la section
2. Cliquez **Supprimer**
3. Confirmez la suppression

Effets attendus :

- la section et ses sous-sections sont supprimées
- l'arborescence documentaire correspondante est reconstruite ou supprimée selon la configuration restante

### Configurer les paramètres docs d'une section racine

1. Depuis `/section/list`, cliquez **Paramètres docs**
2. Dans `/section/settings/docs`, repérez la section racine à configurer
3. Saisissez un **chemin relatif** sous `volumes/docs`, par exemple `guides/produits`
4. Cliquez **Enregistrer**

Après enregistrement, le backend génère une structure du type :

```text
volumes/docs/
  guides/
    produits/
      1-Guides/
        index.md
        1.2-Installation/
          index.md
```

Le contenu généré suit ce format :

```markdown
# 1.2-Installation

Procedure d'installation
```

## Règles métier et validations

### Hiérarchie

- une section peut avoir zéro ou une section parente
- une section peut avoir plusieurs enfants
- une section ne peut pas être sa propre parente
- une section ne peut pas être déplacée sous une de ses propres sous-sections

### Ordre d'affichage

- l'ordre d'affichage est un entier positif ou nul
- les sections soeurs sont triées par ordre d'affichage puis par nom
- l'index hiérarchique utilisé pour les dossiers et titres Markdown est calculé à partir de cet ordre

### Paramètres docs

- seules les sections racines peuvent être configurées
- un seul chemin est autorisé par section racine
- le chemin doit être **relatif** à `volumes/docs`
- les chemins absolus sont refusés
- les segments `..` sont refusés

### Synchronisation documentaire

- la création d'une section met à jour l'arborescence si une racine configurée est concernée
- la modification d'un nom, d'un ordre, d'une parenté ou d'une description déclenche une resynchronisation
- la suppression reconstruit ou supprime l'arborescence documentaire concernée
- la modification des paramètres docs remplace l'implantation précédente par la nouvelle

## Responsive mobile / desktop

### Desktop

- arborescence à gauche et panneau de détail/édition à droite
- usage confortable du Tree View et des actions contextuelles

### Mobile

- empilement vertical des panneaux
- conservation des mêmes informations essentielles
- accès aux paramètres docs inchangé via le bouton dédié

## Dépannage

### Le chemin est refusé

Vérifiez que :

- le chemin n'est pas vide
- le chemin ne commence pas par `/`
- le chemin ne contient pas `..`
- la section configurée est bien une racine

### Les fichiers Markdown ne sont pas générés

Vérifiez que :

- la section racine possède une configuration dans `/section/settings/docs`
- le backend a accès au dossier `volumes/docs`
- le service `data-server` est démarré avec le montage du volume documentaire

### L'arborescence générée ne reflète pas l'ordre attendu

Vérifiez l'**ordre d'affichage** de chaque section soeur. L'index hiérarchique et les noms de dossiers dépendent de cette valeur.

## Concepts clés

- **Section racine** : section sans parent, seule éligible au paramétrage documentaire
- **Sous-section** : section rattachée à une autre section
- **Ordre d'affichage** : entier utilisé pour trier les sections soeurs et calculer l'index hiérarchique
- **Index hiérarchique** : séquence du type `1`, `1.2`, `1.2.3`
- **Chemin relatif** : sous-chemin stocké dans l'application puis résolu sous `volumes/docs`