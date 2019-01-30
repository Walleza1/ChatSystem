# Manuel d'utilisation du système de Clavardage v2.1
Ce manuel d'utilisation accompagne l'application de Clavardage mise à disposition pour assurer des échanges textuels entre différents utilisateurs au sein d'un réseau local.

## Architecture du système
Ce paragraphe présente les caractéristiques techniques de l'application. Pour les consignes d'utilisation, passer au paragraphe suivant.

### Découverte des agents et communication
Dans sa version par défaut, le système opère une découverte des agents actifs sur le réseau local par une trame de broadcast UDP. Ensuite, la discusion avec un autre utilisateur (seules les discussions à deux interlocuteurs sont supportées actuellement) est gérée par l'établissement d'une connexion TCP, dans un souci de maintien de fiabilité.

### Persistence des messages
L'historique des conversations d'un utilisateurs sont sauvegardées en local sur la machine de ce dernier. Une base de données embarquée (h2 embedded) a été choisie pour mener à bien cette tâche. La base de données est solidaire de l'application Java et il n'est pas nécessaire d'avoir un serveur de base de données actif en parallèle.

### Design et vue dynamique
La vue de l'application a été réalisée avec JavaFX, ainsi que la librairie ControlsFX pour les notifications "push".

### Serveur de présence 

Lors du démarage de l'application, l'utilisateur peut choisir ou non d'utiliser un serveur de présence.

L'utilisation normale, c'est-à-dire sans serveur de présence, est locale à notre réseau. Ainsi toute personne non présente sur notre réseau local ne sera pas visible. 

Dans le cas de l'utilisation d'un serveur de présence, tout appareil connecté au réseau, et donc a signalé sa présence au serveur de présence, sera visible par l'utilisateur. Sa configuration est détaillée en fin de manuel (partie Administrateur)

## Fonctionnalités du système

### Connexion

À l'ouverture de l'application, vous arrivez sur un écran de connexion vous invitant à entrer un nom d'utilisateur. Il vous suffit d'entrer dans la zone de texte le nom choisi, puis de cliquer sur "Se connecter", ou appuyer sur entrée.

#### Nom d'utilisateur déjà pris

Si le nom d'utilisateur que vous avez choisi est déjà utilisé par un utilisateur en ligne sur l'application, vous recevrez un message d'erreur par le biais d'une fenêtre pop-up et serez invité à choisir un autre nom d'utilisateur.

#### Choix d'utilisation du serveur de présence

Si votre administrateur réseau a mis en place un serveur de présence pour tenir compte de la connexion de tous les utilisateurs, vous pouvez choisir d'activer ce mode en cliquant sur le bouton en bas à droite de l'écran.

### Conversations

Une fois que vous avez réussi à vous connecter, vous arrivez sur l'écran principal de l'application Clavardage 2.0  
La fenêtre qui apparaît ressemble à celle-ci :  
![Ecran principal](https://github.com/Walleza1/ChatSystem/blob/master/screenshot.png)  
L'écran de divise en trois colonnes verticales, dont nous allons détailler l'utilisation de gauche à droite.

#### Barre d'actions
La barre d'action regroupe des boutons dont nous allons détailler l'utilisation en allant de haut en bas : 
* Le premier bouton sert à changer de nom d'utilisateur. Un clic sur celui-ci ouvrira une fenêtre pop-up qui permet d'entrer un nouveau nom, si tant est que celui-ci soit disponible, c'est-à-dire qu'aucun autre utilisateur connecté ne le possède déjà.
* Le deuxième bouton est un compteur d'utilisateurs en ligne, il affiche simplement le nombre d'utilisateurs actuellement connectés à l'application et avec qui vous pouvez discuter.
* Le troisième bouton, "À-propos", fournit des informations de base sur la création et les auteurs de l'application.
* Le quatrième et dernier bouton en forme de croix et l'icone de déconnexion. Celui-ci permet de revenir à l'écran de connexion, tout en conservant l'historique des conversations avec les autres utilisateurs. (À noter que toutes les autres manières de fermer l'application conservent aussi les données).

#### Profil et liste d'utilisateurs
En haut de cette partie de l'écran, vous pouvez retrouver votre photo de profil ainsi que votre pseudo. La fonctionnalité de changement d'image de profil sera intégrée dans une prochaine mise-à-jour.  
Ensuite, une liste regroupe d'abord tous les utilisateurs connectés, puis les utilisateurs non connectés avec lesquels vous avez déjà échangé par le passé. En cliquant sur un utilisateur, vous ouvrez l'éran de conversation, à droite de l'écran.

#### Conversation
Cette partie de l'écran occupe la majorité de celui-ci. En haut, vous retrouvez le nom de votre interlocuteur, ainsi qu'un bouton en forme de croix à droite qui permet de fermer la discussion.  
En-dessous, l'affichage des messages horodatés entre l'utilisateur et vous est présent. 

##### Envoi de messages
Pour envoyer des messages, entrez du texte dans la zone de texte en bas de l'écran, puis cliquez sur "Envoyer" ou appuyez sur Entrée. Le message devrait s'afficher dans la liste des messages de la conversation.

##### Envoi de fichiers
Pour envoyer un fichier, cliquer sur l'icone "Fichier", à gauche de la zone de texte. L'explorateur de votre système d'exploitation s'ouvre, et vous pouvez choisir un fichier de tout type. Une fois sélectionné, le fichier est automatiquement envoyé à votre interlocuteur.

##### Consultation d'historique d'un utilisateur déconnecté
Il est possible de cliquer sur un utilisateur déconnecté et consulter l'ensemble des messages échangés avec lui, dans une interface identique à celle d'un utilisateur connecté. Seule différence, il est impossible d'envoyer de message ou de fichier à un utilisateur déconnecté.

### Notifications "push"
Un système de notifications apparaissant en surcouche de l'application, même si la fenêtre est réduite, est utilisé pour signaler à  l'utilisateur des événements importants. Une notification apparaît dans les cas suivants : 
* Un nouvel utilisateur est connecté
* Un utilisateur change de nom d'utilisateur
* Un utilisateur se déconnecte
* Un utilisateur vous envoie un fichier

### Réception de fichier
Si un utilisateur vous envoie un fichier, celui-ci est stocké dans le dossier ~/Clavardage/Téléchargements.

## Limites d'utilisation

### Limites de fiabilité liées à UDP

Lors de l'utilisation normale, sans serveur de présence, notre application utilise le protocole UDP, ce qui provoque un manque de garanti de fiabilité. Ainsi, le paquet de notification peut se perdre, créant ainsi un décalage.

Nous avons utilisé, pour nos tests, une répétition de l'envoi.

### Cas limites non vérifiés par des tests

Si deux utilisateurs se connectent dans un laps de temps très réduit, inférieur à la seconde, il est possible que le système ne les détecte pas comme étant tous deux connectés.  
Nous n'avons pu tester notre système qu'avec un maximum de 3 utilisateurs, nous ne pouvons donc pas assurer la scalabilité du système.

## Configuration du serveur de présence (Administrateur)

Si vous utilisez le serveur de présene, il est nécessaire de préciser son adresse IP sur les configurations de chaque utilisateur. Vous pouvez le faire en allant inscrire l'adresse dans le fichier .serverConfig du dossier ~/Clavardage


