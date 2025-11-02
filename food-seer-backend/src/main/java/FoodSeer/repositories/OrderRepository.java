package FoodSeer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import FoodSeer.entity.Order;
import FoodSeer.entity.User;

/**
 * Repository interface for managing Order entities in the database.
 * Provides CRUD operations through Spring Data JPA.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders for a specific user.
     *
     * @param user the user
     * @return list of orders belonging to the user
     */
    List<Order> findByUser(User user);
    
    /**
     * Find all fulfilled orders for a specific user.
     *
     * @param user the user
     * @param isFulfilled true for fulfilled orders
     * @return list of fulfilled orders belonging to the user
     */
    List<Order> findByUserAndIsFulfilled(User user, boolean isFulfilled);
}
