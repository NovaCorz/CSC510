import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Preferences from './pages/Preferences';
import Recommendations from './pages/Recommendations';
import Inventory from './pages/Inventory';
import CreateOrder from './pages/CreateOrder';
import Orders from './pages/Orders';
import OrderManagement from './pages/OrderManagement';
import InventoryManagement from './pages/InventoryManagement';
import UserManagement from './pages/UserManagement';
import Navigation from './components/Navigation';
import { isAuthenticated } from './services/api';

// Protected Route component
const ProtectedRoute = ({ children }) => {
  return isAuthenticated() ? children : <Navigate to="/" />;
};

// Layout component that includes navigation for authenticated pages
const AppLayout = ({ children }) => {
  return (
    <div className="app-layout">
      <Navigation />
      <div className="app-content">
        {children}
      </div>
    </div>
  );
};

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<Login />} />
          <Route path="/register" element={<Register />} />
          
          {/* Protected routes with navigation */}
          <Route 
            path="/preferences" 
            element={
              <ProtectedRoute>
                <Preferences />
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/recommendations" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <Recommendations />
                </AppLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/inventory" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <Inventory />
                </AppLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/create-order" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <CreateOrder />
                </AppLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/orders" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <Orders />
                </AppLayout>
              </ProtectedRoute>
            } 
          />
          
          {/* Staff & Admin routes */}
          <Route 
            path="/order-management" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <OrderManagement />
                </AppLayout>
              </ProtectedRoute>
            } 
          />
          <Route 
            path="/inventory-management" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <InventoryManagement />
                </AppLayout>
              </ProtectedRoute>
            } 
          />
          
          {/* Admin-only routes */}
          <Route 
            path="/users" 
            element={
              <ProtectedRoute>
                <AppLayout>
                  <UserManagement />
                </AppLayout>
              </ProtectedRoute>
            } 
          />

          {/* Catch all - redirect to login */}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
