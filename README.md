# WESTERN GOVERNORS UNIVERSITY
## D308 – Mobile Application Development (Android)

# Vacation Planner Android App

The **Vacation Planner** is a custom-built Android application designed to help users schedule and manage vacations with ease. This project served as my introduction to Android mobile development and provided hands-on experience with key aspects of mobile app design, development, and local data management.

---

## Project Highlights

This application was developed to demonstrate proficiency in:

- Custom vacation scheduling with associated excursions
- Full Create, Read, Update, and Delete (CRUD) functionality
- Configurable reminders (alerts) for vacation dates
- Data sharing capabilities using Android’s built-in share functionality
- Local database utilizing Room
- Responsive user interface utilizing Material Design

---

## Getting Started

### Home Screen
Upon launch, tap the **Enter** button to access the Vacation List screen.

---

### Vacation List
All added vacations are displayed here.

- Tap the **Add (+)** button to create a new vacation
- Tap the **menu icon (⋮)** in the top-right corner for additional options:
    - **Add Sample Data** to populate the app with example vacations and excursions
    - After adding sample data the screen will automatically reflect the data.
    - For each vacation a card will display and contain the name and date range.
    - If no vacations exist a message will indicate this.
    - Each card will contain a pencil icon and trash icon to edit and delete a vacation.
    - When deleting a vacation that contains users will receive an alert with a message to confirm.
    - If a vacation contains excursions the message will alert to notify the user that delete is not allowed.

---

### Vacation Details
To add or update a vacation:

- Enter the following fields:
    - Vacation Title
    - Hotel Name
    - Start Date
    - End Date
- Use the **menu (⋮)** to:
    - **Save Vacation**
    - **Set Reminders** for the start date, end date, or both
    - **Share** Vacation and Excursions details using available sharing options
- Alternatively, users can click the **Save** button at the bottom of the screen

---

### Excursion Details
To add an excursion to a vacation:

- Tap the **Add (+)** button from the Vacation Details screen
- Enter:
    - Excursion Title
    - Excursion Date
- The vacation’s date range is displayed above the excursion date field for reference
- Use the **menu (⋮)** to:
    - **Save Excursion**
    - **Delete Excursion**
- The **Save** button is also available at the bottom of the screen

> **Note:** An excursion’s date must fall within the vacation’s start and end dates.

---

### Editing and Deleting
- To **edit** a vacation, select it from the list, make any necessary updates, and tap **Save Vacation**
- To **delete** a vacation:
    - Open the vacation and select **Delete Vacation** from the menu, or
    - Tap the **trash can icon** directly from the Vacation List screen

> **Note** A vacation’s end date must be later than its start date. Start and End dates on the same day are valid.

### APK and GitLab
This application is packaged with a signed APK targeting **Android 14** and is ready for deployment.

The source code and project repository are available here in the project2 branch:  
https://gitlab.com/wgu-gitlab-environment/student-repos/cmcgarr/d308-mobile-application-development-android/-/tree/a7cb6ec6db2907a2b71152b3b08f1cb5764c73c7/

