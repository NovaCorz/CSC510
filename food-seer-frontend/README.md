# FoodSeer Frontend

A modern React-based frontend for the FoodSeer food recommendation application.

## Features

- **User Authentication**: Secure login with JWT tokens
- **Preferences Management**: Collect user budget and dietary restrictions
- **Personalized Recommendations**: Display food recommendations based on user preferences
- **Responsive Design**: Works on desktop and mobile devices

## Project Structure

```
src/
├── components/          # Reusable React components
│   ├── BudgetQuestion.js
│   └── DietaryRestrictions.js
├── pages/              # Page components
│   ├── Login.js
│   ├── Preferences.js
│   └── Recommendations.js
├── services/           # API service layer
│   └── api.js
├── App.js             # Main app with routing
├── index.js           # Entry point
└── index.css          # Global styles
```

## Routes

- `/` - Login page
- `/preferences` - User preferences (protected)
- `/recommendations` - Recommendations page (protected)

## Development

### Prerequisites
- Node.js 16+
- npm or yarn

### Installation
```bash
npm install
```

### Run Development Server
```bash
npm start
```

The app will run on `http://localhost:3000`

### Build for Production
```bash
npm run build
```

## API Integration

The frontend communicates with the Spring Boot backend at `http://localhost:8080`

### Authentication Flow
1. User logs in with username/password
2. Backend returns JWT token
3. Token is stored in localStorage
4. Token is included in all subsequent API requests via Authorization header

### API Endpoints Used
- `POST /auth/login` - User login
- `GET /api/users/me` - Get current user info
- `PUT /api/users/me/preferences` - Update user preferences

## Styling

The app uses vanilla CSS with a modern, clean design featuring:
- Gradient backgrounds
- Card-based layouts
- Smooth transitions and hover effects
- Responsive grid system

## Protected Routes

Routes are protected using the `ProtectedRoute` component which checks for JWT token presence. Unauthenticated users are redirected to the login page.

## State Management

- Authentication state: Managed via localStorage
- User preferences: Managed locally and synced with backend
- User data: Fetched on-demand from backend

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

Part of the FoodSeer project for CSC510
