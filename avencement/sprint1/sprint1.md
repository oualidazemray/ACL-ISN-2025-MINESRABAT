# LO3BA – Suivi des Sprints (ACL-ISN 2025 Mines Rabat)

Projet développé en JavaFX selon la méthodologie Agile/Scrum.
Chaque sprint aboutit à une version jouable du jeu, accompagnée de la conception UML, d’un backlog et d’une distribution claire des tâches.

---

## Sprint 1 – Mise en place du projet et menu principal

### Objectifs

- Initialisation du projet Maven et configuration de JavaFX.
- Création du menu principal (`main-menu.fxml`).
- Navigation vers la scène du jeu (`game-screen.fxml`).
- Application du thème rétro (`retro.css`).

### Réalisé

- Arborescence du projet mise en place (`src/main/java` + `src/main/resources`).
- Implémentation de :

  - `Lo3baMain.java` (classe principale)
  - `MainMenuController.java` et `GameController.java`
  - Fichiers FXML : `main-menu.fxml` et `game-screen.fxml`

- Feuille de style `retro.css` intégrée.
- Lancement du jeu via `mvn javafx:run`.

### UML et conception

- Diagramme de classes : `Lo3baMain`, `MainMenuController`, `GameController`
- Relation : `MainMenuController` → `GameController`

### Répartition

| Membre   | Tâches                           |
| -------- | -------------------------------- |
| Membre 1 | Structure du projet et Lo3baMain |
| Membre 2 | FXML et MainMenuController       |
| Membre 3 | GameController et navigation     |
| Membre 4 | Feuille de style, UML et README  |

### Tag Git

`v1.0` – Première version jouable (menu principal et navigation).

### Préparation du sprint suivant

Mettre en place le personnage, ses mouvements et la zone de jeu dynamique.

---
