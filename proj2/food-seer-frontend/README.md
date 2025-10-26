# Food Seer Frontend

A React-based frontend application for Food Seer, a personalized meal recommendation system. This frontend provides an interactive quiz interface for customers to specify their preferences and dietary requirements.

## Features

- **Budget Question Page**: Interactive quiz asking about spending preferences per meal
  - Radio button options for predefined budget ranges
  - Custom input field for special occasions
  - Clean, modern UI with dollar sign icon

- **Dietary Restrictions Page**: Multi-select interface for dietary preferences
  - Checkbox options for common dietary restrictions
  - Custom input field for specific dietary needs
  - Carrot icon representing food/dietary focus

- **Responsive Design**: Mobile-friendly interface that works across devices
- **Navigation**: Previous/Next buttons for seamless quiz flow
- **State Management**: React hooks for managing quiz data and navigation

## Project Structure

```
food-seer-frontend/
├── public/
│   └── index.html          # HTML template
├── src/
│   ├── components/
│   │   ├── BudgetQuestion.js      # Budget selection component
│   │   └── DietaryRestrictions.js # Dietary preferences component
│   ├── App.js              # Main application component
│   ├── index.js            # React entry point
│   └── index.css           # Global styles
└── package.json            # Dependencies and scripts
```

## Getting Started

### Prerequisites

- Node.js (version 14 or higher)
- npm or yarn

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd proj2/food-seer-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

## Available Scripts

- `npm start` - Runs the app in development mode
- `npm build` - Builds the app for production
- `npm test` - Launches the test runner
- `npm eject` - Ejects from Create React App (one-way operation)

## Design Features

### UI Components
- **Clean, Modern Design**: Minimalist interface with rounded corners and subtle shadows
- **Consistent Styling**: Unified color scheme with orange (#ffa500) accents
- **Interactive Elements**: Hover effects and smooth transitions
- **Accessibility**: Proper contrast ratios and keyboard navigation support

### Quiz Flow
1. **Budget Selection**: Users choose their spending preference or enter custom amount
2. **Dietary Restrictions**: Users select applicable dietary restrictions
3. **Navigation**: Easy movement between steps with Previous/Next buttons

### Responsive Design
- Mobile-first approach
- Flexible layouts that adapt to different screen sizes
- Touch-friendly interface elements

## Integration

This frontend is designed to integrate with the Food Seer Spring Boot backend API. The quiz data collected can be sent to backend endpoints for processing and meal recommendations.

## Future Enhancements

- Additional quiz questions for comprehensive preference collection
- Integration with backend API endpoints
- User authentication and profile management
- Meal recommendation display
- Feedback and rating system
- AI chatbot integration

## Technologies Used

- **React 18**: Modern React with hooks
- **CSS3**: Custom styling with flexbox and grid
- **JavaScript ES6+**: Modern JavaScript features
- **React Router**: Navigation between pages (ready for future expansion)
