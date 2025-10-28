package FoodSeer.dto;

import java.util.List;

import FoodSeer.entity.Food;

/**
 * Used to transfer Inventory data between the client and server.
 * This class serves as the response object in the REST API.
 */
public class InventoryDto {

    /** ID for inventory entry */
    private Long id;

    /** List of food objects */
    private List<Food> foods;

    /**
     * Default InventoryDto constructor.
     */
    public InventoryDto() {
        super();
    }

    /**
     * Constructs an InventoryDto object from field values.
     *
     * @param id
     *            Inventory ID
     * @param foods
     *            The list of foods in the inventory
     */
    public InventoryDto(final Long id, final List<Food> foods) {
        super();
        this.id = id;
        this.foods = foods;
    }

    /**
     * Gets the inventory ID.
     *
     * @return The ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the inventory ID.
     *
     * @param id
     *            The ID to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the list of food objects.
     *
     * @return List(Food) foods list
     */
    public List<Food> getFoods() {
        return foods;
    }

    /**
     * Sets the list of foods.
     *
     * @param foods
     *            List of Food objects
     */
    public void setFoods(final List<Food> foods) {
        this.foods = foods;
    }
}
