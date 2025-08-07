# Workforce Management API

Welcome to the **Workforce Management API**! This is a backend system designed to help managers assign, track, and manage tasks for employees. The system is built using **Spring Boot** with **Gradle** and handles features such as task assignment, prioritization, task history, and more.

---

## 🚀 Project Setup

### Prerequisites
    - Java 17
    - Gradle 7.x or higher
    - IDE (e.g., IntelliJ IDEA, VSCode)

### 1. Clone the Repository
    git clone https://github.com/yourusername/workforcemgmt.git
    cd workforcemgmt

### 2. Build the Project
    ./gradlew build
  
### 3. Run the Application
    ./gradlew bootRun
  
Visit: http://localhost:8080

### Features Implemented
    ✅ Bug Fixes
    Duplicate Task on Reassignment: Old task is marked CANCELLED when reassigned.
    Cancelled Tasks Hidden: Fetching tasks excludes those marked as CANCELLED.

    ✅ New Features
    Smart Daily Task View: Shows tasks starting in the range + still active tasks from earlier.
    Task Priority: Add/change task priority (HIGH, MEDIUM, LOW).
    Task Comments & Activity History: Tracks all events and allows free-text comments.

### 💻 Running & Testing
    Start the app and use Postman or Insomnia to interact with the API at:
    http://localhost:8080
