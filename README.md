MYTHOS PLANNER (Desktop - Java/JavaFX)
🎯 Présentation
Mythos Planner Desktop est une application de bureau développée en Java 17 avec l’interface graphique réalisée en JavaFX 21.
Elle constitue une réplique fonctionnelle du projet web Symfony Mythos Planner, réalisée dans le cadre du module PIDEV 3A à Esprit School of Engineering.

L’application est conçue de manière modulaire, avec six modules principaux : Utilisateur, Événement, Publicité, Logistique & Services, Sponsor, Marchandise & Donation.
Elle gère différents types d’utilisateurs, chacun ayant accès à des fonctionnalités adaptées à son rôle : Administrateur, Organisateur ou Participant.

📚 Table des matières
Fonctionnalités

Technologies utilisées

Installation

Utilisation

Modules

Architecture

Contribution

Licence

✨ Fonctionnalités
Interface utilisateur responsive (JavaFX)

Gestion des événements, sponsors, équipements, publicités, marchandises

Multi-rôles utilisateur

Intégration de services externes : météo, cartographie, calendrier

Notifications (email / SMS via Twilio)

QR Codes pour événements et billets

Historique des contrats sponsor, score de fidélité, alertes automatiques

Export PDF & génération de rapports

🛠 Technologies utilisées
Langage : Java 17

GUI : JavaFX (FXML, CSS, Scene Builder)

Base de données : MySQL (via JDBC ou Hibernate)

ORM : Hibernate (ou JDBC pour les projets simples)

Bibliothèques externes :

Apache PDFBox (PDF export)

ZXing (QR Code)

OpenWeatherMap API

OpenStreetMap / Leaflet-JavaFX

Twilio Java SDK

Google Calendar API

Gemini ChatBot API (via REST)

🚀 Installation
Cloner le dépôt :

bash
Copier
Modifier
git clone https://github.com/votre-utilisateur/mythos-planner-desktop.git
cd mythos-planner-desktop
Importer le projet dans IntelliJ / Eclipse

Configurer la base de données :

Importer le fichier mythos_schema.sql

Mettre à jour les identifiants dans db.properties

Lancer l'application principale :

bash
Copier
Modifier
Main.java
💡 Utilisation
Lancer l’application depuis votre IDE

Créer un utilisateur selon le rôle voulu

Naviguer à travers les modules via le menu principal

Gérer les ressources liées à vos événements (pubs, sponsors, équipements, etc.)

🧩 Modules
1. 👤 Module Utilisateur
Authentification

Inscription avec rôles : Admin, Organisateur, Participant

Gestion des profils

2. 📅 Module Événement
Liste des événements

Ajout / Modification / Participation

Prévisions météo (API)

3. 📢 Module Publicité
Ajout de campagnes publicitaires

Affichage intelligent selon l’audience cible

4. ⚙ Module Logistique / Équipements
Réservation de matériel

Suivi logistique pour chaque événement

5. 💼 Module Sponsor
Ajout / modification de contrats sponsor

Score de fidélité, tendances, rappels de fin de contrat

6. 🛍 Module Marchandises
Ajout de produits pour chaque événement

Achat par les participants

Gestion des stocks

🏗️ Architecture
MVC (Model - View - Controller)

Séparation claire entre :

models (classes métier)

controllers (logique des actions utilisateurs)

views (FXML)

services (accès base de données, appels API)

🤝 Contribution
Contributions bienvenues !
Forkez le projet, créez une branche, envoyez un pull request.

📜 Licence
Ce projet est sous licence MIT.

