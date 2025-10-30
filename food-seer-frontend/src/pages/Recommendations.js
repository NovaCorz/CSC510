import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCurrentUser, logout } from '../services/api';

const Recommendations = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const userData = await getCurrentUser();
        setUser(userData);
      } catch (error) {
        console.error('Error fetching user data:', error);
        navigate('/');
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, [navigate]);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const handleUpdatePreferences = () => {
    navigate('/preferences');
  };

  if (loading) {
    return (
      <div className="recommendations-container">
        <div className="loading">Loading...</div>
      </div>
    );
  }

  return (
    <div className="recommendations-container">
      <div className="recommendations-header">
        <h1 className="recommendations-title">🍽️ FoodSeer Recommendations</h1>
        <button className="logout-button" onClick={handleLogout}>
          Logout
        </button>
      </div>

      <div className="user-info-card">
        <h2>Welcome, {user?.username}!</h2>
        <div className="preferences-summary">
          <div className="preference-item">
            <span className="preference-label">💰 Budget:</span>
            <span className="preference-value">
              {user?.costPreference || 'Not set'}
            </span>
          </div>
          <div className="preference-item">
            <span className="preference-label">🥗 Dietary Restrictions:</span>
            <span className="preference-value">
              {user?.dietaryRestrictions || 'None'}
            </span>
          </div>
        </div>
        <button 
          className="update-preferences-button"
          onClick={handleUpdatePreferences}
        >
          Update Preferences
        </button>
      </div>

      <div className="recommendations-content">
        <h2>Your Personalized Recommendations</h2>
        <div className="recommendations-grid">
          <div className="recommendation-card">
            <div className="recommendation-icon">🍕</div>
            <h3>Italian Cuisine</h3>
            <p>Based on your preferences, we recommend trying authentic Italian restaurants near you.</p>
            <div className="recommendation-match">85% Match</div>
          </div>
          
          <div className="recommendation-card">
            <div className="recommendation-icon">🍜</div>
            <h3>Asian Fusion</h3>
            <p>Explore delicious Asian fusion options that fit your dietary needs.</p>
            <div className="recommendation-match">78% Match</div>
          </div>
          
          <div className="recommendation-card">
            <div className="recommendation-icon">🥗</div>
            <h3>Fresh & Healthy</h3>
            <p>Fresh, healthy options perfect for your dietary restrictions.</p>
            <div className="recommendation-match">92% Match</div>
          </div>
          
          <div className="recommendation-card">
            <div className="recommendation-icon">🌮</div>
            <h3>Mexican Flavors</h3>
            <p>Spicy and flavorful Mexican dishes within your budget range.</p>
            <div className="recommendation-match">70% Match</div>
          </div>
        </div>
      </div>

      <div className="recommendations-footer">
        <p>Recommendations are based on your cost preference and dietary restrictions.</p>
        <p>More features coming soon!</p>
      </div>
    </div>
  );
};

export default Recommendations;

