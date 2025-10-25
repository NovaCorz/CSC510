package FoodSeer.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.mockito.Mockito;
import FoodSeer.security.JwtTokenProvider;
import FoodSeer.security.CustomUserDetailsService;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {
    
    @Bean
    @Primary
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/me").authenticated()
                .anyRequest().permitAll()
            );
        
        return http.build();
    }

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        JwtTokenProvider provider = Mockito.mock(JwtTokenProvider.class);
        // Configure mock behavior if needed
        return provider;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }
}