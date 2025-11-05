# 🤖 Chatbot Setup Guide

## What's Been Implemented

### Backend (Java/Spring Boot)
- ✅ `ChatRequestDto` & `ChatResponseDto` - DTOs for chat communication
- ✅ `ChatService` & `ChatServiceImpl` - Service to communicate with Ollama API
- ✅ `ChatController` - REST endpoint at `/api/chat`
- ✅ Communicates with Ollama running on `localhost:11434`

### Frontend (React)
- ✅ `Chatbot.js` - Interactive chat component
- ✅ Asks 3 questions about the user's day (mood, hunger, preference)
- ✅ Filters foods based on user's budget and dietary restrictions
- ✅ Uses AI to recommend ONE perfect food item
- ✅ Direct "Order Now" button to create order immediately
- ✅ Beautiful chat UI with typing indicators and animations
- ✅ Added to navigation as "🤖 AI Assistant"



## How It Works

1. **User Input Collection:** The chatbot asks 3 questions to understand your current state
2. **Preference Integration:** It fetches your saved budget and dietary restrictions from your profile
3. **Food Filtering:** Foods are filtered based on:
   - Your budget (budget/moderate/premium/no-limit)
   - Your dietary restrictions (vegan/vegetarian/gluten-free)
4. **AI Analysis:** The filtered food list + your responses are sent to Ollama's gemma3:1b model
5. **Recommendation:** The AI selects the best food match and explains why it's perfect for you
6. **Quick Order:** You can order directly from the recommendation

## Troubleshooting

### "Failed to send message to AI"
- **Cause:** Ollama is not running
- **Fix:** Run `ollama serve` in a terminal

### "No response from AI"
- **Cause:** Model not downloaded
- **Fix:** Run `ollama pull gemma3:1b`

### "Error communicating with Ollama"
- **Cause:** Ollama is running on a different port
- **Fix:** Check Ollama is on port 11434 (default)

### Backend 500 Error
- **Cause:** Backend can't reach Ollama
- **Fix:** Ensure Ollama is running: `curl http://localhost:11434`

## Future Enhancements

Potential improvements:
- 💾 **Conversation History:** Save past recommendations
- ⭐ **Rating System:** Let users rate recommendations to improve accuracy
- 🧠 **Learning:** Use past orders to personalize recommendations
- 📊 **Analytics:** Track which recommendations users accept/decline
- 🎨 **Custom Prompts:** Allow users to customize the AI's personality

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

## Using the Application

### Flow:

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

## API Endpoints

The backend provides the following key endpoints:

### Authentication
- `POST /auth/login` - Login with username and password
- `POST /auth/register` - Register a new user

### User Management
- `GET /api/users/me` - Get current user info (requires authentication)
- `PUT /api/users/me/preferences` - Update user preferences (requires authentication)

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

## Database Schema

The `User` entity now includes:
- `id` - Primary key
- `username` - Unique username
- `email` - Unique email
- `password` - Encrypted password
- `role` - User role (ROLE_STANDARD, ROLE_ADMIN)
- `costPreference` - User's budget preference (NEW)
- `dietaryRestrictions` - User's dietary restrictions (NEW)

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
