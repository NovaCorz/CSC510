package FoodSeer.service;

import FoodSeer.entity.User;

public interface UserService {

    public boolean userExists ( String username );

    public User getCurrentUser ();

}
