package FoodSeer.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import FoodSeer.entity.User;
import FoodSeer.repositories.UserRepository;
import lombok.AllArgsConstructor;

/**
 * Supports finding and logging in a user by username or email.
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /** Link to userRepository */
    private UserRepository userRepository;

    /**
     * Returns UserDetails for the user associated with the username or email
     * address.
     *
     * @param usernameOrEmail
     *            username or email to search for
     * @return UserDetails object representing the user.
     */
    @Override
    public UserDetails loadUserByUsername ( final String usernameOrEmail ) throws UsernameNotFoundException {
        final User user = userRepository.findByUsernameOrEmail( usernameOrEmail, usernameOrEmail )
                .orElseThrow( () -> new UsernameNotFoundException(
                        "User " + usernameOrEmail + " does not exist with the given username or email." ) );
        final Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add( new SimpleGrantedAuthority( user.getRole() ) );

        return new org.springframework.security.core.userdetails.User( usernameOrEmail, user.getPassword(),
                authorities );
    }
}
