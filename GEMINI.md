# Project Overview

This is a parking management system built with Kotlin and Spring Boot. It uses a microservices architecture with separate services for different functionalities. The system uses PostgreSQL and MongoDB for data storage.

## Building and Running

### Prerequisites

- Java 21
- Docker

### Running the application

1.  **Set up the environment variables:**

    Create a `.env` file in the root of the project and add the following environment variables:

    ```
    DATABASE_HOST=localhost
    DATABASE_PORT=5432
    DATABASE_NAME=parking
    DATABASE_USERNAME=admin
    DATABASE_PASSWORD=admin
    DATABASE_CONTAINER_NAME=parking-db

    MONGODB_HOST=localhost
    MONGODB_PORT=27017
    MONGODB_DBNAME=parking
    MONGODB_USERNAME=admin
    MONGODB_PASSWORD=admin
    MONGODB_CONTAINER=parking-mongodb
    ```

2.  **Start the databases:**

    ```
    docker-compose up -d
    ```

3.  **Run the application:**

    ```
    ./gradlew bootRun
    ```

### Testing the application

To run the tests, use the following command:

```
./gradlew test
```

## Development Conventions

- The project uses Kotlin as the primary programming language.
- The project follows the standard Spring Boot project structure.
- The project uses Gradle for dependency management and building.
- The project uses Docker for running the databases in a containerized environment.
- The project uses `springdoc-openapi` to generate API documentation. You can access the Swagger UI at `http://localhost:8080/swagger-ui.html`.
- The project is divided into features, and each feature should follow the layered architecture described in the "Feature Structure" section. The `client` and `parking_spot` features serve as good examples of this structure.

## Feature Structure

Each feature in the project should follow a layered architecture, which consists of the following components:

-   **Controller:** The controller is responsible for handling HTTP requests and exposing the feature's API. It should be located in the feature's root directory and named `<Feature>Controller.kt`.
-   **Service:** The service contains the business logic for the feature. It should be located in the feature's root directory and named `<Feature>Service.kt`.
-   **Repository:** The repository is responsible for interacting with the database. It should be located in the feature's root directory and named `<Feature>Repository.kt`.
-   **Domain Model:** The domain model represents the feature's data. It should be located in the feature's root directory and named `<Feature>.kt`.
-   **DTOs:** The DTOs (Data Transfer Objects) are used for transferring data between the client and the server. They should be located in a `models` subdirectory and named `<Feature>Dto.kt`.
-   **Mapper:** The mapper is responsible for converting between DTOs and entities. It should be located in the `<Feature>Dto.kt` file.

This structure helps to keep the code organized and easy to maintain.

## Database Schema

The following diagram shows the database schema of the application:

```mermaid
erDiagram

    COUNTRY ||--o{ CITY : contains
    CITY ||--o{ PLACE : has

    PLACE ||--o{ PARKING : contains
    PARKING ||--o{ ENTRY_GATE : has

    PARKING ||--o{ PARKING_SPOT : has
    PARKING ||--o{ PARKING_EVENT : tracks

    USER ||--|| CARD : owns
    CARD ||--o{ CREDIT_SUPPLING : records

    
    ENTRY_GATE ||--o{ PARKING_EVENT : logs
    ENTRY_GATE ||--o{ GATE_MOVMENT: movLogs
    CARD ||--o{ PARKING_EVENT : authorizes

    COUNTRY {
        string id PK
        string name
        string isoCode
        datetime createdAt
    }

    CITY {
        string id PK
        string name
        string postalCode
        string stateCode
        string countryId FK
        datetime createdAt
    }

    PLACE {
        string id PK
        string name
        string addressLine
        string cityId FK
        float latitude
        float longitude
        datetime createdAt
    }

    PARKING {
        string id PK
        string placeId FK
        string name
        float latitude
        float longitude
        int totalCapacity
        int currentOccupied
        string status "OPEN|CLOSED|MAINTENANCE"
        datetime createdAt
    }

    ENTRY_GATE {
        string id PK
        string parkingId FK
        string name
        string direction "IN|OUT"
        string hardwareId
        bool isActive
        datetime createdAt
    }

    PARKING_SPOT {
        string id PK
        string parkingId FK
        string level
        string spotNumber
        string type "CAR|EV|HANDICAP"
        bool isOccupied
        datetime updatedAt
    }

    USER {
        string id PK
        string fullName
        string email
        string phone
        string status "ACTIVE|BLOCKED"
        datetime createdAt
    }

    CARD {
        string id PK
        string userId FK
        string cardNumber
        float creditBalance
        string status "ACTIVE|BLOCKED|EXPIRED"
        datetime issuedAt
        datetime expiresAt
    }

    PARKING_EVENT {
        string id PK
        string cardId FK
        string parkingId FK
        string entryGateId FK
        string direction "IN|OUT"
        datetime timestamp
        float chargedAmount
        int occupiedAfterEvent
    }

    GATE_MOVMENT {
        datetime placedAt
        string entryGateId FK
        string previousParking FK "Nullable"
        string newParking FK
        string raison "Nullable"
    }

    CREDIT_SUPPLING {
      string id PK
      string cardId FK
      float amount
      float feeTaken
      string source "ADMIN|ONLINE"
      string status "SUCCESS|FAILED"
      float balanceBefore
      float balanceAfter
      string reference
      datetime createdAt
    }

    CREDIT_FEE_FACTOR {
        string id PK
        float price
        string unit "US|TDN"
        datetime createdAt 
    }
```
