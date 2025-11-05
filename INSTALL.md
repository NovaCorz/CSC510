# FoodSeer Complete Setup Guide

This comprehensive guide covers the complete setup of the FoodSeer application, including the AI chatbot integration with Ollama.

## Branch Information

Current branch: `frontend-recreation`

Created off of `dev` to ensure safe development without losing work.

## Prerequisites

1. **Java 21** - For running the Spring Boot backend
2. **Maven** - For building the backend
3. **MySQL** - Database for user management
4. **Node.js & npm** - For running the React frontend
5. **Ollama** - For AI-powered food recommendations

## Backend Setup

### 1. Configure MySQL Database

Make sure MySQL is running on your machine. The application will automatically create the database if it doesn't exist.

Default configuration:
- Database: `users`
- Host: `localhost:3306`
- Username: `root`
- Password: `` (empty)

If your MySQL setup is different, update `food-seer-backend/src/main/resources/application.properties`

### 2. Build and Run Backend

```bash
cd food-seer-backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Verify Backend

The application automatically creates an admin user with credentials:
- Username: `admin`
- Password: `admin123`

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

## Ollama AI Setup

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

## Using the Application

### Application Flow:

1. **Login Page** (`/`)
   - Use the demo credentials: `admin` / `admin123`
   - The login page has a clean, modern design

2. **Preferences Page** (`/preferences`)
   - After login, you'll be directed to set your preferences
   - **Step 1**: Select your budget (Under $10, Under $20, Under $30, or custom)
   - **Step 2**: Select dietary restrictions (Vegan, Vegetarian, Lactose intolerant, or custom)
   - Click "Next" to proceed to the next step
   - On the final step, your preferences are saved to the backend

3. **Recommendations Page** (`/recommendations`)
   - View personalized recommendations based on your preferences
   - See your saved budget and dietary restrictions
   - Update preferences anytime by clicking "Update Preferences"
   - Logout when done

4. **AI Assistant** (`/chatbot`) - 🤖
   - Login as a **customer** (not admin/staff)
   - Click "🤖 AI Assistant" in the navigation
   - Answer 3 questions about your day:
     - **Question 1:** How are you feeling today? (e.g., "tired")
     - **Question 2:** How hungry are you? (e.g., "very hungry")
     - **Question 3:** What kind of food are you in the mood for? (e.g., "comfort food")
   - The AI analyzes your responses along with your budget and dietary restrictions
   - Recommends ONE perfect food item from the available menu
   - Click "Order This Now!" to create an order immediately
   - Click "Get Another Suggestion" to start over

## How the AI Chatbot Works

1. **User Input Collection:** The chatbot asks 3 questions to understand your current state
2. **Preference Integration:** It fetches your saved budget and dietary restrictions from your profile
3. **Food Filtering:** Foods are filtered based on:
   - Your budget (budget/moderate/premium/no-limit)
   - Your dietary restrictions (vegan/vegetarian/gluten-free)
4. **AI Analysis:** The filtered food list + your responses are sent to Ollama's gemma3:1b model
5. **Recommendation:** The AI selects the best food match and explains why it's perfect for you
6. **Quick Order:** You can order directly from the recommendation

## API Endpoints

The backend provides the following key endpoints:

### Authentication
- `POST /auth/login` - Login with username and password
- `POST /auth/register` - Register a new user

### User Management
- `GET /api/users/me` - Get current user info (requires authentication)
- `PUT /api/users/me/preferences` - Update user preferences (requires authentication)

### Chat
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

## Database Schema

The `User` entity includes:
- `id` - Primary key
- `username` - Unique username
- `email` - Unique email
- `password` - Encrypted password
- `role` - User role (ROLE_STANDARD, ROLE_ADMIN)
- `costPreference` - User's budget preference
- `dietaryRestrictions` - User's dietary restrictions

## Architecture

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

## Files Implemented

### Backend:
- ✅ `food-seer-backend/src/main/java/FoodSeer/dto/ChatRequestDto.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/dto/ChatResponseDto.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/service/ChatService.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/service/impl/ChatServiceImpl.java` (new)
- ✅ `food-seer-backend/src/main/java/FoodSeer/controller/ChatController.java` (new)

### Frontend:
- ✅ `food-seer-frontend/src/pages/Chatbot.js` (new)
- ✅ `food-seer-frontend/src/services/api.js` (modified - added `sendChatMessage`)
- ✅ `food-seer-frontend/src/App.js` (modified - added `/chatbot` route)
- ✅ `food-seer-frontend/src/components/Navigation.js` (modified - added AI Assistant link)
- ✅ `food-seer-frontend/src/index.css` (modified - added chatbot styles)

## Troubleshooting

### Backend Issues

1. **Port 8080 already in use:**
   ```bash
   # Find and kill the process using port 8080
   lsof -ti:8080 | xargs kill -9
   ```

2. **MySQL connection failed:**
   - Verify MySQL is running: `mysql.server start` (macOS) or `sudo service mysql start` (Linux)
   - Check credentials in `application.properties`

3. **Admin user not created:**
   - The admin user is created automatically on startup
   - Check the console logs for any errors

4. **Backend 500 Error:**
   - Ensure Ollama is running: `curl http://localhost:11434`
   - Check backend logs for connection errors

