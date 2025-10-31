import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { getMyOrders, getCurrentUser } from '../services/api';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all'); // all, fulfilled, unfulfilled
  const navigate = useNavigate();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        await getCurrentUser(); // Verify authentication
        const ordersData = await getMyOrders(); // Get only current user's orders
        setOrders(ordersData);
      } catch (error) {
        console.error('Error fetching orders:', error);
        navigate('/');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, [navigate]);

  const handleBack = () => {
    navigate('/recommendations');
  };

  const handleCreateOrder = () => {
    navigate('/create-order');
  };

  const getFilteredOrders = () => {
    switch (filter) {
      case 'fulfilled':
        return orders.filter(order => order.isFulfilled);
      case 'unfulfilled':
        return orders.filter(order => !order.isFulfilled);
      default:
        return orders;
    }
  };

  const getTotalPrice = (order) => {
    return order.foods.reduce((total, food) => total + food.price, 0);
  };

  if (loading) {
    return (
      <div className="orders-container">
        <div className="loading">Loading orders...</div>
      </div>
    );
  }

  const filteredOrders = getFilteredOrders();

  return (
    <div className="orders-container">
      <div className="orders-header">
        <h1>📦 My Orders</h1>
        <div className="header-actions">
          <button className="create-button" onClick={handleCreateOrder}>
            Create New Order
          </button>
          <button className="back-button" onClick={handleBack}>
            Back
          </button>
        </div>
      </div>

      <div className="orders-filters">
        <button
          className={`filter-button ${filter === 'all' ? 'active' : ''}`}
          onClick={() => setFilter('all')}
        >
          All Orders ({orders.length})
        </button>
        <button
          className={`filter-button ${filter === 'unfulfilled' ? 'active' : ''}`}
          onClick={() => setFilter('unfulfilled')}
        >
          Pending ({orders.filter(o => !o.isFulfilled).length})
        </button>
        <button
          className={`filter-button ${filter === 'fulfilled' ? 'active' : ''}`}
          onClick={() => setFilter('fulfilled')}
        >
          Fulfilled ({orders.filter(o => o.isFulfilled).length})
        </button>
      </div>

      {filteredOrders.length === 0 ? (
        <div className="no-orders">
          <p>No orders found.</p>
          <button className="create-button" onClick={handleCreateOrder}>
            Create Your First Order
          </button>
        </div>
      ) : (
        <div className="orders-list">
          {filteredOrders.map(order => (
            <div key={order.id} className="order-card">
              <div className="order-header">
                <h3>{order.name || `Order #${order.id}`}</h3>
                <span className={`status-badge ${order.isFulfilled ? 'fulfilled' : 'pending'}`}>
                  {order.isFulfilled ? '✓ Fulfilled' : '⏳ Pending'}
                </span>
              </div>
              
              <div className="order-details">
                <div className="order-info">
                  <p><strong>Order ID:</strong> #{order.id}</p>
                  <p><strong>Total Items:</strong> {order.foods.length}</p>
                  <p><strong>Total Price:</strong> ${getTotalPrice(order)}</p>
                </div>

                <div className="order-items">
                  <h4>Items:</h4>
                  <ul>
                    {order.foods.map((food, index) => (
                      <li key={`${food.id}-${index}`}>
                        {food.foodName} - ${food.price}
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Orders;

