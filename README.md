# Parking Management System

A comprehensive Parking Management System built with Kotlin and Spring Boot. This system provides a robust backend for managing parking facilities, including clients, cards, parking spots, and various events like parking entries/exits and credit suppling.

## Features

*   Client Management
*   Card Management
*   Country, City, and Place Management
*   Parking Facility Management
*   Parking Spot Management
*   Entry Gate Management
*   Parking Event Logging (MongoDB)
*   Gate Movement Logging (MongoDB)
*   Credit Suppling Records (PostgreSQL)

## Tech Stack

*   **Backend**: Kotlin, Spring Boot 3
*   **Databases**:
    *   PostgreSQL: For core relational data.
    *   MongoDB: For logging and event-sourcing.
*   **Build Tool**: Gradle
*   **Containerization**: Docker, Docker Compose
*   **API Documentation**: SpringDoc (OpenAPI 3)

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

*   Java 21
*   Docker

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd parking-managment-system
    ```

2.  **Set up environment variables:**
    Create a `.env` file in the root of the project. You can copy the example below:
    ```env
    DATABASE_HOST=localhost
    DATABASE_PORT=5432
    DATABASE_NAME=parking
    DATABASE_CLIENTNAME=admin
    DATABASE_PASSWORD=admin
    DATABASE_CONTAINER_NAME=parking-db

    MONGODB_HOST=localhost
    MONGODB_PORT=27017
    MONGODB_DBNAME=parking
    MONGODB_CLIENTNAME=admin
    MONGODB_PASSWORD=admin
    MONGODB_CONTAINER=parking-mongodb
    ```

3.  **Start the databases:**
    Ensure Docker is running and execute the following command from the project root:
    ```bash
    docker-compose up -d
    ```
    This will start PostgreSQL and MongoDB containers in the background.

4.  **Run the application:**
    Use the Gradle wrapper to build and run the Spring Boot application:
    ```bash
    ./gradlew bootRun
    ```
    The application will be available at `http://localhost:8080`.

## Running Tests

To run the full suite of unit and integration tests, use the following command:

```bash
./gradlew test
```

## API Documentation

Once the application is running, you can access the interactive Swagger UI for a detailed API reference:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
