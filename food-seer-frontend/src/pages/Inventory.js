import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllFoods, getCurrentUser } from '../services/api';

const Inventory = () => {
  const [foods, setFoods] = useState([]);
  const [filteredFoods, setFilteredFoods] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [sortBy, setSortBy] = useState('name'); // name, price, amount
  const [filterInStock, setFilterInStock] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchFoods = async () => {
      try {
        await getCurrentUser(); // Verify authentication
        const foodsData = await getAllFoods();
        setFoods(foodsData);
        setFilteredFoods(foodsData);
      } catch (error) {
        console.error('Error fetching foods:', error);
        navigate('/');
      } finally {
        setLoading(false);
      }
    };

    fetchFoods();
  }, [navigate]);

  useEffect(() => {
    let result = [...foods];

    // Apply search filter
    if (searchTerm) {
      result = result.filter(food =>
        food.foodName.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    // Apply in-stock filter
    if (filterInStock) {
      result = result.filter(food => food.amount > 0);
    }

    // Apply sorting
    result.sort((a, b) => {
      switch (sortBy) {
        case 'name':
          return a.foodName.localeCompare(b.foodName);
        case 'price':
          return a.price - b.price;
        case 'amount':
          return b.amount - a.amount;
        default:
          return 0;
      }
    });

    setFilteredFoods(result);
  }, [foods, searchTerm, sortBy, filterInStock]);

  const handleBack = () => {
    navigate('/recommendations');
  };

  const handleCreateOrder = () => {
    navigate('/create-order');
  };

  if (loading) {
    return (
      <div className="inventory-container">
        <div className="loading">Loading inventory...</div>
      </div>
    );
  }

  return (
    <div className="inventory-container">
      <div className="inventory-header">
        <h1>🏪 Food Inventory</h1>
        <div className="header-actions">
          <button className="nav-button" onClick={handleCreateOrder}>
            Create Order
          </button>
          <button className="back-button" onClick={handleBack}>
            Back to Recommendations
          </button>
        </div>
      </div>

      <div className="inventory-controls">
        <div className="search-box">
          <input
            type="text"
            placeholder="Search foods..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="search-input"
          />
        </div>

        <div className="filter-controls">
          <label className="filter-checkbox">
            <input
              type="checkbox"
              checked={filterInStock}
              onChange={(e) => setFilterInStock(e.target.checked)}
            />
            In Stock Only
          </label>

          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="sort-select"
          >
            <option value="name">Sort by Name</option>
            <option value="price">Sort by Price</option>
            <option value="amount">Sort by Stock</option>
          </select>
        </div>
      </div>

      <div className="inventory-stats">
        <p>Showing {filteredFoods.length} of {foods.length} items</p>
      </div>

      {filteredFoods.length === 0 ? (
        <div className="no-items">
          <p>No foods found matching your criteria.</p>
        </div>
      ) : (
        <div className="inventory-grid">
          {filteredFoods.map((food) => (
            <div key={food.id} className="inventory-card">
              <div className="food-header">
                <h3>{food.foodName}</h3>
                <span className={`stock-badge ${food.amount > 0 ? 'in-stock' : 'out-of-stock'}`}>
                  {food.amount > 0 ? 'In Stock' : 'Out of Stock'}
                </span>
              </div>
              <div className="food-info">
                <div className="info-row">
                  <span className="info-label">Price:</span>
                  <span className="info-value">${food.price}</span>
                </div>
                <div className="info-row">
                  <span className="info-label">Available:</span>
                  <span className="info-value">{food.amount} units</span>
                </div>
                {food.allergies && food.allergies.length > 0 && (
                  <div className="info-row">
                    <span className="info-label">Allergies:</span>
                    <span className="info-value allergies">
                      {food.allergies.join(', ')}
                    </span>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Inventory;

