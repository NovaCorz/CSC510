package FoodSeer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodSeer.entity.User;
import FoodSeer.repositories.UserRepository;
import FoodSeer.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean userExists ( final String username ) {
        return userRepository.existsByUsername( username );
    }

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
        userRepository.deleteById(id);
    }

}
