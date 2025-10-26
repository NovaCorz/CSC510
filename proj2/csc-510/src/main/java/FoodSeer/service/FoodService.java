package FoodSeer.service;

import java.util.List;

import FoodSeer.dto.FoodDto;

/**
 * Food Service
 */
public interface FoodService {

    /**
     * Food constructors
     *
     * @param foodDto
     *            as a DTO object
     * @return FoodDto
     */
    FoodDto createFood ( FoodDto foodDto );

    /**
     * Food Id
     *
     * @param foodId
     *            as Long
     * @return Dto object
     */
    FoodDto getFoodById ( Long foodId );

    /**
     * Method that gets all foods
     *
     * @return FoodDto
     */
    List<FoodDto> getAllFoods ();

    /**
     * Delete Food
     *
     * @param foodId
     *            as Long
     */
    void deleteFood ( Long foodId );

    /**
     * Delete all foods
     */
    void deleteAllFoods ();

    /**
     * Helper method
     *
     * @param name
     *            of food
     * @return true if food is a duplicate
     */
    boolean isDuplicateName ( String name );

    /**
     * Helper method
     *
     * @param foodDto
     *            as a FoodDto object
     * @return true if the food is valid
     */
    boolean isValidFood ( FoodDto foodDto );

    /**
     * Update the food's inventory amount
     *
     * @param name
     *            as a String
     * @param amount
     *            as an int
     * @return FoodDto with new amount
     */
    FoodDto updateFood(final String name, final int amount, final int price, final List<String> allergies);

    /**
     * Returns true if the food already exists in the database.
     *
     * @param name
     *            food's name to check
     * @return FoodDto if already in the database
     */
    FoodDto getDuplicateName ( String name );
}
