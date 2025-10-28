package FoodSeer.mapper;

import FoodSeer.dto.InventoryDto;
import FoodSeer.entity.Inventory;

/**
 * Converts between InventoryDto and Inventory entity.
 */
public class InventoryMapper {

    /**
     * Converts an Inventory entity to InventoryDto.
     *
     * @param inventory
     *            Inventory to convert
     * @return InventoryDto object
     */
    public static InventoryDto mapToInventoryDto(final Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        return new InventoryDto(inventory.getId(), inventory.getFoods());
    }

    /**
     * Converts an InventoryDto to an Inventory entity.
     *
     * @param inventoryDto
     *            InventoryDto to convert
     * @return Inventory entity
     */
    public static Inventory mapToInventory(final InventoryDto inventoryDto) {
        if (inventoryDto == null) {
            return null;
        }
        return new Inventory(inventoryDto.getId(), inventoryDto.getFoods());
    }
}
