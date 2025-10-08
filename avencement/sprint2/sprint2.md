## Sprint 2 – Personnage et déplacement

### Objectifs

- Création du joueur (sprite dinosaure).
- Gestion du déplacement horizontal et du saut.
- Détection du sol et des collisions simples.
- Intégration de la zone de jeu dynamique (Pane).

---

### Réalisé

- Ajout du joueur dans `game-screen.fxml`.
- Gestion des touches de direction et saut.
- Mise à jour du `GameController` pour gérer le mouvement.
- Première ébauche de la boucle d’animation du jeu.

---

### UML et conception

- Nouvelles classes : `Player`, `GameLoop`.
- Mise à jour du diagramme UML : relation `GameController → Player`.

---

### Répartition

| Membre   | Tâches                                    |
| -------- | ----------------------------------------- |
| Membre 1 | Classe `Player` et gestion des mouvements |
| Membre 2 | `GameController` (boucle d’animation)     |
| Membre 3 | Collisions et tests                       |
| Membre 4 | UML et documentation                      |

---

### Tag Git

**v2.0** – Version avec personnage jouable.

---

### Préparation du sprint suivant

- Ajout des obstacles et des pièces.
- Mise en place du score et de la vie.
- Introduction d’un système de niveaux.
- Amélioration du visuel (HUD rétro).

---

### Rétrospective

**Ce qui a bien fonctionné :**

- L’équipe a réussi à intégrer un personnage jouable et un contrôle fluide.
- La communication entre les membres s’est améliorée.
- La structure du code (MVC + JavaFX) est restée claire.

**Ce qui peut être amélioré :**

- Mieux organiser la gestion des collisions et la gravité.
- Mieux planifier les tâches pour éviter le travail en doublon.
- Ajouter des tests unitaires basiques pour les futures entités.

**Actions pour le prochain sprint :**

- Documenter le fonctionnement du `GameLoop`.
- Introduire une classe `Entity` abstraite pour préparer les obstacles et objets.
- Mettre en place un système de score et un affichage dynamique (HUD).
