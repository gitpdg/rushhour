Tout d'abord nous utilisons dans notre code une librairie extérieure qu'il faut donc importer pour pouvoir utiliser
le code. Cette librairie se trouve dans le dossier lib.
Sous Eclipse il suffit de faire clic droit sur le dossier du projet, d'aller dans Build Path puis dans Configure
Build Path et ensuite d'ajouter les deux fichiers présents dans le dossier lib via le bouton Add External JARs.

Ensuite, nous avons codé l'interface graphique de manière à ce que celle-ci récupère tous les fichiers textes,
commençant par "game", présents dans le dossier "Games" qui doit être situé à la racine du projet.
Vous trouverez pour l'instant dans ce dossier plusieurs exemples de parties que nous avons utilisées pour nos tests.
Pour pouvoir résoudre une autre partie et l'afficher via l'interface graphique il faut donc ajouter cette
partie dans le dossier "Games" et lui donner un nom commençant par "game".

Vous pouvez maintenant commencer à utiliser notre code.
Celui-ci s'organise en trois packages : 
	- le default package contenant le main et une classe permettant de comparer les heuristiques,
	- le package game contenant toutes les classes permettant de résoudre une partie de rush hour,
	- le package GraphicGame contenant toutes les classes permettant de visualiser une partie.

Tous les paramètres sont à modifier dans la classe Main.

Dans un premier temps il faut choisir si on souhaite afficher la résolution d'une partie, via l'interface graphique,
ou plutôt comparer les différentes heuristiques. Ceci se fait par le booléen "graphicTest".

Si on choisit un affichage graphique de la solution, on peut alors choisir, via le booléen "useBruteForce",
soit de résoudre les parties en utilisant la méthode brute force, soit en utilisant une heuristique. Dans ce second
cas on peut alors choisir l'heuristique à utiliser via l'entier "typeHeuristic".

Si on choisit de comparer les différentes heuristiques l'idée va être d'afficher, pour 40 parties, et pour chaque
heuristique, la courbe du temps mis pour résoudre une partie (éventuellement en faisant la moyenne en calculant
plusieurs fois la résolution de la même partie).
Une fois ceci fait on peut soit afficher les courbes avec en abscisse les parties de 1 à 40, soit triées les valeurs
par ordre croissant d'une heuristique donnée. Ceci permet par exemple de bien voir, pour une heuristique donnée,
quelles sont les autres heuristiques meilleures et celles moins bonnes.
Nous avons pour cela 5 paramètres à donner dans la fonction main :
	- SortedPrint qui indique si on laisse les courbes par ordre de 1 à 40 des parties ou si on trie selon
	une heuristique.
	- SortedPrintComp qui détermine par rapport à quelle heuristique trier si on le fait.
	- print qui permet d'afficher au fur à mesure les résultats des calculs fait en affichant, pour chaque
	partie et pour chaque heuristique, le temps moyen mis, le nombre d'états visités, et la longueur de la
	solution.
	- timeGraph qui s'il vaut true affiche le graphe des courbes du temps d'exécution, et sinon celui du
	nombre d'états visités.
	- iter qui est le nombre d'itérations à faire pour moyenner le temps.