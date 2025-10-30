# FoodSeer Frontend Recreation - Setup Guide

This guide will help you set up and run the FoodSeer application with the newly recreated frontend.

## Branch Information

We've created a new branch `frontend-recreation` off of `dev` to ensure safe development without losing work.

Current branch: `frontend-recreation`

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

## Next Steps

### Features to Implement:
1. Connect to actual restaurant recommendation API
2. Add more sophisticated preference options
3. Implement user registration flow
4. Add favorite restaurants functionality
5. Include location-based recommendations

### Development:
- All frontend code is in `food-seer-frontend/src/`
- All backend code is in `food-seer-backend/src/main/java/FoodSeer/`
- Make sure to commit your changes regularly
- Push to the `frontend-recreation` branch

## Merging to Dev

When ready to merge back to dev:
```bash
git add .
git commit -m "Recreated frontend with login, preferences, and recommendations"
git push origin frontend-recreation
# Then create a pull request on GitHub
```

## Notes

- The frontend uses JWT tokens stored in localStorage for authentication
- Tokens expire after 7 days
- All API calls include the Bearer token in the Authorization header
- Protected routes redirect to login if not authenticated

