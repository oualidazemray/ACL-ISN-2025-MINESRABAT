# 🎮 Projet LO3BA  
### (ACL-ISN 2025 –EnseM)

---

## 🏁 Sprint 2 – Séance de TP 3

### 🎯 Objectif du sprint
Refonte du moteur du jeu en migrant de **JavaFX** vers **Swing**,  
et mise en place d’un **système complet de niveaux** (10 niveaux jouables)  
avec gestion des **collisions**, **sons**, et **ressources graphiques**.

---

## 📋 Contenu à rendre

### 📅 À la fin de la séance de TP
- ✅ Backlog du sprint 2  
- 🧩 UML mis à jour (architecture Swing)  
- 👥 Répartition des tâches  

### ⏰ À 18h, la veille de la séance du sprint 3
- 🏷️ Code tagué `V-2` disponible  
- 🔍 Sprint Review  
- 💭 Sprint Retrospective  
- 🧾 Préparation du sprint 3  

---

## 🧱 Backlog Sprint 2

| ID  | En tant que… | Je veux…                                                       | Afin de…                                      | Priorité |
| --- | ------------- | -------------------------------------------------------------- | --------------------------------------------- | -------- |
| US8 | Développeur   | Migrer le projet de JavaFX à Swing                            | Simplifier le rendu et améliorer la stabilité | Haute    |
| US9 | Joueur        | Avoir une interface fluide sans FXML                          | Jouer directement avec une fenêtre Swing      | Haute    |
| US10 | Joueur       | Interagir avec les plateformes, pics et portes                | Avoir des obstacles et défis réels            | Haute    |
| US11 | Développeur  | Implémenter `LevelManager` et `LevelSelectScreen`             | Gérer la progression entre plusieurs niveaux  | Haute    |
| US12 | Joueur       | Jouer sur 10 niveaux différents                               | Avoir une expérience de jeu complète          | Haute    |
| US13 | Développeur  | Intégrer les sons, polices et textures dans les ressources    | Donner une identité visuelle et sonore        | Moyenne  |

---

## 👥 Répartition des tâches

| Membre        | Tâches principales                                                      |
| -------------- | ---------------------------------------------------------------------- |
| **Yassine 1**  | Migration du moteur graphique vers Swing (`GameUI`, `GameLoop`)        |
| **Yassine 2**  | Écran de sélection de niveau (`LevelSelectScreen`) et design visuel    |
| **Oualid**     | Création des entités `Platform`, `Spike`, `Door` et gestion collisions |
| **Wadie**      | UML, intégration des ressources (sons, images, polices, JSON)          |

---



---

## 🔍 Sprint Review

### ✅ Réalisations principales :
- Migration complète de **JavaFX vers Swing**
- Création et gestion de **10 niveaux** jouables via `LevelManager`
- Intégration d’un système de **collisions stable**
- Ajout des **ressources audio (wav)** et **graphiques (png)**
- Ajout de la **police personnalisée PressStart2P-Regular.ttf**
- Gestion du **JSON `levels.json`** pour définir la structure des niveaux

---

## 💭 Sprint Retrospective

| Ce qui a bien fonctionné 👍 | Ce qui doit être amélioré ⚠️ | Actions pour le prochain sprint 🚀 |
|-----------------------------|------------------------------|------------------------------------|
| Migration Swing fluide et stable | Absence d’écran de fin ou de score | Ajouter un écran de victoire et de défaite |
| Structure modulaire et claire | Niveau de difficulté constant | Introduire une progression de difficulté |
| Ressources bien intégrées (sons, textures, fonts) | Performances à optimiser | Ajouter double buffering et limiter le framerate |
| Bon travail d’équipe et communication | Peu de feedback visuel | Ajouter HUD et affichage du score |
| Gameplay fonctionnel                                | Gravité instable parfois     | Revoir la physique et collisions   |
| UML cohérent                                        | Rendu limité avec JavaFX     | Étudier une migration vers Swing   |
| Classe Player (déplacement, saut, gravité)          |                              |                                    |
| GameLoop et gestion du rafraîchissement             |                              |                                    |
| Création de la scène du jeu et intégration du HUD   |                              |                                    |
| UML et documentation du code                        |                              |                                    |


---

## 🧾 Préparation du Sprint 3

- 🎯 **Objectif :** Ajouter les **écrans de fin (Game Over / Victory)**, les **ennemis**, et un **système de score**.  
- 🧩 **UML :** Nouvelles classes `Enemy`, `GameOverScreen`, `VictoryScreen`, `HUD`.  
- 👥 **Organisation :** Amélioration du système de collisions et de la boucle de jeu.
- Nous allons rajouter une classe Ennemy qui va suivre notre joueur
- Nouveaux objets à collecter
- Thèmes par niveau
-  Mode speedrun
- 🏷️ **Tag Git attendu :** `V-3`


## 🗂️ Structure du projet

