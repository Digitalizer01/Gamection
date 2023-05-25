# Gamection: Android Game Collection Manager

Gamection is an Android application written in Kotlin that allows you to manage your video game collection. It leverages the real-time Firebase database to store and retrieve data, providing a seamless user experience. This repository contains the source code and project files for the Gamection Android app.

## Features

- User Authentication: To access the database, Gamection requires a username and password. If you don't have an account, you can create one by providing the necessary information. Once created, user data is securely encrypted and stored.

- User Profile: Gamection stores various user details, including the username, name, gender, date of birth, friends, and game library.

- Game Library: The core functionality of Gamection revolves around managing your game collection. The library is organized into consoles, each of which contains a list of games. Additionally, it keeps track of the user's friends.

- Game Information: Each game entry in the library includes the genre to which it belongs and the date it was added to the collection.

## Firebase Real-time Database Structure

The Firebase real-time database for Gamection follows the following structure:

- **Users**: Stores user-related data, including the username, name, gender, date of birth, friends, and game library.

- **Library**: Consists of consoles, where each console has a list of associated games and the user's friends.

- **Game**: Represents an individual game entry, containing its genre and the date it was added to the library.

## Firebase Credentials

To use the Gamection Android app with Firebase, you will need to provide the necessary Firebase credentials. Please note that the specific details about the Firebase credentials, such as the Firebase project ID, API key, and other configuration settings, are not included in this repository.

To set up the Firebase credentials for the app, follow these steps:

1. Create a new Firebase project on the [Firebase Console](https://console.firebase.google.com/).
2. Generate the required Firebase configuration files (google-services.json) and place them in the appropriate directory within your project.
3. Replace the placeholder values in the configuration files with your actual Firebase credentials.
4. Build and run the Gamection Android app on your device or emulator.

Make sure to keep your Firebase credentials secure and avoid committing them to version control or sharing them publicly. It is recommended to use a secure and private method to handle Firebase credentials, such as using environment variables or a secure configuration file that is excluded from version control.

Please refer to the Firebase documentation for detailed instructions on setting up Firebase credentials for your Android app.
