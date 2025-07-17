# Java Backend Application

This is a Java 21-based backend application built using **Gradle** and backed by **PostgreSQL**. You can run the database using **Docker** (recommended) or use a local PostgreSQL installation.

---

## 📋 Prerequisites

Before running the application, make sure you have the following installed:

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Docker & Docker Compose](https://www.docker.com/products/docker-desktop) (if using Docker for PostgreSQL)
- [Gradle](https://gradle.org/install/) *(optional – use included `./gradlew` wrapper)*

---

## 🐘 Technology Stack

- Java 21
- Spring Boot
- Gradle
- PostgreSQL
- Docker (for DB container)

---

## 🧰 Setup Instructions

### 🧾 1. Clone the Repository

```bash
git clone https://github.com/w31rd00/ip-location-backend.git

cd ip-location-backend
```
### 🐳 2. Setup the database
You can run **docker compose up -d**, which will automatically create docker container 
with postgresql latest image, currently credentials like postgres credentials, are hardcoded in **docker-compose.yml**
file and can be changed. Alternatively you can configure **Postgresql** database locally, without docker, for the project.

### ⚙️ 3. Configure Database Connection
Make sure to configure ```src/main/resources/application.properties```
file for the database configuration, currently project comes with filled ```application.properties```
with hardcoded values for the database credentials, which matches the credentials in
```docker-compose.yml``` file.

### 🛠️ 4. Build the Application
```bash
./gradlew clean build
```

### ▶️ 5. Run the Application

#### Option 1: Run directly with Gradle
```bash
./gradlew bootRun
```
#### Option 2: Run the generated JAR
```bash
java -jar build/libs/ip-0.0.1-SNAPSHOT.jar
```

### 🧪 6. Run Tests
```bash
./gradlew test
```