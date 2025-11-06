package FoodSeer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodSeer.entity.Order;
import FoodSeer.entity.User;
import FoodSeer.repositories.OrderRepository;
import FoodSeer.repositories.UserRepository;
import FoodSeer.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public User getCurrentUser () {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( auth == null || !auth.isAuthenticated() ) {
            return null;
        }

        final String username = auth.getName();
        final Optional<User> u = userRepository.findByUsername( username );
        return u.orElse( null );
    }

    @Override
    public java.util.List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(final Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUserRole(final Long id, final String role) {
        final Optional<User> u = userRepository.findById(id);
        if (u.isEmpty()) return null;
        final User user = u.get();
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(final Long id) {
        // First, find the user
        final Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return; // User doesn't exist, nothing to delete
        }
        
        final User user = userOpt.get();
        
        // Delete all orders associated with this user
        final List<Order> userOrders = orderRepository.findByUser(user);
        orderRepository.deleteAll(userOrders);
        
        // Now delete the user
        userRepository.deleteById(id);
    }

    @Override
    public User getByUsername(String username) {
        final Optional<User> u = userRepository.findByUsername(username);
        return u.orElse(null);
    }

    @Override
    public User updateUserPreferences(String username, String costPreference, String dietaryRestrictions) {
        final Optional<User> u = userRepository.findByUsername(username);
        if (u.isEmpty()) return null;
        final User user = u.get();
        user.setCostPreference(costPreference);
        user.setDietaryRestrictions(dietaryRestrictions);
        return userRepository.save(user);
    }
}
