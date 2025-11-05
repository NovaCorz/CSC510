package FoodSeer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FoodSeer.dto.FoodDto;
import FoodSeer.service.FoodService;

/**
 * Controller class for food
 */
@CrossOrigin ( "*" )
@RestController
@RequestMapping ( "/api/foods" )
public class FoodController {

    /**
     * Food service to use
     */
    @Autowired
    private FoodService foodService;

    /**
     * Gets the food based on the ID parameter
     *
     * @param id
     *            The ID of the food to return
     * @return The food
     */
    @GetMapping ( "{id}" )
    public ResponseEntity<FoodDto> getFood ( @PathVariable ( "id" ) final Long id ) {
        final FoodDto foodDto = foodService.getFoodById( id );
        return ResponseEntity.ok( foodDto );
    }

    /**
     * POST mapping to create a food
     *
     * @param foodDto
     *            The food DTO to create
     * @return response from creation
     */
    @PostMapping
    public ResponseEntity<FoodDto> createFood ( @RequestBody final FoodDto foodDto ) {
        if ( foodService.isDuplicateName( foodDto.getFoodName() ) ) {
            return new ResponseEntity<>( foodDto, HttpStatus.CONFLICT );
        }

        // Validate food before saving
        if ( !foodService.isValidFood( foodDto ) ) {
            return new ResponseEntity<>( foodDto, HttpStatus.BAD_REQUEST );
        }

        final FoodDto savedFoodDto = foodService.createFood( foodDto );
        return ResponseEntity.ok( savedFoodDto );
    }

    /**
     * REST API method to provide GET access to all foods in the system
     *
     * @return JSON representation of all foods
     */
    @GetMapping
    public List<FoodDto> getFoods () {
        return foodService.getAllFoods();
    }

    /**
     * Deletes the food based on params
     *
     * @param foodId
     *            Id of the food to delete
     * @return Response Entity
     */
    @DeleteMapping ( "{id}" )
    public ResponseEntity<String> deleteFood ( @PathVariable ( "id" ) final Long foodId ) {
        try {
            foodService.deleteFood( foodId );
            return ResponseEntity.ok( "Food deleted successfully." );
        } catch (final IllegalStateException e) {
            // Food is part of unfulfilled orders
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Updates an existing food's details (amount, price, and allergies).
     *
     * @param foodDto
     *            FoodDto containing the updated fields
     * @return ResponseEntity containing the updated FoodDto
     */
    @PostMapping ( "/updateFood" )
    public ResponseEntity<FoodDto> updateFood ( @RequestBody final FoodDto foodDto ) {
        // Check if food exists first by ID or name
        final String name = foodDto.getFoodName();
        if (name == null || name.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Verify that food exists
        if (!foodService.isDuplicateName(name)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Validate updated values
        if (foodDto.getAmount() < 0 || foodDto.getPrice() < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Perform the update using new service signature
        final FoodDto updatedFood = foodService.updateFood(
            name,
            foodDto.getAmount(),
            foodDto.getPrice(),
            foodDto.getAllergies()
        );

        return ResponseEntity.ok(updatedFood);
    }


}
