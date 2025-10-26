package FoodSeer.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Inventory for the FoodSeer application.
 * Inventory is a Data Access Object (DAO) tied to the database using Hibernate.
 * InventoryRepository provides the methods for database CRUD operations.
 */
@Entity
public class Inventory {

    /** ID for inventory entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** List of food objects in the inventory */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Food> foods;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory() {
        // Intentionally empty so Hibernate can instantiate Inventory objects
    }

    /**
     * Creates an Inventory with all fields
     *
     * @param id
     *            Inventory's ID
     * @param foods
     *            The list of foods in the inventory
     */
    public Inventory(final Long id, final List<Food> foods) {
        super();
        this.id = id;
        this.foods = foods;
    }

    /**
     * Returns the ID of the inventory entry in the database
     *
     * @return The inventory ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the Inventory (used by Hibernate)
     *
     * @param id
     *            The inventory ID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the list of food objects
     *
     * @return List(Food) foods in the inventory
     */
    public List<Food> getFoods() {
        return foods;
    }

    /**
     * Sets the list of foods
     *
     * @param foods
     *            List of Food objects
     */
    public void setFoods(final List<Food> foods) {
        this.foods = foods;
    }
}
