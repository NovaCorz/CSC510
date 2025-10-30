import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getCurrentUser, logout, getAllFoods } from '../services/api';

const Recommendations = () => {
  const [user, setUser] = useState(null);
  const [foods, setFoods] = useState([]);
  const [filteredFoods, setFilteredFoods] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userData = await getCurrentUser();
        setUser(userData);
        
        const foodsData = await getAllFoods();
        setFoods(foodsData);
        
        // Filter foods based on user preferences
        filterFoodsByPreferences(foodsData, userData);
      } catch (error) {
        console.error('Error fetching data:', error);
        navigate('/');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [navigate]);

  const filterFoodsByPreferences = (foodsData, userData) => {
    let filtered = [...foodsData];

    // Filter by budget/cost preference
    if (userData.costPreference) {
      const budget = userData.costPreference.toLowerCase();
      
      if (budget.includes('budget') || budget.includes('cheap') || budget.includes('low')) {
        // Filter for foods under 10
        filtered = filtered.filter(food => food.price < 10);
      } else if (budget.includes('moderate') || budget.includes('medium') || budget.includes('mid')) {
        // Filter for foods between 10 and 20
        filtered = filtered.filter(food => food.price >= 10 && food.price <= 20);
      } else if (budget.includes('high') || budget.includes('premium') || budget.includes('expensive')) {
        // Filter for foods over 20
        filtered = filtered.filter(food => food.price > 20);
      } else {
        // Try to parse as a number
        const maxBudget = parseFloat(userData.costPreference);
        if (!isNaN(maxBudget)) {
          filtered = filtered.filter(food => food.price <= maxBudget);
        }
      }
    }

    // Filter by dietary restrictions (allergies)
    if (userData.dietaryRestrictions) {
      const restrictionsInput = userData.dietaryRestrictions.toLowerCase();
      const restrictions = restrictionsInput.split(',').map(r => r.trim());
      
      // Expand dietary preference keywords into specific allergen exclusions
      const expandedRestrictions = new Set();
      
      restrictions.forEach(restriction => {
        // Add the original restriction
        expandedRestrictions.add(restriction);
        
        // Expand special dietary preferences
        if (restriction.includes('vegan')) {
          // Vegans avoid all animal products
          expandedRestrictions.add('dairy');
          expandedRestrictions.add('lactose');
          expandedRestrictions.add('eggs');
          expandedRestrictions.add('meat');
          expandedRestrictions.add('beef');
          expandedRestrictions.add('pork');
          expandedRestrictions.add('poultry');
          expandedRestrictions.add('chicken');
          expandedRestrictions.add('fish');
          expandedRestrictions.add('shellfish');
          expandedRestrictions.add('honey');
          expandedRestrictions.add('gelatin');
        } else if (restriction.includes('vegetarian')) {
          // Vegetarians avoid meat and fish but can have dairy/eggs
          expandedRestrictions.add('meat');
          expandedRestrictions.add('beef');
          expandedRestrictions.add('pork');
          expandedRestrictions.add('poultry');
          expandedRestrictions.add('chicken');
          expandedRestrictions.add('fish');
          expandedRestrictions.add('shellfish');
          expandedRestrictions.add('gelatin');
        } else if (restriction.includes('lactose') || restriction.includes('dairy free')) {
          expandedRestrictions.add('dairy');
          expandedRestrictions.add('lactose');
        } else if (restriction.includes('gluten free') || restriction.includes('celiac')) {
          expandedRestrictions.add('gluten');
          expandedRestrictions.add('wheat');
          expandedRestrictions.add('barley');
          expandedRestrictions.add('rye');
        } else if (restriction.includes('nut')) {
          expandedRestrictions.add('nuts');
          expandedRestrictions.add('peanuts');
          expandedRestrictions.add('tree nuts');
          expandedRestrictions.add('almonds');
        } else if (restriction.includes('pescatarian')) {
          // Pescatarians avoid meat/poultry but eat fish
          expandedRestrictions.add('meat');
          expandedRestrictions.add('beef');
          expandedRestrictions.add('pork');
          expandedRestrictions.add('poultry');
          expandedRestrictions.add('chicken');
        }
      });
      
      filtered = filtered.filter(food => {
        if (!food.allergies || food.allergies.length === 0) {
          return true; // No allergies means safe for all
        }
        
        // Check if any of the food's allergies match user's restrictions
        const foodAllergies = food.allergies.map(a => a.toLowerCase());
        return !Array.from(expandedRestrictions).some(restriction => 
          foodAllergies.some(allergy => 
            allergy.includes(restriction) || restriction.includes(allergy)
          )
        );
      });
    }

    setFilteredFoods(filtered);
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const handleUpdatePreferences = () => {
    navigate('/preferences');
  };

  const handleBrowseInventory = () => {
    navigate('/inventory');
  };

  const handleViewOrders = () => {
    navigate('/orders');
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
        <div className="header-actions">
          <button className="nav-button" onClick={handleBrowseInventory}>
            Browse All Foods
          </button>
          <button className="nav-button" onClick={handleViewOrders}>
            My Orders
          </button>
          <button className="logout-button" onClick={handleLogout}>
            Logout
          </button>
        </div>
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
        {filteredFoods.length === 0 ? (
          <div className="no-recommendations">
            <p>No foods match your current preferences.</p>
            <p>Try adjusting your budget or dietary restrictions, or browse all available foods.</p>
          </div>
        ) : (
          <div className="recommendations-grid">
            {filteredFoods.map((food) => (
              <div key={food.id} className="recommendation-card">
                <div className="recommendation-icon">🍽️</div>
                <h3>{food.foodName}</h3>
                <div className="food-details">
                  <p><strong>Price:</strong> ${food.price}</p>
                  <p><strong>Available:</strong> {food.amount > 0 ? `${food.amount} units` : 'Out of stock'}</p>
                  {food.allergies && food.allergies.length > 0 && (
                    <p><strong>Allergies:</strong> {food.allergies.join(', ')}</p>
                  )}
                </div>
                <div className="recommendation-match">
                  {food.amount > 0 ? '✓ Available' : '✗ Out of Stock'}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="recommendations-footer">
        <p>Recommendations are based on your cost preference ({user?.costPreference || 'not set'}) and dietary restrictions ({user?.dietaryRestrictions || 'none'}).</p>
        <p>Showing {filteredFoods.length} of {foods.length} available foods.</p>
      </div>
    </div>
  );
};

export default Recommendations;

