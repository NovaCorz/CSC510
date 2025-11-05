# Food Seer - Complete Setup Guide

This comprehensive guide will help you set up Food Seer locally on your machine, including the AI chatbot integration. You can choose between Docker installation (recommended) or manual installation.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation Method 1: Docker (Recommended)](#installation-method-1-docker-recommended)
- [Installation Method 2: Manual Installation](#installation-method-2-manual-installation)
- [Environment Configuration](#environment-configuration)
- [Database Setup](#database-setup)
- [AI Chatbot Setup (Ollama)](#ai-chatbot-setup-ollama)
- [Running the Application](#running-the-application)
- [Using the Application](#using-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Production Build](#production-build)

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:

### For Docker Installation:
- [Docker](https://docs.docker.com/get-docker/) (version 20.10 or higher)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 2.0 or higher)
- Git

### For Manual Installation:
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) (version 11 or higher, Java 21 recommended)
- [Maven](https://maven.apache.org/download.cgi) (version 3.6 or higher)
- [Node.js](https://nodejs.org/) (version 16.x or higher)
- [npm](https://www.npmjs.com/) (version 8.x or higher)
- [MySQL](https://dev.mysql.com/downloads/mysql/) (version 8.0 or higher)
- Git
- A modern web browser (Chrome, Firefox, Safari, or Edge)

### System Requirements

**Minimum:**
- **CPU:** 2 cores
- **RAM:** 4 GB (6 GB recommended for running both frontend and backend)
- **Storage:** 5 GB free space (10 GB with Ollama model)
- **OS:** Windows 10/11, macOS 10.15+, or Linux (Ubuntu 20.04+)

**Recommended:**
- **CPU:** 4+ cores
- **RAM:** 8 GB or more
- **Storage:** 10 GB free space
- **Internet:** Stable connection for AI API calls and dependency downloads

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

Open your browser and navigate to `http://localhost:3000`. You should see the Food Seer login page.

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
# Navigate to backend directory
cd food-seer-backend

# Clean and install dependencies using Maven
mvn clean install
```

#### Configure Application Properties

Edit `src/main/resources/application.properties` with your configuration:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/users
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# CORS Configuration
cors.allowed.origins=http://localhost:3000
```

### Step 3: Frontend Setup

```bash
# Navigate to frontend directory
cd ../food-seer-frontend

# Install Node.js dependencies
npm install
```

### Step 4: Configure Frontend Environment

Create `.env` file in the frontend directory:

```env
REACT_APP_API_URL=http://localhost:8080
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
spring.datasource.url=jdbc:mysql://localhost:3306/users
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Alternative: H2 Database (Development)
# spring.datasource.url=jdbc:h2:mem:foodseer
# spring.datasource.driver-class-name=org.h2.Driver
# spring.h2.console.enabled=true
# spring.h2.console.path=/h2-console

# Logging
logging.level.root=INFO
logging.level.com.foodseer=DEBUG

# AI Service Configuration (if using external AI)
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

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=604800000
```

### Frontend Environment Variables

Create `.env` file in the frontend directory:

```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_ENV=development
REACT_APP_API_TIMEOUT=30000
```

---

## Database Setup

### Using MySQL

1. **Install MySQL** (if not already installed)

2. **Create Database:**
```sql
CREATE DATABASE users;
CREATE USER 'foodseer_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON users.* TO 'foodseer_user'@'localhost';
FLUSH PRIVILEGES;
```

3. **Update application.properties** with your database credentials

The application automatically creates tables on startup and seeds an admin user with credentials:
- Username: `admin`
- Password: `admin123`

### Database Schema

The `User` entity includes:
- `id` - Primary key
- `username` - Unique username
- `email` - Unique email
- `password` - Encrypted password
- `role` - User role (ROLE_STANDARD, ROLE_ADMIN)
- `costPreference` - User's budget preference
- `dietaryRestrictions` - User's dietary restrictions

### Using H2 (In-Memory Database)

For quick development setup without MySQL:

```properties
spring.datasource.url=jdbc:h2:mem:foodseer
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Access H2 Console at `http://localhost:8080/h2-console`

---

## AI Chatbot Setup (Ollama)

The Food Seer chatbot uses Ollama with the gemma3:1b model for AI-powered food recommendations.

### What's Implemented

**Backend Components:**
- ✅ `ChatRequestDto` & `ChatResponseDto` - DTOs for chat communication
- ✅ `ChatService` & `ChatServiceImpl` - Service to communicate with Ollama API
- ✅ `ChatController` - REST endpoint at `/api/chat`
- ✅ Communicates with Ollama on `localhost:11434`

**Frontend Components:**
- ✅ `Chatbot.js` - Interactive chat component
- ✅ Asks 3 questions about mood, hunger level, and food preference
- ✅ Filters foods based on budget and dietary restrictions
- ✅ AI recommends ONE perfect food item
- ✅ Direct "Order Now" button
- ✅ Beautiful chat UI with typing indicators
- ✅ Added to navigation as "🤖 AI Assistant"

### Step 1: Download Ollama

#### For Windows:
1. Go to https://ollama.com/download
2. Click "Download for Windows"
3. Run the installer (`OllamaSetup.exe`)
4. Follow the installation wizard

#### For Mac:
1. Go to https://ollama.com/download
2. Click "Download for macOS"
3. Open the `.dmg` file and drag Ollama to Applications

#### For Linux:
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

### Step 2: Pull the gemma3:1b Model

After installation, open a terminal and run:

```bash
ollama pull gemma3:1b
```

This will download the model (~1GB). Wait for it to complete.

### Step 3: Start Ollama Server

Ollama should start automatically after installation. To verify it's running:

```bash
ollama list
```

You should see `gemma3:1b` in the list.

If Ollama isn't running, start it:
```bash
ollama serve
```

The server will run on `http://localhost:11434` by default.

### Step 4: Test Ollama

Test that Ollama is working:

```bash
ollama run gemma3:1b
```

Type a test message like "Hello!" and you should get a response. Type `/bye` to exit.

### How the Chatbot Works

1. **User Input Collection:** Asks 3 questions to understand current state
2. **Preference Integration:** Fetches saved budget and dietary restrictions
3. **Food Filtering:** Filters based on budget and dietary needs
4. **AI Analysis:** Sends filtered foods + responses to Ollama's gemma3:1b
5. **Recommendation:** AI selects best match with explanation
6. **Quick Order:** Direct order creation from recommendation

### Chatbot Architecture

```
┌─────────────┐      ┌──────────────┐      ┌─────────────┐      ┌──────────┐
│  Frontend   │ ───> │  Backend     │ ───> │  Ollama     │ ───> │ gemma3:1b│
│  Chatbot.js │      │ChatController│      │  API        │      │  Model   │
└─────────────┘      └──────────────┘      └─────────────┘      └──────────┘
       │                                            │                   │
       │ POST /api/chat                             │ POST /api/generate│
       │ {"message": "prompt"}                      │ {"model": "...",  │
       │                                            │  "prompt": "..."} │
       │                                            │                   │
       └────────────────────────────────────────────┴───────────────────┘
                    {"message": "AI response"}
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

You'll need to run the backend, frontend, and Ollama in separate terminal windows.

**Terminal 1 - Ollama (for AI chatbot):**
```bash
ollama serve
```

**Terminal 2 - Backend (Java Spring Boot):**
```bash
cd food-seer-backend
mvn spring-boot:run
```

**Terminal 3 - Frontend:**
```bash
cd food-seer-frontend
npm start
```

The application will be available at:
- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:8080`
- Ollama API: `http://localhost:11434`
- H2 Console (if enabled): `http://localhost:8080/h2-console`

---

## Using the Application

### Application Flow:

1. **Login Page** (`/`)
   - Use demo credentials: `admin` / `admin123`
   - Or register a new account
   - Clean, modern login interface

2. **Preferences Page** (`/preferences`)
   - After login, set your preferences
   - **Step 1**: Select budget (Under $10, Under $20, Under $30, or custom)
   - **Step 2**: Select dietary restrictions (Vegan, Vegetarian, Lactose intolerant, or custom)
   - Click "Next" between steps
   - Preferences saved to backend on completion

3. **Recommendations Page** (`/recommendations`)
   - View personalized recommendations
   - See saved budget and dietary restrictions
   - Update preferences anytime
   - Logout when done

4. **AI Assistant** (`/chatbot`) - **Customer Only**
   - Click "🤖 AI Assistant" in navigation
   - Answer 3 questions:
     - **Q1:** How are you feeling today? (e.g., "tired")
     - **Q2:** How hungry are you? (e.g., "very hungry")
     - **Q3:** What food are you in the mood for? (e.g., "comfort food")
   - AI analyzes responses with your preferences
   - Get ONE personalized food recommendation
   - Click "Order This Now!" to create order immediately
   - Click "Get Another Suggestion" to restart

---

## API Endpoints

### Authentication
- `POST /auth/login` - Login with username and password
- `POST /auth/register` - Register a new user

### User Management
- `GET /api/users/me` - Get current user info (requires authentication)
- `PUT /api/users/me/preferences` - Update user preferences (requires authentication)

### Chat/AI
- `POST /api/chat` - Send message to AI chatbot (requires authentication)

### Request/Response Examples

**Login Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Login Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

**Update Preferences Request:**
```json
{
  "costPreference": "under-20",
  "dietaryRestrictions": "vegan, lactose-intolerant"
}
```

**Chat Request:**
```json
{
  "message": "I'm feeling tired and want comfort food"
}
```

**Chat Response:**
```json
{
  "message": "Based on your preferences, I recommend..."
}
```

---

## Testing

### Backend Tests

Using Maven:
```bash
cd food-seer-backend
mvn test

# Run specific test class
mvn test -Dtest=YourTestClass

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests

```bash
cd food-seer-frontend
npm test
```

### Testing the Chatbot Integration

1. Start Ollama, backend, and frontend
2. Login as a **customer** (not admin/staff)
3. Navigate to "🤖 AI Assistant"
4. Complete the 3-question conversation
5. Verify recommendation is received
6. Test "Order This Now!" functionality
7. Test "Get Another Suggestion"

### Code Coverage

**Backend (Maven with JaCoCo):**
```bash
cd food-seer-backend
mvn clean test jacoco:report
# View report at: target/site/jacoco/index.html
```

---

## Troubleshooting

### Common Issues

#### Port Already in Use

**Problem:** Port 8080 (backend) or 3000 (frontend) already in use

```bash
# Find and kill process using port
# On macOS/Linux:
lsof -i :8080
lsof -i :3000
# Kill process:
lsof -ti:8080 | xargs kill -9

# On Windows:
netstat -ano | findstr :8080
# Kill using task manager or:
taskkill /PID <PID> /F
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

#### MySQL Connection Issues

**Problem:** Cannot connect to database

Solutions:
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
- Verify MySQL is listening on port 3306

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
# On Linux, add user to docker group
sudo usermod -aG docker $USER
# Log out and back in for changes to take effect
```

#### Spring Boot Application Won't Start

**Problem:** Application fails to start
```bash
# Check logs for specific errors
mvn spring-boot:run

# Run with debug enabled
mvn spring-boot:run -Dspring-boot.run.arguments=--debug

# Verify all required dependencies
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

#### CORS Errors

**Problem:** Frontend can't connect to backend

Solutions:
- Verify backend is running on port 8080
- Check `@CrossOrigin` annotation in controllers
- Verify `REACT_APP_API_URL` in frontend `.env`
- Check browser console for specific CORS error

#### Login Fails

**Problem:** Cannot login with admin credentials

Solutions:
- Verify backend is running
- Check that admin user was created (check logs)
- Verify API_BASE_URL in `src/services/api.js`
- Check browser console and network tab
- Test login endpoint directly:
  ```bash
  curl -X POST http://localhost:8080/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}'
  ```

### Chatbot-Specific Issues

#### "Failed to send message to AI"
- **Cause:** Ollama is not running
- **Fix:** Run `ollama serve` in a terminal

#### "No response from AI"
- **Cause:** Model not downloaded
- **Fix:** Run `ollama pull gemma3:1b`

#### "Error communicating with Ollama"
- **Cause:** Ollama running on different port
- **Fix:** Check Ollama is on port 11434 (default)
- **Verify:** `curl http://localhost:11434`

#### Backend 500 Error for Chat
- **Cause:** Backend can't reach Ollama
- **Fix:** Ensure Ollama is running
- **Test:** `curl http://localhost:11434`

#### AI Assistant Link Not Visible
- **Cause:** Not logged in as customer
- **Fix:** Ensure you're logged in with customer role, not admin/staff

---

## Production Build

### Backend

```bash
cd food-seer-backend
mvn clean package -DskipTests
# JAR file will be in target/ directory
# Run with: java -jar target/food-seer-backend-1.0.0.jar
```

### Frontend

```bash
cd food-seer-frontend
npm run build
# Production build will be in build/ directory
# Serve with any static file server
```

---

## Additional Features & Development

### Linting and Formatting

**Backend (Java):**
```bash
# Using Checkstyle (if configured)
mvn checkstyle:check

# Using SpotBugs
mvn spotbugs:check

# Format code
mvn com.coveo:fmt-maven-plugin:format
```

**Frontend:**
```bash
# Run linter
npm run lint

# Fix linting issues
npm run lint:fix

# Format with Prettier
npm run format
```

### Files Changed/Added for Chatbot

**Backend:**
- ✅ `food-seer-backend/src/main/java/FoodSeer/dto/ChatRequestDto.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/dto/ChatResponseDto.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/service/ChatService.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/service/impl/ChatServiceImpl.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/controller/ChatController.java` (new)

**Frontend:**
- ✅ `food-seer-frontend/src/pages/Chatbot.js` (new)
- ✅ `food-seer-frontend/src/services/api.js` (modified - added `sendChatMessage`)
- ✅ `food-seer-frontend/src/App.js` (modified - added `/chatbot` route)
- ✅ `food-seer-frontend/src/components/Navigation.js` (modified - added AI Assistant link)
- ✅ `food-seer-frontend/src/index.css` (modified - added chatbot styles)

### Future Enhancements

Potential improvements:
- 💾 **Conversation History:** Save past recommendations
- ⭐ **Rating System:** Let users rate recommendations to improve accuracy
- 🧠 **Learning:** Use past orders to personalize recommendations
- 📊 **Analytics:** Track acceptance/decline of recommendations
- 🎨 **Custom Prompts:** Allow users to customize AI personality
- 🔗 **Restaurant API Integration:** Connect to actual restaurant recommendation APIs
- 📍 **Location-Based:** Add location-based recommendations
- ❤️ **Favorites:** Implement favorite restaurants functionality

---

## Branch Information

Development is done on the `frontend-recreation` branch off of `dev`.

### Merging to Dev

When ready to merge:
```bash
git add .
git commit -m "Description of changes"
git push origin frontend-recreation
# Create pull request on GitHub
```

---

## Important Notes

- Frontend uses JWT tokens stored in localStorage for authentication
- Tokens expire after 7 days (configurable in backend)
- All API calls include Bearer token in Authorization header
- Protected routes redirect to login if not authenticated
- Admin user is automatically created on first run
- Database tables are created automatically on startup

---

## Getting Help

For questions or issues:
1. Check the [GitHub Issues](https://github.com/NovaCorz/CSC510/issues)
2. Review the API documentation at `http://localhost:8080/swagger-ui.html` (if Swagger configured)
3. Check the demo video linked in the README
4. Review contribution guidelines before submitting PRs

---

## Quick Start Summary

```bash
# 1. Clone repo
git clone https://github.com/NovaCorz/CSC510.git
cd CSC510

# 2. Setup MySQL database
# Create 'users' database

# 3. Install and start Ollama
ollama pull gemma3:1b
ollama serve

# 4. Start backend
cd food-seer-backend
mvn spring-boot:run

# 5. Start frontend (in new terminal)
cd food-seer-frontend
npm install
npm start

# 6. Access application
# http://localhost:3000
# Login: admin / admin123
```

Ready to start exploring Food Seer with AI-powered recommendations! 🍕🤖