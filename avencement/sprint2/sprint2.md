## Sprint 2 – Personnage et déplacement

### Objectifs

Système de vies et de score.
Obstacles dynamiques : Ajout d’obstacles en mouvement - Le joueur doit synchroniser ses mouvements pour les éviter
Deuxième niveau (Level 2): des obstacles supplémentaires et des plateformes de différentes hauteurs
bonus à collecter : Ajout d’objets spéciaux à ramasser et permet de allonger la durée (un bonus equivalent à 1s de plus)
Effets visuels adapté à la difficulté du niveau.
un compteur à rebours
monstre detecte la position, se deplace vers l hero et s il le touche il perd une vie des 3



### UML et conception

- Nouvelles classes : `Player`, `GameLoop`.
- Mise à jour du diagramme UML : relation `GameController → Player`.

---

### Répartition

| Membre   | Tâches                                    |
| -------- | ----------------------------------------- |
| Y 1      | Classe `Player` et gestion des mouvements |
| Y 2      | `GameController` (boucle d’animation)     |
| W 1      | Collisions et tests                       |
| w 2      | UML et documentation                      |


**Actions pour le prochain sprint :**

- Documenter le fonctionnement du `GameLoop`.
- Introduire une classe `Entity` abstraite pour préparer les obstacles et objets.
- Mettre en place un système de score et un affichage dynamique (HUD).
