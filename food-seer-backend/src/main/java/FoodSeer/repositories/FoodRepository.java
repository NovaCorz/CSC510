package FoodSeer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import FoodSeer.entity.Food;

/**
 * Food Repository
 */
public interface FoodRepository extends JpaRepository<Food, Long> {

}
