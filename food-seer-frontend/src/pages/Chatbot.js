import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { sendChatMessage, getCurrentUser, getAllFoods } from '../services/api';

const Chatbot = () => {
  const navigate = useNavigate();
  const messagesEndRef = useRef(null);

  const QUESTIONS = [
    "Hi! I'm your FoodSeer assistant. How are you feeling today? (e.g., tired, energetic, stressed, happy)",
    "How hungry are you right now? (e.g., very hungry, a bit peckish, just want a snack)",
    "What kind of food are you in the mood for? (e.g., something light, comfort food, healthy, sweet)"
  ];

  // Load state from localStorage or use defaults
  const loadState = () => {
    try {
      const saved = localStorage.getItem('chatbotState');
      if (saved) {
        const parsed = JSON.parse(saved);
        return {
          messages: parsed.messages || [],
          conversationStep: parsed.conversationStep || 0,
          userResponses: parsed.userResponses || { mood: '', hunger: '', preference: '' },
          recommendedFood: parsed.recommendedFood || null
        };
      }
    } catch (error) {
      console.error('Error loading chatbot state:', error);
    }
    return null;
  };

  const savedState = loadState();
  const [messages, setMessages] = useState(savedState?.messages || []);
  const [inputMessage, setInputMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [conversationStep, setConversationStep] = useState(savedState?.conversationStep || 0);
  const [userResponses, setUserResponses] = useState(savedState?.userResponses || {
    mood: '',
    hunger: '',
    preference: ''
  });
  const [recommendedFood, setRecommendedFood] = useState(savedState?.recommendedFood || null);

  // Save state to localStorage whenever it changes
  useEffect(() => {
    const state = {
      messages,
      conversationStep,
      userResponses,
      recommendedFood
    };
    localStorage.setItem('chatbotState', JSON.stringify(state));
  }, [messages, conversationStep, userResponses, recommendedFood]);

  useEffect(() => {
    // Start with the first question if no saved state
    if (messages.length === 0) {
      setMessages([{
        role: 'assistant',
        content: QUESTIONS[0]
      }]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    // Scroll to bottom when messages change
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const getPersonalizedPrompt = (mood, hunger, preference, userData, foods) => {
    const budget = userData?.costPreference || 'moderate';
    const dietaryRestrictions = userData?.dietaryRestrictions || '';
    
    // Convert dietary restrictions to array if it's a string
    const allergies = Array.isArray(dietaryRestrictions) 
      ? dietaryRestrictions 
      : (dietaryRestrictions ? [dietaryRestrictions] : []);
    
    // Filter foods based on budget and allergies
    const availableFoods = foods.filter(food => {
      // Budget filtering
      if (budget === 'budget' && food.price > 10) return false;
      if (budget === 'moderate' && food.price > 20) return false;
      if (budget === 'premium' && food.price > 35) return false;
      
      // Allergy filtering
      if (allergies.includes('vegan') && 
          food.allergies.some(a => ['dairy', 'eggs', 'meat', 'fish', 'shellfish', 'beef', 'chicken', 'pork'].includes(a.toLowerCase()))) {
        return false;
      }
      if (allergies.includes('vegetarian') && 
          food.allergies.some(a => ['meat', 'fish', 'shellfish', 'beef', 'chicken', 'pork'].includes(a.toLowerCase()))) {
        return false;
      }
      if (allergies.includes('gluten-free') && 
          food.allergies.some(a => a.toLowerCase().includes('gluten') || a.toLowerCase().includes('wheat'))) {
        return false;
      }
      
      return true;
    });

    const foodList = availableFoods.map(f => `${f.foodName} ($${f.price})`).join(', ');
    const allergiesText = allergies.length > 0 ? allergies.join(', ') : 'none';
    
    return `You are a helpful food recommendation assistant. Based on the following information, recommend ONE specific food item from the available menu.

User's mood: ${mood}
User's hunger level: ${hunger}
User's preference: ${preference}
User's budget: ${budget}
User's dietary restrictions: ${allergiesText}

Available foods that match their budget and dietary restrictions: ${foodList}

Please recommend exactly ONE food item from the available list that best matches their mood, hunger level, and preferences. 
Explain in 2-3 sentences why this food is perfect for them right now. Be conversational and friendly.
Format your response as: "I recommend [FOOD NAME]! [Explanation]"`;
  };

  const handleSendMessage = async () => {
    if (!inputMessage.trim()) return;

    const userMessage = {
      role: 'user',
      content: inputMessage
    };

    setMessages(prev => [...prev, userMessage]);
    setInputMessage('');
    setIsLoading(true);

    try {
      // Store user responses
      const responses = { ...userResponses };
      if (conversationStep === 0) responses.mood = inputMessage;
      if (conversationStep === 1) responses.hunger = inputMessage;
      if (conversationStep === 2) responses.preference = inputMessage;
      setUserResponses(responses);

      // If we've asked all questions, get food recommendation
      if (conversationStep === 2) {
        // Get user data and foods for personalized recommendation
        const userData = await getCurrentUser();
        const foods = await getAllFoods();
        
        const personalizedPrompt = getPersonalizedPrompt(
          responses.mood,
          responses.hunger,
          inputMessage, // current preference
          userData,
          foods
        );

        // Send to AI for recommendation
        const aiResponse = await sendChatMessage(personalizedPrompt);

        // Find the full food object
        const matchedFood = foods.find(f => 
          aiResponse.message.toLowerCase().includes(f.foodName.toLowerCase())
        );

        setRecommendedFood(matchedFood);

        setMessages(prev => [...prev, {
          role: 'assistant',
          content: aiResponse.message
        }]);

        // If we found a match, show order button
        if (matchedFood) {
          setMessages(prev => [...prev, {
            role: 'system',
            content: 'recommendation-card',
            food: matchedFood
          }]);
        }

      } else {
        // Move to next question
        const nextStep = conversationStep + 1;
        setConversationStep(nextStep);
        
        setMessages(prev => [...prev, {
          role: 'assistant',
          content: QUESTIONS[nextStep]
        }]);
      }

    } catch (error) {
      console.error('Error sending message:', error);
      setMessages(prev => [...prev, {
        role: 'assistant',
        content: 'Sorry, I encountered an error. Please make sure Ollama is running and try again.'
      }]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const handleOrderFood = () => {
    if (recommendedFood) {
      // Navigate to create order page with the recommended food
      // The CreateOrder page will add it to the cart automatically
      navigate('/create-order', { state: { addToCart: recommendedFood } });
    }
  };

  const handleStartOver = () => {
    const newState = {
      messages: [{
        role: 'assistant',
        content: QUESTIONS[0]
      }],
      conversationStep: 0,
      userResponses: { mood: '', hunger: '', preference: '' },
      recommendedFood: null
    };
    
    setMessages(newState.messages);
    setConversationStep(newState.conversationStep);
    setUserResponses(newState.userResponses);
    setRecommendedFood(newState.recommendedFood);
    
    // Save the reset state to localStorage
    localStorage.setItem('chatbotState', JSON.stringify(newState));
  };

  return (
    <div className="chatbot-container">
      <div className="chatbot-header">
        <h2>🤖 FoodSeer AI Assistant</h2>
        <p>Let me help you find the perfect meal for your day!</p>
      </div>

      <div className="chatbot-messages">
        {messages.map((msg, index) => (
          <div key={index} className={`message ${msg.role}`}>
            {msg.role === 'system' && msg.content === 'recommendation-card' ? (
              <div className="recommendation-card">
                <h3>🎯 Your Personalized Recommendation</h3>
                <div className="food-card">
                  <h4>{msg.food.foodName}</h4>
                  <p className="food-price">${msg.food.price.toFixed(2)}</p>
                  <p className="food-allergies">
                    {msg.food.allergies && msg.food.allergies.length > 0 ? (
                      <>Contains: {msg.food.allergies.join(', ')}</>
                    ) : (
                      'No common allergens'
                    )}
                  </p>
                  <div className="recommendation-actions">
                    <button onClick={handleOrderFood} className="btn-primary">
                      Order This Now!
                    </button>
                    <button onClick={handleStartOver} className="btn-secondary">
                      Get Another Suggestion
                    </button>
                  </div>
                </div>
              </div>
            ) : (
              <>
                <div className="message-avatar">
                  {msg.role === 'user' ? '👤' : '🤖'}
                </div>
                <div className="message-content">
                  {msg.content}
                </div>
              </>
            )}
          </div>
        ))}
        {isLoading && (
          <div className="message assistant">
            <div className="message-avatar">🤖</div>
            <div className="message-content typing">
              <span></span><span></span><span></span>
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      <div className="chatbot-input">
        <input
          type="text"
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Type your answer here..."
          disabled={isLoading || conversationStep > 2}
        />
        <button 
          onClick={handleSendMessage} 
          disabled={isLoading || !inputMessage.trim() || conversationStep > 2}
          className="btn-send"
        >
          Send
        </button>
      </div>

      <div className="chatbot-footer">
        <button onClick={() => navigate('/recommendations')} className="btn-link">
          Skip to Browse All Foods
        </button>
      </div>
    </div>
  );
};

export default Chatbot;

