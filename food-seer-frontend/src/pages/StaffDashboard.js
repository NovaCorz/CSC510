import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getUnfulfilledOrders, getFulfilledOrders, fulfillOrder, getCurrentUser } from '../services/api';

const StaffDashboard = () => {
  const [unfulfilledOrders, setUnfulfilledOrders] = useState([]);
  const [fulfilledOrders, setFulfilledOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [view, setView] = useState('unfulfilled'); // unfulfilled or fulfilled
  const [processing, setProcessing] = useState({});
  const navigate = useNavigate();

  const fetchOrders = async () => {
    try {
      const user = await getCurrentUser();
      
      // Check if user has staff or admin role
      if (user.role !== 'ROLE_ADMIN' && user.role !== 'ROLE_STAFF') {
        alert('Access denied. Staff privileges required.');
        navigate('/recommendations');
        return;
      }

      const unfulfilled = await getUnfulfilledOrders();
      const fulfilled = await getFulfilledOrders();
      setUnfulfilledOrders(unfulfilled);
      setFulfilledOrders(fulfilled);
    } catch (error) {
      console.error('Error fetching orders:', error);
      navigate('/');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleFulfillOrder = async (orderId) => {
    if (!window.confirm('Mark this order as fulfilled?')) {
      return;
    }

    setProcessing(prev => ({ ...prev, [orderId]: true }));
    
    try {
      await fulfillOrder(orderId);
      alert('Order fulfilled successfully!');
      // Refresh orders
      await fetchOrders();
    } catch (error) {
      console.error('Error fulfilling order:', error);
      alert('Failed to fulfill order. Please try again.');
    } finally {
      setProcessing(prev => ({ ...prev, [orderId]: false }));
    }
  };

  const getTotalPrice = (order) => {
    return order.foods.reduce((total, food) => total + food.price, 0);
  };

  const handleBack = () => {
    navigate('/recommendations');
  };

  if (loading) {
    return (
      <div className="staff-dashboard-container">
        <div className="loading">Loading dashboard...</div>
      </div>
    );
  }

  const displayOrders = view === 'unfulfilled' ? unfulfilledOrders : fulfilledOrders;

  return (
    <div className="staff-dashboard-container">
      <div className="dashboard-header">
        <h1>👨‍🍳 Staff Dashboard - Order Management</h1>
        <button className="back-button" onClick={handleBack}>
          Back
        </button>
      </div>

      <div className="dashboard-stats">
        <div className="stat-card">
          <h3>Pending Orders</h3>
          <p className="stat-number">{unfulfilledOrders.length}</p>
        </div>
        <div className="stat-card">
          <h3>Fulfilled Today</h3>
          <p className="stat-number">{fulfilledOrders.length}</p>
        </div>
        <div className="stat-card">
          <h3>Total Orders</h3>
          <p className="stat-number">{unfulfilledOrders.length + fulfilledOrders.length}</p>
        </div>
      </div>

      <div className="view-toggle">
        <button
          className={`toggle-button ${view === 'unfulfilled' ? 'active' : ''}`}
          onClick={() => setView('unfulfilled')}
        >
          Pending Orders ({unfulfilledOrders.length})
        </button>
        <button
          className={`toggle-button ${view === 'fulfilled' ? 'active' : ''}`}
          onClick={() => setView('fulfilled')}
        >
          Fulfilled Orders ({fulfilledOrders.length})
        </button>
      </div>

      {displayOrders.length === 0 ? (
        <div className="no-orders">
          <p>No {view} orders at this time.</p>
        </div>
      ) : (
        <div className="orders-list">
          {displayOrders.map(order => (
            <div key={order.id} className={`order-card ${view === 'unfulfilled' ? 'pending' : 'completed'}`}>
              <div className="order-header">
                <div className="order-title">
                  <h3>{order.name || `Order #${order.id}`}</h3>
                  <span className="order-id">ID: #{order.id}</span>
                </div>
                {view === 'unfulfilled' ? (
                  <button
                    className="fulfill-button"
                    onClick={() => handleFulfillOrder(order.id)}
                    disabled={processing[order.id]}
                  >
                    {processing[order.id] ? 'Processing...' : '✓ Fulfill Order'}
                  </button>
                ) : (
                  <span className="fulfilled-badge">✓ Fulfilled</span>
                )}
              </div>

              <div className="order-summary">
                <div className="summary-item">
                  <span className="label">Total Items:</span>
                  <span className="value">{order.foods.length}</span>
                </div>
                <div className="summary-item">
                  <span className="label">Total Price:</span>
                  <span className="value">${getTotalPrice(order)}</span>
                </div>
              </div>

              <div className="order-items-detail">
                <h4>Order Items:</h4>
                <table className="items-table">
                  <thead>
                    <tr>
                      <th>Item</th>
                      <th>Price</th>
                      <th>Allergies</th>
                    </tr>
                  </thead>
                  <tbody>
                    {order.foods.map((food, index) => (
                      <tr key={`${food.id}-${index}`}>
                        <td>{food.foodName}</td>
                        <td>${food.price}</td>
                        <td>{food.allergies && food.allergies.length > 0 
                          ? food.allergies.join(', ') 
                          : 'None'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default StaffDashboard;

