package FoodSeer.mapper;

import FoodSeer.dto.FoodDto;
import FoodSeer.entity.Food;

/**
 * Mapper class for foods
 */
public class FoodMapper {

    /**
     * Maps food to DTO
     *
     * @param food
     *            The food to map
     * @return The mapped food DTO.
     */
    public static FoodDto mapToFoodDto ( final Food food ) {
        final FoodDto foodDto = new FoodDto();
        foodDto.setId( food.getId() );
        foodDto.setFoodName( food.getFoodName() );
        foodDto.setAmount( food.getAmount() );
        foodDto.setPrice( food.getPrice() );
        foodDto.setAllergies( food.getAllergies() );
        return foodDto;
    }

    /**
     * Maps food DTO to food
     *
     * @param foodDto
     *            The DTO to map
     * @return The mapped food
     */
    public static Food mapToFood ( final FoodDto foodDto ) {
        final Food food = new Food();
        food.setId( foodDto.getId() );
        food.setFoodName( foodDto.getFoodName() );
        food.setAmount( foodDto.getAmount() );
        food.setPrice( foodDto.getPrice() );
        food.setAllergies( foodDto.getAllergies() );
        return food;
    }

}
