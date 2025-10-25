package FoodSeer.service;

import FoodSeer.entity.User;

public interface UserService {

    public boolean userExists ( String username );

    public User getCurrentUser ();

    /**
     * Returns all users in the system.
     */
    public java.util.List<FoodSeer.entity.User> listUsers();

    /**
     * Find user by id.
     */
    public FoodSeer.entity.User findById(Long id);

    /**
     * Update a user's role and return updated user.
     */
    public FoodSeer.entity.User updateUserRole(Long id, String role);

    /**
     * Delete a user by id.
     */
    public void deleteUser(Long id);

}
