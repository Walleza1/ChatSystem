# Clavardage v2.1

Système de conversation au sein d'un réseau local permettant l'échange de texte et de fichiers avec persistence des données en local.

## Déploiement

### Pré-requis
Windows, Linux, MacOS supportés. 
Java 1.8.0 ou supérieur (1.8.0_191 recommandé).

### Installation
Aller sur les [releases du projet](https://github.com/Walleza1/ChatSystem/releases), puis télécharger le fichier JAR de la release la plus récente.

### Exécution
Une fois le JAR télécharger, se placer dans le dossier courant du fichier, puis exécuter la commande suivante : 
```
java -jar chatsystem.jar
```
Le programme devrait se lancer.

## Utilisation
Entrer un nom d'utilisateur pour se connecter et échanger avec les autres utilisateurs en ligne. Plus de détails sur les consignes d'utilisation seront fournies dans le manuel d'utilisateur (voir sources).

### Précautions
Il est nécessaire de mettre en place notre solution dans un réseau local permettant le bon transit des paquets UDP et TCP utilisés par notre application. Vérifiez que votre réseau local est bien configuré et que le pare-feu de votre machine ne bloque pas certains paquets.

### Support
Pour tout problème d'utilisation, vous pouvez contacter [Vincent Erb](erb@etud.insa-toulouse.fr) ou [M M Jérôme Kompé](mampiani@etud.insa-toulouse.fr).

## Auteurs
* **Vincent Erb** - [VincentErb](https://github.com/VincentErb)
* **M M Jérôme Kompé** - [Walleza1](https://github.com/Walleza1)

## Annexes et Sources

### Manuel d'utilisateur
Joint à ce projet, un [manuel d'utilisateur](ManuelUtilisation.md) reprenant les points suivants :

* **Architecture du système**
* **Fonctionnalités du système**
* **Limites d'utilisation**

### Rapport de conception
Les diagrammes UML de différents types créés lors de la conception sont disponibles [ici](futur link vers les diagrammes).

### Sources d'apprentissage 
* [0] UDP Socket In Java : [UDP & Java](https://www.baeldung.com/udp-in-java)
* [1] Tutoriel JavaFX : [JavaFX 2015](https://code.makery.ch/fr/library/javafx-tutorial/).
* [2] JDK8 JavaDoc : [JDK Doc 8](https://docs.oracle.com/javase/8/docs/api/)
* [3] IO in Java : [IO Java](https://www.jmdoudoux.fr/java/dej/chap-flux.htm)
* [4] Serialize java : [Enregistrer les objets](http://blog.paumard.org/cours/java/chap10-entrees-sorties-serialization.html)
