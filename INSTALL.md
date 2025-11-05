# Installation Guide

This guide will help you set up Food Seer locally on your machine. You can choose between Docker installation (recommended) or manual installation.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation Method 1: Docker (Recommended)](#installation-method-1-docker-recommended)
- [Installation Method 2: Manual Installation](#installation-method-2-manual-installation)
- [Environment Configuration](#environment-configuration)
- [Running the Application](#running-the-application)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:

### For Docker Installation:
- [Docker](https://docs.docker.com/get-docker/) (version 20.10 or higher)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 2.0 or higher)
- Git

### For Manual Installation:
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) (version 11 or higher, 17 recommended)
- [Maven](https://maven.apache.org/download.cgi) (version 3.6 or higher) or [Gradle](https://gradle.org/install/)
- [Node.js](https://nodejs.org/) (version 16.x or higher)
- [npm](https://www.npmjs.com/) (version 8.x or higher) or [yarn](https://yarnpkg.com/)
- Git
- A modern web browser (Chrome, Firefox, Safari, or Edge)

---

## Installation Method 1: Docker (Recommended)

Docker installation is the easiest way to get started with Food Seer as it handles all dependencies automatically.

### Step 1: Clone the Repository

```bash
git clone https://github.com/NovaCorz/CSC510.git
cd CSC510
```

### Step 2: Configure Environment Variables

Create a `.env` file in the root directory:

```bash
cp .env.example .env
```

Edit the `.env` file with your configuration (see [Environment Configuration](#environment-configuration) section).

### Step 3: Build and Run with Docker

```bash
# Build the Docker containers
docker-compose build

# Start the application
docker-compose up
```

The application will be available at:
- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080`

### Step 4: Verify Installation

Open your browser and navigate to `http://localhost:3000`. You should see the Food Seer landing page.

To stop the application:
```bash
docker-compose down
```

---

## Installation Method 2: Manual Installation

If you prefer not to use Docker, follow these steps for manual installation.

### Step 1: Clone the Repository

```bash
git clone https://github.com/NovaCorz/CSC510.git
cd CSC510
```

### Step 2: Backend Setup (Java Spring Boot)

#### Verify Java Installation

```bash
# Check Java version (should be 11 or higher)
java -version

# Check Maven version
mvn -version
```

#### Install Backend Dependencies

```bash
# Navigate to backend directory (adjust path if different)
cd food-seer-backend

# Clean and install dependencies using Maven
mvn clean install
```

#### Configure Application Properties

Edit `src/main/resources/application.properties` or `application.yml` with your configuration:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/foodseer
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

# Or for H2 in-memory database (development):
# spring.datasource.url=jdbc:h2:mem:testdb
# spring.h2.console.enabled=true

# AI/ML Configuration
openai.api.key=your-openai-api-key

# CORS Configuration
cors.allowed.origins=http://localhost:3000
```

#### Build the Application

```bash
# Using Maven
mvn package
```

### Step 3: Frontend Setup

```bash
# Navigate to frontend directory (adjust path if different)
cd ../food-seer-frontend

# Install Node.js dependencies
npm install
```

### Step 4: Configure Environment Variables

**Frontend (.env in frontend directory):**
```bash
cp .env.example .env
```

Edit the `.env` file:
```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENV=development
```

---

## Environment Configuration

### Backend Configuration (application.properties)

Create or edit `backend/src/main/resources/application.properties`:

```properties
# Server Settings
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/foodseer
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# H2 Database (Alternative for Development)
# spring.datasource.url=jdbc:h2:mem:foodseer
# spring.datasource.driver-class-name=org.h2.Driver
# spring.h2.console.enabled=true
# spring.h2.console.path=/h2-console

# Logging
logging.level.root=INFO
logging.level.com.foodseer=DEBUG

# AI Service Configuration
ai.service.api.key=your-api-key-here
ai.service.endpoint=https://api.openai.com/v1

# CORS Settings
cors.allowed.origins=http://localhost:3000,http://localhost:3001
cors.allowed.methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed.headers=*
cors.allow.credentials=true

# File Upload Settings
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Session Configuration
spring.session.timeout=30m
```

### Frontend Environment Variables

Create `.env` file in the frontend directory:

```env
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENV=development
REACT_APP_API_TIMEOUT=30000
```

---

## Running the Application

### Using Docker

```bash
# Start all services
docker-compose up

# Start in detached mode (background)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Manual Run

You'll need to run both backend and frontend in separate terminal windows.

**Terminal 1 - Backend (Java Spring Boot):**

Using Maven:
```bash
cd food-seer-backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd food-seer-frontend
npm start
```

The application will be available at:
- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080/api`
- H2 Console (if enabled): `http://localhost:8080/h2-console`

---

## Database Setup

### Using MySQL

1. **Install MySQL** (if not already installed)

2. **Create Database:**
```sql
CREATE DATABASE foodseer;
CREATE USER 'foodseer_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON foodseer.* TO 'foodseer_user'@'localhost';
FLUSH PRIVILEGES;
```

3. **Update application.properties** with your database credentials

### Using H2 (In-Memory Database)

For quick development setup, use H2 database:

```properties
spring.datasource.url=jdbc:h2:mem:foodseer
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Access H2 Console at `http://localhost:8080/h2-console`

---

## Troubleshooting

### Common Issues

#### Port Already in Use

If you see an error like "port already in use":

```bash
# Find the process using the port (example for port 8080)
# On macOS/Linux:
lsof -i :8080
# On Windows:
netstat -ano | findstr :8080

# Kill the process or change the port in application.properties
```

#### Java Version Issues

**Problem:** Wrong Java version
```bash
# Check current Java version
java -version

# Set JAVA_HOME environment variable
# On macOS/Linux:
export JAVA_HOME=/path/to/java11
# On Windows:
set JAVA_HOME=C:\Program Files\Java\jdk-11

# Verify
echo $JAVA_HOME  # macOS/Linux
echo %JAVA_HOME%  # Windows
```

#### Maven Build Failures

**Problem:** Maven dependencies not downloading
```bash
# Clear Maven cache and rebuild
mvn clean install -U

# Force update snapshots
mvn clean install -U -DskipTests

# Clear local repository cache
rm -rf ~/.m2/repository
mvn clean install
```

#### Docker Issues

**Problem:** Docker build fails
```bash
# Clear Docker cache and rebuild
docker-compose down -v
docker-compose build --no-cache
docker-compose up
```

**Problem:** Permission denied errors
```bash
# On Linux, you may need to run with sudo or add your user to docker group
sudo usermod -aG docker $USER
# Log out and back in for changes to take effect
```

#### Database Connection Issues

**Problem:** Cannot connect to database
- Verify MySQL service is running:
  ```bash
  # On macOS:
  brew services start mysql
  # On Linux:
  sudo systemctl start mysql
  # On Windows: Start MySQL service from Services panel
  ```
- Check database credentials in `application.properties`
- Ensure database exists and user has proper permissions

#### Spring Boot Application Won't Start

**Problem:** Application fails to start
```bash
# Check logs for specific errors
mvn spring-boot:run

# Run with debug enabled
mvn spring-boot:run -Dspring-boot.run.arguments=--debug

# Verify all required dependencies are in pom.xml
mvn dependency:tree
```

#### Frontend Module Not Found Errors

```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install

# Clear npm cache
npm cache clean --force
npm install
```

#### AI/API Key Issues

**Problem:** AI recommendations not working
- Verify your API key is correctly set in `application.properties`
- Check that your API key has sufficient credits/permissions
- Review backend logs for specific error messages
- Test API key independently with a curl command

---

## Additional Setup

### Running Tests

**Backend Tests:**

Using Maven:
```bash
cd food-seer-backend
mvn src/test

# Run specific test class
mvn src/test -Dtest=YourTestClass

# Run with coverage
mvn src/test jacoco:report
```

### Code Coverage

**Backend (Maven with JaCoCo):**
```bash
cd food-seer-backend
mvn clean test jacoco:report
# View report at: target/site/jacoco/index.html
```

### Linting and Formatting

**Backend (Java):**
```bash
# Using Checkstyle (if configured in pom.xml)
mvn checkstyle:check

# Using SpotBugs
mvn spotbugs:check

# Format code with google-java-format
mvn com.coveo:fmt-maven-plugin:format
```

**Frontend:**
```bash
# Run linter
npm run lint

# Fix linting issues
npm run lint:fix

# Format code with Prettier
npm run format
```

### Building for Production

**Backend:**
```bash
cd food-seer-backend
mvn clean package -DskipTests
# JAR file will be in target/ directory
```

**Frontend:**
```bash
cd food-seer-frontend
npm run build
# Production build will be in build/ directory
```

---

## Next Steps

After successful installation:

1. **Create a test account** to explore the recommendation system
2. **Check the API endpoints** at `http://localhost:8080/swagger-ui.html` (if Swagger is configured)
3. **Review the API Documentation** 
4. **Check out the Demo Video** linked in the README
5. **Read the Contribution Guidelines** if you plan to contribute

For questions or issues, please open an issue on the [GitHub repository](https://github.com/NovaCorz/CSC510/issues).

---

## System Requirements

### Minimum Requirements
- **CPU:** 2 cores
- **RAM:** 4 GB (6 GB recommended for running both frontend and backend)
- **Storage:** 5 GB free space
- **OS:** Windows 10/11, macOS 10.15+, or Linux (Ubuntu 20.04+)
- **Java:** JDK 11 or higher

### Recommended Requirements
- **CPU:** 4+ cores
- **RAM:** 8 GB or more
- **Storage:** 10 GB free space
- **Internet:** Stable connection for AI API calls and dependency downloads