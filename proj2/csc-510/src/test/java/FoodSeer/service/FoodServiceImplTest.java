package FoodSeer.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import FoodSeer.dto.FoodDto;
import FoodSeer.exception.ResourceNotFoundException;

/**
 * Test Food service implementation
 */
@SpringBootTest
public class FoodServiceImplTest {

    /** Food service */
    @Autowired
    private FoodService foodService;

    /** Delete all foods before each test */
    @BeforeEach
    public void setUp () throws Exception {
        foodService.deleteAllFoods();
    }

    /**
     * Test create food
     */
    @Test
    @Transactional
    public void testCreateFood () {
        final FoodDto food1 = new FoodDto( "COFFEE", 5, 3, Arrays.asList( "MILK", "SUGAR" ) );
        final FoodDto createdFood1 = foodService.createFood( food1 );
        assertAll( "Food contents",
                () -> assertEquals( "COFFEE", createdFood1.getFoodName() ),
                () -> assertEquals( 5, createdFood1.getAmount() ),
                () -> assertEquals( 3, createdFood1.getPrice() ),
                () -> assertEquals( Arrays.asList( "MILK", "SUGAR" ), createdFood1.getAllergies() ) );

        final FoodDto food2 = new FoodDto( "PUMPKIN_SPICE", 10, 7, Arrays.asList( "CINNAMON" ) );
        final FoodDto createdFood2 = foodService.createFood( food2 );
        assertAll( "Food contents",
                () -> assertEquals( "PUMPKIN_SPICE", createdFood2.getFoodName() ),
                () -> assertEquals( 10, createdFood2.getAmount() ),
                () -> assertEquals( 7, createdFood2.getPrice() ),
                () -> assertEquals( Arrays.asList( "CINNAMON" ), createdFood2.getAllergies() ) );
    }

    /**
     * Test get food by ID
     */
    @Test
    @Transactional
    public void testGetFoodById () {
        final FoodDto food1 = new FoodDto( "COFFEE", 5, 3, Arrays.asList( "MILK", "SUGAR" ) );
        final FoodDto createdFood1 = foodService.createFood( food1 );
        final FoodDto fetchedFood1 = foodService.getFoodById( createdFood1.getId() );
        assertAll( "Food contents",
                () -> assertEquals( "COFFEE", fetchedFood1.getFoodName() ),
                () -> assertEquals( 5, fetchedFood1.getAmount() ),
                () -> assertEquals( 3, fetchedFood1.getPrice() ),
                () -> assertEquals( Arrays.asList( "MILK", "SUGAR" ), fetchedFood1.getAllergies() ) );

        final FoodDto food2 = new FoodDto( "PUMPKIN_SPICE", 10, 7, Arrays.asList( "CINNAMON" ) );
        final FoodDto createdFood2 = foodService.createFood( food2 );
        final FoodDto fetchedFood2 = foodService.getFoodById( createdFood2.getId() );
        assertAll( "Food contents",
                () -> assertEquals( "PUMPKIN_SPICE", fetchedFood2.getFoodName() ),
                () -> assertEquals( 10, fetchedFood2.getAmount() ),
                () -> assertEquals( 7, fetchedFood2.getPrice() ),
                () -> assertEquals( Arrays.asList( "CINNAMON" ), fetchedFood2.getAllergies() ) );
    }

    /**
     * Test create invalid food
     */
    @Test
    @Transactional
    public void testCreateInvalidFood () {
        // invalid amount
        final FoodDto food1 = new FoodDto( "MATCHA", -1, 2, Arrays.asList( "MILK" ) );

        final IllegalArgumentException invalidAmountException = assertThrows( IllegalArgumentException.class, () -> {
            foodService.createFood( food1 );
        } );
        assertEquals( "The provided food information is invalid.", invalidAmountException.getMessage() );

        // duplicate food name
        final FoodDto food2 = new FoodDto( "COFFEE", 5, 3, Arrays.asList( "MILK" ) );
        final FoodDto createdFood1 = foodService.createFood( food2 );
        assertAll( "Food contents",
                () -> assertEquals( "COFFEE", createdFood1.getFoodName() ),
                () -> assertEquals( 5, createdFood1.getAmount() ) );

        final FoodDto food3 = new FoodDto( "COFFEE", 8, 4, Arrays.asList( "MILK", "SUGAR" ) );
        final IllegalArgumentException duplicateNameException = assertThrows( IllegalArgumentException.class, () -> {
            foodService.createFood( food3 );
        } );
        assertEquals( "The name of the new food already exists in the system.", duplicateNameException.getMessage() );

        // non-existent ID check
        final Long nonExistentId = 9999L;
        final ResourceNotFoundException notFoundException = assertThrows( ResourceNotFoundException.class, () -> {
            foodService.getFoodById( nonExistentId );
        } );
        assertEquals( "Food does not exist with id " + nonExistentId, notFoundException.getMessage() );
    }

    /**
     * Test update food amount
     */
    @Test
    @Transactional
    public void testUpdateFood () {
        final FoodDto food1 = new FoodDto( "COFFEE", 5, 3, Arrays.asList( "MILK" ) );
        final FoodDto createdFood1 = foodService.createFood( food1 );
        assertAll( "Food contents",
                () -> assertEquals( "COFFEE", createdFood1.getFoodName() ),
                () -> assertEquals( 5, createdFood1.getAmount() ) );

        final FoodDto updatedFood = foodService.updateFood( "COFFEE", 12 );
        assertAll( "Updated food contents",
                () -> assertEquals( "COFFEE", updatedFood.getFoodName() ),
                () -> assertEquals( 12, updatedFood.getAmount() ) );
    }
}
