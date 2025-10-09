# Skip List Leaderboard: A Data Structure Case Study

This project is a **Spring Boot + D3.js application** that provides a real-time, interactive visualization of a **Skip List** data structure. It serves as a practical case study to demonstrate the performance and behavior of skip lists in a dynamic, high-write environment.

The application simulates a live leaderboard where player scores are constantly being added, updated, and removed. A single-page front-end consumes data from the backend API to render the skip list's internal state, allowing users to observe its structure as it changes. This makes it an effective educational tool for understanding this advanced data structure.

---

## Core Features

* **Real-Time Visualization:** Interactive D3.js graph displays the skip list's nodes, levels, and forward pointers, with pan and zoom functionality.
* **Dynamic Data Simulation:** Backend continuously simulates a live environment by adding, updating, and removing leaderboard entries.
* **Animated Search Algorithm:** Visually traces the O(log n) search path when a score is queried, demonstrating skip list efficiency.
* **Live Leaderboard Panel:** Sidebar continuously updates with the current top players, fetched from the skip list in real time.

---

## Technology Stack

* **Backend:** Java, Spring Boot, Maven
* **Frontend:** HTML5, CSS3, JavaScript (ES6+), D3.js

---

## Running Locally

### Prerequisites

* **Java Development Kit (JDK) 11** or higher.
* **Maven:** Not required separately; project includes Maven Wrapper.

### Instructions

1. **Clone the repository:**

   ```bash
   git clone https://github.com/BETTlM/LeaderBoardManager.git
   ```

2. **Navigate to the project directory:**

   ```bash
   cd LeaderBoardManager
   ```

3. **Build the project:**
   Compiles the application and downloads dependencies.

   * On macOS & Linux:

     ```bash
     ./mvnw clean install
     ```

   * On Windows:

     ```bash
     mvnw.cmd clean install
     ```

4. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

5. **View the application:**
   Open your browser at [http://localhost:8080](http://localhost:8080)

---

## API Endpoints

* `GET /api/visualization` → Returns a JSON object representing the full skip list structure for rendering.
* `GET /api/leaderboard` → Returns the top 15 players for the sidebar.
* `POST /api/search` → Accepts a JSON body with a key to search for; triggers the backend to trace the search path for the next visualization update.

---

## Project Team Members

* [Lohit G](https://github.com/zappvik)
* [Sanjith S J](https://github.com/BETTlM)
* [Madhan M](https://github.com/MMADHANCSE)