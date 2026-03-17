# 🗓️My-Desktop-Planner
"My Desktop Planner" is a Java desktop application designed for efficient task planning, enabling users to create, categorize, and manage simple, complex, and periodic tasks, as well as projects . "My Desktop Planner" serves as a comprehensive tool for organizing and prioritizing daily activities with a user friendly GUI built with JavaFx.
---
 
## 📖 Overview
 
**My Desktop Planner** helps users organize their daily activities by creating, categorizing, and scheduling tasks across a visual calendar interface. The application supports simple tasks, composite tasks, and periodic tasks, while also allowing users to group work into projects and manage time slots (créneaux) throughout the day.
 
---
 
## ✨ Features
 
- 🔐 **User Authentication** — Register and log in with a username and password
- ✅ **Task Management** — Create, edit, and track tasks with priorities, deadlines, and states
- 🗂️ **Task Types**
  - **Simple tasks** (`Tsimple`) — Basic one-off tasks
  - **Composite tasks** (`Tcomposee`) — Tasks decomposable into subtasks that fit within available time slots
  - **Periodic/Observable tasks** (`TacheObservable`) — Recurring tasks tracked over time
- 📁 **Categories** — Organize tasks with custom named categories and color coding
- 📋 **Projects** — Group related tasks under a named project with a description
- 🕐 **Time Slot Scheduling** (Créneaux) — Define free/busy time windows per day and auto-assign tasks
- 📅 **Calendar View** — Monthly calendar display with period selection for planning
- 🏅 **Badges** — Users earn performance badges (Good, VeryGood, Excellent) based on task completion
- 💾 **SQLite Persistence** — All data is saved locally via an embedded SQLite database
 
---
 
## 🛠️ Tech Stack
 
| Layer | Technology |
|---|---|
| Language | Java 11+ |
| UI Framework | JavaFX |
| Database | SQLite (via JDBC) |
| Build Tool | Maven / Gradle |
| Architecture | MVC (Model-View-Controller) |
 
 
---
 
## 🚀 Getting Started
 
### Prerequisites
 
- Java 11 or higher
- JavaFX SDK
- SQLite JDBC driver (e.g., `sqlite-jdbc-3.x.x.jar`)
 
### Setup
 
1. **Clone the repository**
   ```bash
   git clone (URL of the Repo)
   cd my-desktop-planner
   ```
 
2. **Add dependencies**
   - Ensure the JavaFX SDK is on your module path
   - Add the SQLite JDBC driver to your classpath
 
3. **Build and run**
   ```bash
   # With Maven
   mvn clean javafx:run
 
   # Or compile manually
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp sqlite-jdbc.jar src/**/*.java
   ```
 
4. **First launch**  
   The database is automatically created at `~/Documents/database.db` on first run.
 

---
 
## 🙋 Contributing
 
Contributions, issues, and feature requests are welcome. Feel free to open an issue or submit a pull request.
 
---
 
## 📄 License
 
This project was developed as part of a JavaFX course project. See `LICENSE` for details.
