Tout d'abord nous utilisons dans notre code une librairie ext�rieure qu'il faut donc importer pour pouvoir utiliser
le code. Cette librairie se trouve dans le dossier lib.
Sous Eclipse il suffit de faire clic droit sur le dossier du projet, d'aller dans Build Path puis dans Configure
Build Path et ensuite d'ajouter les deux fichiers pr�sents dans le dossier lib via le bouton Add External JARs.

Ensuite, nous avons cod� l'interface graphique de mani�re � ce que celle-ci r�cup�re tous les fichiers textes,
commen�ant par "game", pr�sents dans le dossier "Games" qui doit �tre situ� � la racine du projet.
Vous trouverez pour l'instant dans ce dossier plusieurs exemples de parties que nous avons utilis�es pour nos tests.
Pour pouvoir r�soudre une autre partie et l'afficher via l'interface graphique il faut donc ajouter cette
partie dans le dossier "Games" et lui donner un nom commen�ant par "game".

Vous pouvez maintenant commencer � utiliser notre code.
Celui-ci s'organise en trois packages : 
	- le default package contenant le main et une classe permettant de comparer les heuristiques,
	- le package game contenant toutes les classes permettant de r�soudre une partie de rush hour,
	- le package GraphicGame contenant toutes les classes permettant de visualiser une partie.

Tous les param�tres sont � modifier dans la classe Main.

Dans un premier temps il faut choisir si on souhaite afficher la r�solution d'une partie, via l'interface graphique,
ou plut�t comparer les diff�rentes heuristiques. Ceci se fait par le bool�en "graphicTest".

Si on choisit un affichage graphique de la solution, on peut alors choisir, via le bool�en "useBruteForce",
soit de r�soudre les parties en utilisant la m�thode brute force, soit en utilisant une heuristique. Dans ce second
cas on peut alors choisir l'heuristique � utiliser via l'entier "typeHeuristic".

Si on choisit de comparer les diff�rentes heuristiques l'id�e va �tre d'afficher, pour 40 parties, et pour chaque
heuristique, la courbe du temps mis pour r�soudre une partie (�ventuellement en faisant la moyenne en calculant
plusieurs fois la r�solution de la m�me partie).
Une fois ceci fait on peut soit afficher les courbes avec en abscisse les parties de 1 � 40, soit tri�es les valeurs
par ordre croissant d'une heuristique donn�e. Ceci permet par exemple de bien voir, pour une heuristique donn�e,
quelles sont les autres heuristiques meilleures et celles moins bonnes.
Nous avons pour cela 5 param�tres � donner dans la fonction main :
	- SortedPrint qui indique si on laisse les courbes par ordre de 1 � 40 des parties ou si on trie selon
	une heuristique.
	- SortedPrintComp qui d�termine par rapport � quelle heuristique trier si on le fait.
	- print qui permet d'afficher au fur � mesure les r�sultats des calculs fait en affichant, pour chaque
	partie et pour chaque heuristique, le temps moyen mis, le nombre d'�tats visit�s, et la longueur de la
	solution.
	- timeGraph qui s'il vaut true affiche le graphe des courbes du temps d'ex�cution, et sinon celui du
	nombre d'�tats visit�s.
	- iter qui est le nombre d'it�rations � faire pour moyenner le temps.