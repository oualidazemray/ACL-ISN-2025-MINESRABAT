# Sprint 1 – Séance de TP

**Projet : LO3BA (ACL-ISN 2025 – Mines Rabat)**

---

## 1. Backlog Sprint 1

### Objectif global

Mettre en place la première version du jeu **LO3BA** avec un menu principal fonctionnel et la navigation vers la scène de jeu.

### User Stories

| ID  | En tant que... | Je veux...                                   | Afin de...                          | Priorité |
| --- | -------------- | -------------------------------------------- | ----------------------------------- | -------- |
| US1 | Joueur         | Voir un écran d’accueil avec le titre du jeu | Identifier facilement le jeu        | Haute    |
| US2 | Joueur         | Cliquer sur “START GAME” pour commencer      | Accéder à la scène du jeu           | Haute    |
| US3 | Joueur         | Avoir un bouton “EXIT”                       | Pouvoir quitter le jeu simplement   | Moyenne  |
| US4 | Développeur    | Appliquer un thème visuel rétro              | Donner une identité visuelle au jeu | Basse    |

---

## 2. Conception (Sprint 2 – Prévision UML)

### Classes prévues pour le Sprint 2

- **Player** : gère le personnage du joueur (position, mouvement, saut).
- **GameController** (évolué) : gère la logique du jeu et les entrées clavier.
- **GameLoop** : classe responsable de la mise à jour continue du jeu (animation).
- **Entity** (classe abstraite potentielle) : base pour `Player`, `Coin`, `Obstacle`.

### Relations UML (prévision)

```
Lo3baMain → MainMenuController → GameController → Player
                              ↘ GameLoop
```

---

## 3. Répartition des tâches (Sprint 1)

| Membre        | Rôle / Tâches principales                                                     |
| ------------- | ----------------------------------------------------------------------------- |
| **Yassine 1** | Création du fichier `Lo3baMain.java` et configuration du projet JavaFX        |
| **Yassine 2** | Création du `main-menu.fxml` et intégration du CSS rétro                      |
| **Oualid**    | Implémentation du `MainMenuController` et navigation vers le `GameController` |
| **Wadie**     | Rédaction du README, préparation du diagramme UML et organisation du dépôt    |

---

## 4. Livrables de la séance

- Backlog du sprint (présent dans ce fichier)
- Conception UML (prévisionnelle pour le sprint 2)
- Répartition des tâches (tableau ci-dessus)

---

**Prochaine étape (Sprint 2)** :
Ajout du personnage, gestion du mouvement et intégration du gameplay de base.
