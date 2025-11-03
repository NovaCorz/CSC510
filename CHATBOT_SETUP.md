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

## How to Set Up Ollama

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

## How to Test the Chatbot Integration

### 1. Start the Backend

```bash
cd food-seer-backend
mvn spring-boot:run
```

### 2. Start the Frontend

```bash
cd food-seer-frontend
npm start
```

### 3. Test the Chatbot

1. Login as a **customer** (not admin/staff)
2. Click "🤖 AI Assistant" in the navigation
3. Answer the 3 questions:
   - **Question 1:** How are you feeling today? (e.g., "tired")
   - **Question 2:** How hungry are you? (e.g., "very hungry")
   - **Question 3:** What kind of food are you in the mood for? (e.g., "comfort food")
4. The AI will analyze your responses along with your budget and dietary restrictions
5. It will recommend ONE food item from the available menu
6. Click "Order This Now!" to create an order immediately
7. Click "Get Another Suggestion" to start over

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

## Files Changed/Created

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

## Ready to Test!

Once Ollama is installed and running, you can:
1. Start both backend and frontend
2. Login as a customer
3. Click "🤖 AI Assistant"
4. Have a conversation and get personalized food recommendations!

Enjoy your AI-powered food recommendations! 🍕🤖

