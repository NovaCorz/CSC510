# FoodSeer Frontend Recreation - Setup Guide

This guide will help you set up and run the FoodSeer application.

## Prerequisites

1. **Java 21** - For running the Spring Boot backend
2. **Maven** - For building the backend
3. **MySQL** - Database for user management
4. **Node.js & npm** - For running the React frontend

## Backend Setup

### 1. Configure MySQL Database

Make sure MySQL is running on your machine. The application will automatically create the database if it doesn't exist.

Default configuration:
- Database: `users`
- Host: `localhost:3306`
- Username: `root`
- Password: `` (empty)

If your MySQL setup is different, update `food-seer-backend/src/main/resources/application.properties`

### 2. Create and update application.properties file

At the following path you will find a template file: food-seer-backend/src/main/resources/application.properties.template

Use this template and make a copy in the same folder called application.properties

Inside of this newly created application.properties file change the following
- spring.datasource.password: To whatever you have your MySQL password set to
- app.jwt-secret: Using https://emn178.github.io/online-tools/sha256.html make a jwt secret
- app.admin-user-password: To whatever you want the default admin password to be.

### 3. How to Set Up Ollama

#### Step 1: Download Ollama

##### For Windows:
1. Go to https://ollama.com/download
2. Click "Download for Windows"
3. Run the installer (`OllamaSetup.exe`)
4. Follow the installation wizard

##### For Mac:
1. Go to https://ollama.com/download
2. Click "Download for macOS"
3. Open the `.dmg` file and drag Ollama to Applications

##### For Linux:
```bash
curl -fsSL https://ollama.com/install.sh | sh
```

#### Step 2: Pull the gemma3:1b Model

After installation, open a terminal and run:

```bash
ollama pull gemma3:1b
```

This will download the model (~1GB). Wait for it to complete.

#### Step 3: Start Ollama Server

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


### 4. Build and Run Backend

```bash
cd food-seer-backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 5. Verify Backend

The application automatically creates an admin user with credentials:
- Username: `admin`
- Password: `what you have app.admin-user-password set to in the application.properties file`

You can test the login endpoint:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

## Frontend Setup

### 1. Install Dependencies

```bash
cd food-seer-frontend
npm install
```

### 2. Run Frontend

```bash
npm start
```

The frontend will start on `http://localhost:3000`

## Notes

- The frontend uses JWT tokens stored in localStorage for authentication
- Tokens expire after 7 days
- All API calls include the Bearer token in the Authorization header
- Protected routes redirect to login if not authenticated

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

