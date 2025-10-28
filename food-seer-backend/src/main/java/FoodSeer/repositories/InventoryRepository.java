package FoodSeer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import FoodSeer.entity.Inventory;

/**
 * InventoryRepository for working with the database
 * through Spring Data JPA.
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
