package FoodSeer.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import FoodSeer.dto.InventoryDto;
import FoodSeer.entity.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Tests InventoryServiceImpl for FoodSeer.
 */
@SpringBootTest
@Transactional
public class InventoryServiceImplTest {

    /** Reference to InventoryService (and InventoryServiceImpl). */
    @Autowired
    private InventoryService inventoryService;

    /** Reference to EntityManager */
    @Autowired
    private EntityManager entityManager;

    /**
     * Sets up the test case.
     * We assume only one inventory row.
     * Because inventory is treated as a singleton (only one row),
     * we must DELETE instead of TRUNCATE to reset auto-increment properly.
     *
     * @throws Exception if error
     */
    @BeforeEach
    public void setUp() throws Exception {
        final Query query = entityManager.createNativeQuery("DELETE FROM inventory");
        query.executeUpdate();

        entityManager.createNativeQuery("ALTER TABLE inventory ALTER COLUMN id RESTART WITH 1").executeUpdate();


    }

    /**
     * Tests InventoryService.createInventory().
     */
    @Test
    @Transactional
    public void testCreateInventory() {

        // Ensure Inventory always starts with ID 1L.
        final List<Food> foods = new ArrayList<>();

        // Food constructor no longer takes ID — let Hibernate assign it
        final Food pizza = new Food("pizza", 20, 10, new ArrayList<>());
        final Food pasta = new Food("pasta", 30, 12, new ArrayList<>());
        final Food salad = new Food("salad", 40, 8, new ArrayList<>());

        foods.add(pizza);
        foods.add(pasta);
        foods.add(salad);

        final InventoryDto inventoryDto = new InventoryDto(1L, foods);

        final InventoryDto createdInventoryDto = inventoryService.createInventory(inventoryDto);

        // Verify contents of returned InventoryDto
        assertAll("InventoryDto contents",
                () -> assertEquals(1L, createdInventoryDto.getId()),
                () -> assertEquals(20, createdInventoryDto.getFoods().get(0).getAmount()),
                () -> assertEquals(30, createdInventoryDto.getFoods().get(1).getAmount()),
                () -> assertEquals(40, createdInventoryDto.getFoods().get(2).getAmount()));
    }

    /**
     * Tests InventoryService.updateInventory().
     */
    @Test
    @Transactional
    public void testUpdateInventory() {

        final List<Food> foods = new ArrayList<>();

        final Food pizza = new Food("pizza", 20, 10, new ArrayList<>());
        final Food pasta = new Food("pasta", 30, 12, new ArrayList<>());

        foods.add(pizza);
        foods.add(pasta);

        final InventoryDto inventoryDto = new InventoryDto(1L, foods);

        inventoryService.createInventory(inventoryDto);

        // Modify existing food amounts
        pizza.setAmount(100);
        pasta.setAmount(100);

        final List<Food> updatedFoods = new ArrayList<>();
        updatedFoods.add(pizza);
        updatedFoods.add(pasta);

        inventoryDto.setFoods(updatedFoods);

        final InventoryDto updatedInventoryDto = inventoryService.updateInventory(inventoryDto);

        // Validate updated values
        assertAll("Updated InventoryDto contents",
                () -> assertEquals(1L, updatedInventoryDto.getId()),
                () -> assertEquals(100, updatedInventoryDto.getFoods().get(0).getAmount()),
                () -> assertEquals(100, updatedInventoryDto.getFoods().get(1).getAmount()));
    }
}
