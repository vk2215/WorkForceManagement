🚀 Workforce Management API: Project Summary
Welcome to my submission for the Backend Engineer Challenge! This project is a Workforce Management API built with Spring Boot and Java 17.

🐞 Bugs Fixed
Task Reassignment: When reassigning a task, the old task is now automatically marked as CANCELLED to avoid duplicates.

Cluttered Task List: The task-fetching endpoint now correctly filters out all CANCELLED and COMPLETED tasks.

✨ New Features Implemented
Smart Daily View: The API now provides a "smart" task list that includes all active tasks for a given date, plus any overdue tasks.

Task Priorities: I added a new Priority field (HIGH, MEDIUM, LOW) and new endpoints to update a task's priority and fetch tasks by priority.

Comments & History: Each task now has a full activity history and supports user comments, giving team leads a complete audit trail.

⚙️ How to Run the Project
Clone the repo: git clone https://github.com/vk2215/WorkForceManagement.git

Run the app: ./gradlew bootRun

The API will be live at http://localhost:8080.
