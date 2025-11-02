package FoodSeer.repositories;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import FoodSeer.entity.Food;
import jakarta.transaction.Transactional;

/**
 * Repository test class for Food entity
 */
@DataJpaTest
@AutoConfigureTestDatabase ( replace = Replace.NONE )
class FoodRepositoryTest {

    /** Food repository */
    @Autowired
    private FoodRepository foodRepository;

    /** Food 1 ID */
    private Long food1Id;

    /** Food 2 ID */
    private Long food2Id;

    @BeforeEach
    public void setUp () throws Exception {
        foodRepository.deleteAll();

        final List<String> allergies1 = Arrays.asList( "MILK", "SUGAR" );
        final List<String> allergies2 = Arrays.asList( "CINNAMON" );

        final Food food1 = new Food("COFFEE", 5, 3, allergies1 );
        final Food food2 = new Food("PUMPKIN_SPICE", 10, 7, allergies2 );

        food1Id = foodRepository.save( food1 ).getId();
        food2Id = foodRepository.save( food2 ).getId();
    }

    @Test
    @Transactional
    public void testAddFoods () {
        final Food f1 = foodRepository.findById( food1Id ).get();
        assertAll( "Food 1 contents",
                () -> assertEquals( food1Id, f1.getId() ),
                () -> assertEquals( "COFFEE", f1.getFoodName() ),
                () -> assertEquals( 5, f1.getAmount() ),
                () -> assertEquals( 3, f1.getPrice() ),
                () -> assertEquals( Arrays.asList( "MILK", "SUGAR" ), f1.getAllergies() ) );

        final Food f2 = foodRepository.findById( food2Id ).get();
        assertAll( "Food 2 contents",
                () -> assertEquals( food2Id, f2.getId() ),
                () -> assertEquals( "PUMPKIN_SPICE", f2.getFoodName() ),
                () -> assertEquals( 10, f2.getAmount() ),
                () -> assertEquals( 7, f2.getPrice() ),
                () -> assertEquals( Arrays.asList( "CINNAMON" ), f2.getAllergies() ) );
    }

}
