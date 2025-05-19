# NutriJoy - Application de Gestion des Habitudes Alimentaires [Projet de groupe L2 semestre 2]

**Version :** 1.0  
**Date de création :** 26/05/2024  

---

## **Présentation**
NutriJoy est une application mobile Android conçue pour aider les utilisateurs à surveiller et gérer leurs habitudes alimentaires. L'application permet de scanner les codes-barres des produits alimentaires et de visualiser les informations nutritionnelles à l'aide de graphiques clairs et interactifs.

---

## **Fonctionnalités principales**
- **Scan de codes-barres** : Obtenez instantanément les informations nutritionnelles des produits scannés.
- **Recherche par mots-clés** : Recherchez manuellement des produits.
- **Historique et favoris** : Suivez les produits déjà scannés ou enregistrez vos produits préférés.
- **Comparaison des produits** : Comparez deux produits pour faire des choix nutritionnels éclairés.
- **Statistiques** : Visualisez la consommation calorique par jour, semaine ou mois grâce à des diagrammes détaillés.
- **Thème sombre** : Choisissez un mode sombre pour une meilleure lisibilité.

---

## **Technologies utilisées**
- **Backend :**
  - MongoDB (système de gestion de bases de données)
  - Firebase (authentification et gestion des sessions)
  - Node.js (exécution des scripts backend)
  - Retrofit (requêtes HTTP vers l'API OpenFoodFacts)

- **Frontend :**
  - Android Studio (environnement de développement)
  - Java et XML (développement de l'interface)

---

## **Structure du projet**
- **`activities`** : Contient les classes gérant les différentes pages de l'application.
- **`model`** : Définit les objets manipulés dans l'application.
- **`bdd`** : Gestion de la communication avec la base de données.
- **`network`** : Requêtes réseau vers l'API OpenFoodFacts.
- **`adapters`** : Affichage des listes déroulantes.
- **`fragments`** : Gestion des fenêtres et transitions de pages.

---

## **Installation**
1. Cloner le dépôt :  
   ```bash
   git clone https://github.com/whoismathieu/nutrijoy.git
   cd nutrijoy
   ```

2. Ouvrir le projet avec Android Studio.

3. Compiler et exécuter l’application sur un émulateur ou un appareil Android.

---

## 📊 **Statut des fonctionnalités**
| Fonctionnalité                       | Statut    |
|--------------------------------------|-----------|
| Scan de codes-barres                 | ✅         |
| Recherche par mots-clés              | ✅         |
| Historique et favoris                | ✅         |
| Comparaison de produits              | ✅         |
| Statistiques (jour, semaine, mois)   | ✅         |
| Thème sombre                         | ✅         |
| Filtrage avancé des produits         | ❌ (à venir) |

---

## 💡 **Perspectives d’amélioration**
- **Suggestions de recettes** basées sur les besoins nutritionnels de l'utilisateur.
- **Amélioration de la base de données** : Migration vers une base de données locale plus performante.
- **Intégration de l’intelligence artificielle** : Propositions personnalisées de produits ou de recettes.

---

## **Équipe**
- [**Mathieu Moustache**](https://github.com/whoismathieu)
- [**Vincent Tan**](https://github.com/20centan)
- [**Ali Traore**](https://github.com/Taliii7)
- [**Abdel Malik Djaffer**](https://github.com/malik439)

---

## 📚 **Ressources**
- [OpenFoodFacts API](https://openfoodfacts.github.io/openfoodfacts-server/api/)
- [MongoDB Java SDK](https://www.mongodb.com/docs/atlas/device-sdks/sdk/java/)
- [Retrofit](https://square.github.io/retrofit/)
- [Firebase](https://firebase.google.com/docs)
- [Android Studio](https://developer.android.com/studio)