### Frontend Issues

1. **Port 3000 already in use:**
   - The terminal will prompt you to use a different port
   - Or kill the process: `lsof -ti:3000 | xargs kill -9`

2. **CORS errors:**
   - The backend is configured with `@CrossOrigin("*")`
   - Make sure the backend is running on port 8080

3. **Login fails:**
   - Check that the backend is running
   - Verify the API_BASE_URL in `src/services/api.js` is correct
   - Check browser console for error messages

### Ollama Issues

1. **"Failed to send message to AI":**
   - **Cause:** Ollama is not running
   - **Fix:** Run `ollama serve` in a terminal

2. **"No response from AI":**
   - **Cause:** Model not downloaded
   - **Fix:** Run `ollama pull gemma3:1b`

3. **"Error communicating with Ollama":**
   - **Cause:** Ollama is running on a different port
   - **Fix:** Check Ollama is on port 11434 (default)

## Future Enhancements

### Features to Implement:
1. 💾 **Conversation History:** Save past recommendations
2. ⭐ **Rating System:** Let users rate recommendations to improve accuracy
3. 🧠 **Learning:** Use past orders to personalize recommendations
4. 📊 **Analytics:** Track which recommendations users accept/decline
5. 🎨 **Custom Prompts:** Allow users to customize the AI's personality
6. Connect to actual restaurant recommendation API
7. Add more sophisticated preference options
8. Implement user registration flow
9. Add favorite restaurants functionality
10. Include location-based recommendations

## Development Notes

- All frontend code is in `food-seer-frontend/src/`
- All backend code is in `food-seer-backend/src/main/java/FoodSeer/`
- The frontend uses JWT tokens stored in localStorage for authentication
- Tokens expire after 7 days
- All API calls include the Bearer token in the Authorization header
- Protected routes redirect to login if not authenticated
- Backend communicates with Ollama running on `localhost:11434`
- Make sure to commit your changes regularly
- Push to the `frontend-recreation` branch

## Merging to Dev

When ready to merge back to dev:
```bash
git add .
git commit -m "Recreated frontend with login, preferences, recommendations, and AI chatbot"
git push origin frontend-recreation
# Then create a pull request on GitHub
```

## Quick Start Commands

```bash
# Terminal 1 - Start MySQL (if not running)
mysql.server start  # macOS
# or
sudo service mysql start  # Linux

# Terminal 2 - Start Backend
cd food-seer-backend
mvn spring-boot:run

# Terminal 3 - Start Frontend
cd food-seer-frontend
npm start

# Terminal 4 - Start Ollama (if not auto-started)
ollama serve

# Test everything is working
curl http://localhost:8080/auth/login
curl http://localhost:11434
open http://localhost:3000
```

## Ready to Use!

Once everything is set up:
1. Navigate to `http://localhost:3000`
2. Login with `admin` / `admin123`
3. Set your preferences
4. View recommendations
5. Try the AI Assistant for personalized food suggestions!

Enjoy your AI-powered food recommendations! 🍕🤖