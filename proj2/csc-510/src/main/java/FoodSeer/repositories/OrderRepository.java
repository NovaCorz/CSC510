package FoodSeer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import FoodSeer.entity.Order;

/**
 * Repository interface for managing Order entities in the database.
 * Provides CRUD operations through Spring Data JPA.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // You can add custom query methods here later if needed
}
