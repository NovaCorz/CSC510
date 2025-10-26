package FoodSeer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import FoodSeer.TestUtils;
import FoodSeer.dto.FoodDto;
import FoodSeer.repository.FoodRepository;
import FoodSeer.repository.InventoryRepository;
import FoodSeer.service.FoodService;

/**
 * Tests FoodController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FoodControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc mvc;

    /** Reference to food repository */
    @Autowired
    private FoodRepository foodRepository;

    /** Reference to inventory repository */
    @Autowired
    private InventoryRepository inventoryRepository;

    /** Reference to food service */
    @Autowired
    private FoodService foodService;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        foodRepository.deleteAll();
        inventoryRepository.deleteAll();
    }

    /**
     * Tests GET /api/foods endpoint
     *
     * @throws Exception
     *             if error
     */
    @Test
    @Transactional
    @WithMockUser ( username = "staff", roles = "STAFF" )
    void testGetFoods () throws Exception {
        mvc.perform( get( "/api/foods" ) ).andExpect( status().isOk() );
    }

    /**
     * Tests POST /api/foods endpoint (createFood)
     *
     * @throws Exception
     *             if error
     */
    @Test
    @Transactional
    @WithMockUser ( username = "staff", roles = "STAFF" )
    void testCreateFood () throws Exception {
        final FoodDto food1 = new FoodDto( "COFFEE", 5, 3, Arrays.asList( "MILK", "SUGAR" ) );

        mvc.perform( post( "/api/foods" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( food1 ) )
                .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.price" ).value( "3" ) )
                .andExpect( jsonPath( "$.foodName" ).value( "COFFEE" ) );

        final FoodDto food2 = new FoodDto( "PUMPKIN_SPICE", 10, 7, Arrays.asList( "CINNAMON" ) );
        final FoodDto savedFood2 = foodService.createFood( food2 );

        mvc.perform(
                get( "/api/foods/" + foodService.getFoodById( savedFood2.getId() ).getId() ) )
                .andExpect( status().isOk() );
    }

    /**
     * Tests updateFood endpoint
     *
     * @throws Exception
     *             if error
     */
    @Test
    @Transactional
    @WithMockUser ( username = "staff", roles = "STAFF" )
    void testUpdateFood () throws Exception {
        final FoodDto food1 = new FoodDto( "COFFEE", 5, 3, Arrays.asList( "MILK" ) );
    
        mvc.perform( post( "/api/foods" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( food1 ) )
                .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.amount" ).value( "5" ) )
                .andExpect( jsonPath( "$.foodName" ).value( "COFFEE" ) );
    
        final FoodDto updatedFood = new FoodDto( "COFFEE", 12, 4, Arrays.asList( "MILK", "SUGAR" ) );
    
        mvc.perform( post( "/api/foods/updateFood" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updatedFood ) )
                .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
    }
}
