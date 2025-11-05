# 🎮 Projet LO3BA

### (ACL-ISN 2025 –ENSEM)

---

## 🏁 Sprint 1 – Séance de TP 2

### 🎯 Objectif du sprint

Développer la **première version jouable** du jeu :

- ajout du **personnage principal** ;
- gestion du **déplacement et gravité** ;
- **système de niveaux** basique.

---

## 📋 Contenu à rendre

### 📅 À la fin de la séance de TP

- ✅ Backlog du sprint 1
- 🧩 Conception UML
- 👥 Répartition des tâches

### ⏰ À 18h, la veille de la séance du sprint 2

- 🏷️ Code tagué `V-1` disponible
- 🔍 Sprint Review
- 💭 Sprint Retrospective
- 🧾 Préparation du sprint 2

---

## 🧱 Backlog Sprint 1

| ID  | En tant que… | Je veux…                                                | Afin de…                            | Priorité |
| --- | ------------ | ------------------------------------------------------- | ----------------------------------- | -------- |
| US1 | Joueur       | Contrôler le dinosaure avec ← → et ^                    | Me déplacer dans le niveau          | Haute    |
| US2 | Joueur       | Voir un sol et des plateformes                          | Évoluer dans un environnement clair | Haute    |
| US4 | Développeur  | Mettre à jour l’UML (Player, Entity, Level)             | Structurer le code                  | Moyenne  |
| US5 | Développeur  | Ajouter une boucle de jeu (rafraîchissement du rendu)   | Gérer animations / mouvements       | Moyenne  |
| US7 | Joueur       | Passer d’un niveau à un autre après un objectif atteint | Découvrir une progression de jeu    | Haute    |

---

## 👥 Répartition des tâches

| Membre        | Tâches principales                                |
| ------------- | ------------------------------------------------- |
| **Yassine 1** | Classe `Player` (déplacement, saut, gravité)      |
| **Oualid**    | `GameLoop` et gestion du rafraîchissement         |
| **Yassine 2** | Création de la scène du jeu et intégration du HUD |
| **Wadie**     | UML et documentation du code                      |

---

## 🔍 Sprint Review

- ✅ Personnage jouable avec déplacements et saut
- ✅ Environnement de base (plateformes, sol)
- ✅ Boucle de jeu fluide
- ⚙️ Transition de niveau partiellement fonctionnelle

---

## 💭 Sprint Retrospective

| Ce qui a bien fonctionné 👍                       | Ce qui doit être amélioré ⚠️ | Actions pour le prochain sprint 🚀 |
| ------------------------------------------------- | ---------------------------- | ---------------------------------- |
| Gameplay fonctionnel                              | Gravité instable parfois     | Revoir la physique et collisions   |
| UML cohérent                                      | Rendu limité avec JavaFX     | Étudier une migration vers Swing   |
| Classe Player (déplacement, saut, gravité)        |                              |                                    |
| GameLoop et gestion du rafraîchissement           |                              |                                    |
| Création de la scène du jeu et intégration du HUD |                              |                                    |
| UML et documentation du code                      |                              |                                    |

---

## 🧾 Préparation du Sprint 2

- 🎯 **Objectif :** Migrer le projet vers **Swing** pour améliorer le rendu.
- 🧩 **UML :** Adapter les classes `GameUI`, `GameLoop`, `LevelManager`.
- 🧠 **Contenu :** Intégration des obstacles (`Platform`, `Spike`, `Door`).
- 🏷️ **Tag Git attendu :** `V-2`
