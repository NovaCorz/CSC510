package FoodSeer.config;

/**
 * Defines user roles for WolfCafe
 */
public class Roles {

    /** Admin role name */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * Defines all roles in the system, EXCEPT for the Admin role.
     */
    public enum UserRoles {

        /** Customer for FoodSeer */
        ROLE_CUSTOMER,

        /** Staff for FoodSeer */
        ROLE_STAFF

    }

}
