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

Dans le cas de l'utilisation d'un serveur de présence, tout appareil connecté au réseau, et donc a signalé sa présence au serveur de présence, sera visible par l'utilisateur.

## Fonctionnalités du système

### Connexion

#### Nom d'utilisateur déjà pris

### Choix d'utilisation du serveur de présence

### Liste d'utilisateurs

### Conversation

### Envoi de fichiers

### Consultation d'historique d'un utilisateur déconnecté

### Changement de nom d'utilisateur

### Déconnexion

## Limites d'utilisation

### Limites de fiabilité liées à UDP

Lors de l'utilisation normale, sans serveur de présence, notre application utilise le protocole UDP, ce qui provoque un manque de garanti de fiabilité. Ainsi, le paquet de notification peut se perdre, créant ainsi un décalage.

Nous avons utilisé, pour nos tests, une répétition de l'envois.

### Cas limites non vérifiés par des tests
