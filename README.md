# 420-310-epreuve-synthese-groupe2
Répartition du projet API RPG pour quatre personnes

Les membres sont désignés par A, B, C et D. Chaque tâche comprend les règles métier, les routes API nécessaires, la gestion des erreurs, les tests et la documentation correspondante.

**1. Répartition par sprint**

Les quatre sprints ci-dessous représentent un ordre de réalisation proposé. Leur durée doit être adaptée à l’échéance du cours.

| Étape | Personne A | Personne B | Personne C | Personne D |
|---|---|---|---|---|
| **Sprint 1 — Préparer une partie** | Création des joueurs et des héros : nom, unicité, caractéristiques et espèces. | Classes, calcul des PV et de la magie, équipement initial et restrictions par classe. | Création, entrée et sortie des lobbys, sélection des héros, limite de quatre joueurs et lancement de la partie. | Mise en place du dépôt, vérifications automatiques et structure du stockage en mémoire. Consultation de l’état des parties. |
| **Sprint 2 — Jouer un premier combat** | Bestiaire, création des créatures et comportements automatiques du Gobelin, de l’Orc et du Troll. | Rencontres disponibles, démarrage du combat, initiative, rounds, participant actif et fin du tour. | Attaques, cibles valides, dégâts, critiques, défense et vérification de la fin du combat. | Inventaire, armes, armures, calcul de la CA, potions et changement d’arme. Intégration de ces actions au combat. |
| **Sprint 3 — Compléter l’aventure** | Sorts du Mage et du Clerc : coûts, cibles, dégâts, soins et Bénédiction. | Expérience, répartition des XP, niveaux jusqu’à 5 et augmentation des PV maximum. | Récompenses, attribution des objets, repos et interdiction de rejouer une rencontre remportée. Fin de partie lorsque tous les héros sont vaincus. | Effets temporaires : poison, étourdissement et expiration des bonus de défense. Journal des événements et consultation par API. |
| **Sprint 4 — Étendre et livrer** | Première User Story originale et ses tests. | Deuxième User Story originale et ses tests. | Troisième User Story originale et ses tests. | Tests du parcours complet, vérification de la conservation de l’état entre appels et préparation de la version finale. |

Pendant le sprint 4, A, B et C participent également aux corrections et aux tests d’intégration. D révise les extensions et contribue à leur intégration. On ajuste la charge selon les estimations, car le nombre de tâches ne reflète pas leur difficulté.

**2. Décisions à prendre ensemble avant de coder**

- Définir les principaux concepts et leurs responsabilités : héros, inventaire, partie, rencontre, combat, action et effet.
- Convenir des routes API, des formats JSON et des réponses d’erreur.
- Définir une façon commune de lancer les dés, avec des résultats contrôlables dans les tests.
- Convenir des interfaces du stockage en mémoire et du bestiaire. Chaque membre implémente ensuite les besoins de ses fonctionnalités.
- Préparer les questions au client sur les règles ambiguës, par exemple les égalités d’initiative ou le cumul des effets.

Le serveur applique les règles. Une route permet d’attaquer ou de lancer un sort; elle ne permet pas au client de fixer librement les PV d’une cible.

**3. Transformer la répartition en issues GitHub**

Epic commun : « Permettre à un groupe de vivre une aventure RPG au tour par tour par API ».

Chaque cellule du tableau regroupe plusieurs tâches. Il faut la découper en issues d’environ quelques heures à deux jours, avec **une seule personne responsable par issue**.

Exemple de User Story :

> En tant que joueur, je veux créer un héros afin de pouvoir le sélectionner pour une aventure.

Critères d’acceptation :

- Le nom contient de 2 à 30 caractères, uniquement des lettres et des espaces, sans espace au début ou à la fin.
- Un nom déjà utilisé est refusé.
- Les caractéristiques choisies sont comprises entre 3 et 18 avant les ajustements d’espèce et ne dépassent jamais 20.
- Le héros commence au niveau 1, avec 0 XP et ses PV et points de magie au maximum.

Tâches associées :

| Tâche | Responsable initial |
|---|---|
| Valider le nom et son unicité | A |
| Appliquer les caractéristiques et les bonus d’espèce | A |
| Calculer les statistiques initiales selon la classe | B |
| Exposer la création par API et tester les réponses | A |
| Réviser l’ensemble et vérifier les critères | C |

Chaque issue reçoit une description, des critères d’acceptation, une estimation, ses dépendances, un label et un milestone de sprint.

Tableau Kanban : **À faire → En cours → En revue → Terminé**.

**4. Collaboration Git conforme au PowerPoint**

1. Conserver `main` stable et utiliser `develop` pour intégrer le travail.
2. Créer une branche courte par tâche depuis `develop`, par exemple `feature/12-creation-heros`. Remplacer 12 par le véritable numéro de l’issue.
3. Faire de petits commits : `feat(heros): ajoute la validation du nom`, `test(combat): couvre les coups critiques`.
4. Mettre sa branche à jour avec `develop` avant la revue. Ne jamais faire de rebase sur `main`, `develop` ou une branche partagée.
5. Ouvrir une PR vers `develop`, avec le besoin, les changements, les étapes de test et le lien vers l’issue.
6. Obtenir au moins une approbation d’un autre membre et des vérifications réussies avant la fusion. Privilégier le squash pour les PR de fonctionnalités.
7. Préparer une branche `release/*` pour la livraison, puis intégrer la version dans `main` et `develop` et poser un tag.

Le mot-clé `Closes #12` ferme automatiquement l’issue lorsque les conditions de fusion vers la branche par défaut sont remplies. Ne pas supposer qu’une fusion vers `develop` la fermera si la branche par défaut est `main`.

Rotation proposée des réviseurs :

| Sprint | PR de A | PR de B | PR de C | PR de D |
|---|---|---|---|---|
| 1 et 3 | B révise | C révise | D révise | A révise |
| 2 et 4 | C révise | D révise | A révise | B révise |

**5. Trois User Stories originales**

Pistes à présenter au client avant leur développement :

- **A : bouclier temporaire**, qui absorbe des dégâts avant de diminuer les PV.
- **B : fuite d’un combat**, avec des conditions de réussite et des conséquences sur la partie.
- **C : boss à plusieurs phases**, dont le comportement change selon ses PV.

Ce sont des propositions, pas des exigences du PDF. L’équipe doit préciser les règles, les interactions et les critères d’acceptation avec le client. Leur charge totale visée est d’environ neuf heures, conformément au projet; réduire leur portée si les estimations dépassent ce budget.

**6. Quand une tâche est-elle terminée?**

Une tâche est terminée lorsque ses critères d’acceptation sont satisfaits, ses tests passent, la PR est approuvée, la documentation est à jour et la fonctionnalité a été vérifiée dans la version intégrée.

Au début de chaque sprint, l’équipe estime et répartit les issues. Un point quotidien de 15 minutes sert à signaler l’avancement et les blocages. Chaque sprint se termine par une démonstration et une rétrospective.

Chaque membre doit pouvoir expliquer l’architecture générale et les interactions entre les modules. La réflexion individuelle reste le travail de chacun.
