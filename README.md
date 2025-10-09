# Skip List Leaderboard: A Data Structure Case Study

This project is a Spring Boot and D3.js application that provides a real-time, interactive visualization of a Skip List data structure. It serves as a practical case study to demonstrate the performance and behavior of skip lists in a dynamic, high-write environment.

## Overview

The application simulates a live leaderboard where player scores are constantly being added, updated, and removed. A single-page front-end consumes data from the backend API to render the skip list's internal state, allowing users to observe its structure as it changes. The project is designed to be an effective educational tool for understanding this advanced data structure.

## Core Features

  * **Real-Time Visualization:** An interactive D3.js graph displays the skip list's nodes, levels, and forward pointers, with pan and zoom functionality.
  * **Dynamic Data Simulation:** A backend service continuously simulates a live environment by adding, updating, and removing leaderboard entries.
  * **Animated Search Algorithm:** Visually traces the O(log n) search path when a score is queried, demonstrating the efficiency of the data structure.
  * **Live Leaderboard Panel:** A sidebar continuously updates with the current top players, fetched from the skip list in real time.

## Technology Stack

  * **Backend:** Java, Spring Boot, Maven
  * **Frontend:** HTML5, CSS3, JavaScript (ES6+), D3.js

## Running Locally

To run this project on your local machine, follow the steps below.

### Prerequisites

  * Java Development Kit (JDK) 11 or higher.

You do not need to install Maven separately, as the project includes the Maven Wrapper.

### Instructions

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/BETTlM/LeaderBoardManager.git
    ```

2.  **Navigate to the project directory:**

    ```bash
    cd LeaderBoardManager
    ```

3.  **Build the project:**
    This command compiles the application and downloads all dependencies.

      * On macOS & Linux:
        ```bash
        ./mvnw clean install
        ```
      * On Windows:
        ```bash
        mvnw.cmd clean install
        ```

4.  **Run the application:**
    This command starts the backend server.

    ```bash
    mvn spring-boot:run
    ```

5.  **View the application:**
    Open your web browser and navigate to `http://localhost:8080`.

## API Endpoints

The backend exposes the following RESTful API endpoints:

  * `GET /api/visualization`: Returns a JSON object representing the entire skip list structure for rendering.
  * `GET /api/leaderboard`: Returns a list of the top 15 players for the sidebar.
  * `POST /api/search`: Accepts a JSON body with a key to search for. This triggers the backend to trace the search path for the next visualization update.