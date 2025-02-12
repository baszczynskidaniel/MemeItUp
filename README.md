# MemeItUp



**MemeItUp** is an engaging online multiplayer game where players create memes by filling in meme pictures with captions. Each round, players are given a random meme template and have a limited amount of time to come up with the funniest caption. Once everyone has submitted their memes, they are displayed one by one, and players vote on their favorites. The most entertaining memes score the most points, and the player with the highest score at the end of the game wins. It's a perfect game for game nights, streams, or just having fun with friends!

## Backend
The backend implementation is available [here](https://github.com/baszczynskidaniel/MemeItUpApi).

## Architecture
The project follows the **Model-ViewModel-Intent (MVI)** architecture and is built using **Kotlin Multiplatform** for iOS, Android, and Desktop.

## How to Run

### Prerequisites
- Install [Android Studio](https://developer.android.com/studio)

### Running on Desktop
1. Open Android Studio
2. Edit Configurations -> Add New Configuration -> Gradle
3. In the **Run** section, paste: `composeApp:run`
4. Run the configuration

### Running on Android
1. Change the run configuration to **Compose**
2. Run the app

### Running on iOS
1. Open the project in **Xcode**
2. Select a target device
3. Run the app

### App Settings

Settings for **MemeItUp** are implemented using a preference data store to store local user settings. This includes preferences for the app's theme, allowing users to customize their experience with their chosen visual style. Additionally, the language of the app can be changed to cater to a diverse range of players. The data store ensures that these settings are saved and applied every time the user accesses the game, providing a consistent and personalized experience.

### Internet Communication

**MemeItUp** leverages **Ktor** for efficient communication between the app and its server. Ktor is utilized for WebSocket communication with the server, ensuring real-time data exchange and seamless interaction during gameplay. Additionally, Ktor handles HTTP communication with an external Open API to fetch jokes, enhancing the entertainment value of the game. This robust framework ensures that all data transmission is reliable and swift, providing players with a smooth and engaging experience.

### Image Retrieval

**MemeItUp** utilizes **Coil** for dependency injection to manage the retrieval of images. Coil streamlines the process of accessing and injecting the necessary components, ensuring that images are efficiently retrieved and displayed within the app. This allows the game to quickly and reliably load meme templates and other visual content, enhancing the overall user experience.

### Dependency Injection

**MemeItUp** employs **Koin** for dependency injection throughout the app. Koin helps manage dependencies efficiently and ensures that all required components are properly injected where needed. This streamlines the development process and enhances the app's maintainability and scalability. By using Koin, **MemeItUp** maintains a clean architecture, facilitating easy updates and modifications to the app's functionality.


---
Enjoy MemeItUp! 🎉

