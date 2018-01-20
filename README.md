Notre code s'organise en trois package : 
	- le default package contenant le main et une classe permettant de comparer les heuristiques,
	- le package game contenant toutes les classes permettant de résoudre une partie de rush hour,
	- le package GraphicGame conteant toutes les classes permettant de visualiser une partie.

Tous les paramètres sont à modifier dans la classe Main.

Dans un premier temps il faut choisir si on souhaite afficher la résolution d'une partie, via l'interface graphique,
ou plutôt comparer les différentes heuristiques. Ceci se fait par le booléen "graphicTest".

Si on choisit un affichage graphique de la solution, on peut alors choisir, via le booléan "useBruteForce",
soit de résoudre les parties en utilisant la méthode brute force, soit en utilisant une heuristique. Dans ce second
cas on peut alors choisir l'heuristique à utiliser via l'entier "typeHeuristic".

Si on choisit de comparer les différentes heuristiques l'idée va être d'afficher, pour 40 parties, et pour chaque
heuristique, la courbe du temps mis pour résoudre une partie (éventuellement en faisant la moyenne en calculant
plusieurs fois la résolution de la même partie).
Une fois ceci fait on peut soit afficher les courbes avec les parties triées de 1 à 40, soit triées les valeurs
par ordre croissant d'une heuristique donnée. Ceci permet par exemple de bien voir, pour une heuristique donnée,
quelles sont les autres heuristiques meilleures et celles moins bonnes.
Nous avons pour cela 5 paramètres à donner dans la fonction main :
	- SortedPrint qui indique si on laisse les courbes par ordre de 1 à 40 des parties ou si on trie selon
	une heuristique.
	- SortedPrintComp qui détermine par rapport à quelle heuristique trier si on le fait.
	- print qui permet d'afficher au fur à mesure les résultats des calculs fait en affichant, pour chaque
	partie et pour chaque heuristique, le temps moyen mis, le nombre d'états visités, et la longueur de la
	solution.
	- timeGraph qui s'il vaut true affiche le graphe des courbes du temps d'exécution, et sinon affiche le
	graphe du nombre d'états visités.
	- iter qui est le nombre d'itérations à faire pour moyenner le temps.