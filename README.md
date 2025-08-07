🚀 Workforce Management API: Backend Challenge
Project Overview
Welcome to my solution for the Backend Engineer Challenge! This project is a Workforce Management API, a core component of a logistics super-app. I've taken the provided codebase, fixed some reported bugs, and implemented several highly-requested new features.

Bug Fixes 🐞
1. Duplicate Tasks: Solved the issue where task reassignment created duplicates by ensuring the old task is marked as CANCELLED before a new one is created.

2. Cluttered Task View: The fetch-by-date endpoint no longer includes cancelled or completed tasks, giving employees a cleaner, more relevant list.

New Features ✨
Smart Daily View: The fetch-by-date endpoint now returns a "smart" list that includes all active tasks for a given date range, plus any active tasks that are overdue.

Task Priorities: Tasks now have a Priority field (HIGH, MEDIUM, LOW), with new endpoints to update a task's priority and fetch tasks by priority.

Activity History & Comments: Every task has a complete activity log, automatically tracking key events like status changes. Users can also add free-text comments. All of this is included when you fetch a single task's details.

Technical Stack 🛠️
Language: Java 17

Framework: Spring Boot 3.0.4

Build Tool: Gradle

Data Storage: In-memory collections (no database required)

How to Run the Project 🏃
Clone the repository:

git clone https://github.com/vk2215/WorkForceManagement.git

Navigate to the project folder:

cd WorkForceManagement

Start the application:

./gradlew bootRun

The API will be accessible at http://localhost:8080.
