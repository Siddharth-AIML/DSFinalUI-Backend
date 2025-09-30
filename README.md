

# Smart Traffic Management System

The Smart Traffic Management System is a distributed, real-time application designed to control and optimize urban traffic flow. It provides a web-based dashboard for monitoring and managing traffic signals across multiple intersections, built on a scalable and robust backend architecture.

## Features

  * **Real-time Dashboard:** A user-friendly web interface to monitor the status of all traffic signals.
  * **Distributed Architecture:** Utilizes Java RMI to run core components as independent, scalable services.
  * **Load Balancing:** An integrated load balancer distributes requests across services to ensure high availability and prevent bottlenecks.
  * **Multi-Database Support:** Employs a partitioned database schema to manage different types of data efficiently.
  * **RESTful API:** A clean API for communication between the frontend and the backend services.

## System Architecture

The application is built with a multi-tier, distributed architecture:

  * **Presentation Layer:** A static web frontend built with HTML, CSS, and JavaScript that serves as the user interface.
  * **Application Layer:** A Spring Boot application that provides the core business logic.
      * **Web Server:** Exposes a REST API for the frontend to consume.
      * **Java RMI Services:** Core components like the `SignalController` and `SignalManipulator` are implemented as RMI services, allowing them to be distributed across a network.
      * **Load Balancer:** A custom load balancer distributes incoming requests to the available RMI services using a round-robin algorithm.
  * **Data Layer:** A multi-database architecture using MySQL to persist data. The system is configured to use three separate databases for different concerns: `signalcontrollerdb`, `signalmanipulatordb`, and `pedestriansignalmanipulatordb`.

## Prerequisites

Before you begin, ensure you have the following installed:

  * **Java Development Kit (JDK):** Version 17 or higher.
  * **Apache Maven:** For building and managing the project.
  * **MySQL:** The database for storing application data.

## Setup and Installation

1.  **Clone the Repository:**

    ```bash
    git clone <your-repository-url>
    cd DSFinalUI-Backend-f60cb45c4b4ebd64e3433b2945caacc5471fd399
    ```

2.  **Database Setup:**

      * Start your MySQL server.
      * Create the three required databases by executing the following SQL commands:
        ```sql
        CREATE DATABASE signalcontrollerdb;
        CREATE DATABASE signalmanipulatordb;
        CREATE DATABASE pedestriansignalmanipulatordb;
        ```

3.  **Configure Application Properties:**

      * Open the `src/main/resources/application.properties` file.
      * Update the `spring.datasource.username` and `spring.datasource.password` properties for all three databases to match your MySQL credentials.

## Running the Application

You can run the application using the provided Maven wrapper.

1.  **Make the Maven Wrapper Executable (for Linux/macOS):**

    ```bash
    chmod +x mvnw
    ```

2.  **Build the Project:**

    ```bash
    ./mvnw clean install
    ```

3.  **Run the Application:**

    ```bash
    ./mvnw spring-boot:run
    ```

The application will start on the default port (usually 8080). You can access the dashboard by navigating to `http://localhost:8080` in your web browser.

# Vidyalankar Institute of Technology 
**Guided By Dr. Amit Nerurkar** 
*Team Members* <br>
Siddharth Metkari <br>
Rishant Singh <br>
Aryan Kulkarni <br>
Swathi Pillai <br>
Jyotsna Kasibhotla <br>
