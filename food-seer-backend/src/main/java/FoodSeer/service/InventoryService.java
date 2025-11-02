package FoodSeer.service;

import FoodSeer.dto.InventoryDto;

/**
 * Interface defining the inventory behaviors for FoodSeer.
 * The inventory tracks all available Food items in the system.
 */
public interface InventoryService {

    /**
     * Creates a new inventory.
     *
     * @param inventoryDto
     *            The inventory data to create
     * @return The created inventory as a DTO
     */
    InventoryDto createInventory(InventoryDto inventoryDto);

    /**
     * Returns the current FoodSeer inventory.
     *
     * @return The single inventory instance as a DTO
     */
    InventoryDto getInventory();

    /**
     * Updates the contents of the FoodSeer inventory.
     *
     * @param inventoryDto
     *            The updated inventory data
     * @return The updated inventory as a DTO
     */
    InventoryDto updateInventory(InventoryDto inventoryDto);
}
