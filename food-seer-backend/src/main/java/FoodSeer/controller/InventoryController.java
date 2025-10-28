package FoodSeer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FoodSeer.dto.InventoryDto;
import FoodSeer.service.InventoryService;

/**
 * Controller for FoodSeer's inventory.
 * The inventory represents the full list of available foods in the system.
 * This is typically a singleton entry in the database.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    /**
     * Connection to InventoryService for manipulating the Inventory model.
     */
    @Autowired
    private InventoryService inventoryService;

    /**
     * REST API endpoint to provide GET access to the FoodSeer inventory.
     *
     * @return ResponseEntity containing the current inventory
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @GetMapping
    public ResponseEntity<InventoryDto> getInventory() {
        final InventoryDto inventoryDto = inventoryService.getInventory();
        return ResponseEntity.ok(inventoryDto);
    }

    /**
     * REST API endpoint to update the FoodSeer inventory.
     *
     * @param inventoryDto
     *            Updated inventory data
     * @return ResponseEntity containing the saved InventoryDto
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<InventoryDto> updateInventory(@RequestBody final InventoryDto inventoryDto) {
        final InventoryDto savedInventoryDto = inventoryService.updateInventory(inventoryDto);
        return ResponseEntity.ok(savedInventoryDto);
    }
}
