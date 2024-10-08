# Comment lancer la solution

L'énoncé stipule Javascript et Java. J'ai donc interprété cela comme un front en Javascript et un back-end en Java. Dans le doute, le front-end possède également toute la logique de la simulation pour ne pas dépendre du back-end.

Un SSE a été mis en place pour le lien entre front et back, pour éviter de faire un polling de requête HTTP pour récuperer le prochain statut.

Pour lancer le front : `npm install && npm run run-server`. En ne passant pas par un serveur, le chargement de fichier de configuration rejete une erreur CORS.

Pour lancer le back-end : Lancer avec un IDE tel qu'IntelliJ

# Rappel de l'énoncé

L'objectif est d'implémenter une simulation de la propagation d’un feu de forêt.

Durée indicative de l’exercice : environ 2h/3h

La forêt est représentée par une grille de dimension h x l.
La dimension temporelle est discrétisée. Le déroulement de la simulation se fait donc étape par étape.
Dans l’état initial, une ou plusieurs cases sont en feu.

Si une case est en feu à l’étape t, alors à l’étape t+1 :
* Le feu s'éteint dans cette case (la case est remplie de cendre et ne peut ensuite plus brûler)
* et il y a une probabilité p que le feu se propage à chacune des 4 cases adjacentes

La simulation s’arrête lorsqu’il n’y a plus aucune case en feu
Les dimensions de la grille, la position des cases initialement en feu, ainsi que la probabilité de propagation, sont des paramètres du programme stockés dans un fichier de configuration (format libre).