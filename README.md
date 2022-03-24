# Calculatrice Graphiquue

*Calculatrice graphique programmée avec JAVA, qui permet de tracer le graphe d'une fonctions (ou plusieurs) à partir d'une formule et d'un intervalle donnée par l'utilisateur.*

## Fonctionnalités :

- Une page de connexion qui permet à l'utilisateur de s'identifier directement avec un username pour acceder à la page de la calculatrice graphique.
- Une interface graphique jolie et bien soignée crée à l'aide de fichiers FXML et CSS.
- Traçage d'une fonction à partir d'une expression donnée par l'utilisateur, qui est d'abord analysé à l'aide de la classe "Parser.java"
- Possiblité de choisir un intervalle de définition de la fonction, si non la fonction sera tracée sur un intervalle définit par défaut.
- Superposition de plusieurs graphes de fonctions sur la meme fenetre.
- Possibilité de supprimer le graphe d'une fonction et son expression et son intervalle (si donné) à partir d'un bouton "Supprimer".
- Possibilité de supprimer tous les graphes ( + leurs expressions + intervalles) à la fois afin d'ouvrir une nouvelle page propre
- Tout utilisateur peut enregistrer ses fonctions à l'aide d'un bouton "Enregitrer" pour les retrouver ensuite dans son historique qui se trouve sur la page de la calculatrice graphique.
- Un historique propre à chaque utilisateur qui contient toutes les fonctions enregistrées par ce dernier.
- Possibilité de selectionner une fonction à partir de l'historique ( elle sera donc chargé depuis la BDD) et ensuite la tracer.
- Une Base De Données sous le nom de "UsersList" qui nous permet de sauvegarder une liste des utilisateurs ainsi que les listes de fonctions sauvegardées par chaque utilisateur.
- Possibilité de zoomer et dézoomer sur la fenetre en utilisant la roulette de la souris
    - Un zoom/dézoom sur la zone de traçage du graphe.
    - Un zoom/dézoom sur l'axe des abscisses (X) seulement.
    - Un zoom/dézoom sur l'axe des ordonnées (Y) seuelement.
- Possibilité de zoomer une zone selectionnée avec la souris par l'utilisateur  (en utilisant le bouton gauche pour commencer la selection et le gardant enfoncé en glissant).
    - On peut également selectionnée une large zone à zoomer en commençant la selection sur l'axe (Y) et ensuite glisser à droite
- Possibilité de naviguer (se déplacer) sur l'ensemble du graphe en utilisant le bouton droit de la souris et en le gardant pressé durant tout le déplacement :
    - On peut se déplacer sur la zone de traçage du graphe.
    - On peut se déplacer seulement sur l'axe des abscisses (X).
    - On peut se déplacer seulement sur l'axe des ordonnées (Y).
- Possibilité de faire un AutoZoom à l'aide d'un bouton "AutoZoom" ou simplement avec un double clic avec le bouton gauche de la souris, cela nous permettra de redimensionner les axes (X) et (Y) aux valeurs minimales des trois intervalles de fonctions.
- Possibilité de quitter la fenetre de la calculatrice graphique à l'aide du bouton "quitter".
- Une fenetre d'aide rattachée à la page d'accueil qui explique comment on peut se servir du programme.



## Comment Utiliser la Calculatrice Graphique ?

- Afin de faciliter l'utilisation de cette calculatrice graphique,j'ai crée un script sous le nom de "launch.sh" qui se trouve dans : src/application/launch.sh
- Pour lancer le programme sur un terminal, il suffit d'ouvrir le terminal et ensuite se placer dans le répertoire du projet "src/application" et ensuite lancer la commande: ./launch.sh
- Le script se chargera de récuperer le chemin courant de l'utilisateur ( grace à $PWD) et ensuite acceder au repertoire où se trouve tous le programme de la calculatrice graphique
- La compilation des classes sera faite automatiquement après le lancement de la commande citée ci-dessus, et ensuite l'execution automatique de la classe Main qui lancera le programme.
- Je me suis chargé de mettre toute la bibliothèque de JAVAFX nécessaire pour l'utilisation pour que l'utilisateur n'aie pas à l'installer lui-meme.  
- A la femeture du programme, tous les fichiers avec lextension " .class " seront  directement supprimés de la machine de l'utilisateur.
- Un rapport expliquant comment utiliser l'interface graphique est mis à votre disposition sur le répertoire "src/"
    
 
