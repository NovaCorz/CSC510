package FoodSeer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;
import FoodSeer.security.JwtAuthenticationFilter;
import FoodSeer.service.impl.JwtAuthenticationEntryPoint;

/**
 * Details about roles and permissions. This file should be edited with any
 * global roles/permissions for the application.
 */
@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

    /** JWT authentication entry point for an authenticated user */
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    /** Filters for authentication */
    private final JwtAuthenticationFilter     authenticationFilter;

    /** Encodes passwords */
    @Bean
    public static PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create global permission structures for roles.
     *
     * @param http
     *            the security object
     * @return the SecurityFilterChain with permission information
     * @throws Exception
     *             if error
     */
    @Bean
    public SecurityFilterChain securityFilterChain ( final HttpSecurity http ) throws Exception {
        http.csrf( ( csrf ) -> csrf.disable() ).authorizeHttpRequests( ( authorize ) -> {
            authorize.requestMatchers( "/auth/**" ).permitAll();

            authorize.requestMatchers( HttpMethod.OPTIONS, "/**" ).permitAll(); // allows
                                                                                // preflight
            authorize.requestMatchers( HttpMethod.GET, "/api/locations/{id:[0-9]+}" ).permitAll();

            authorize.anyRequest().authenticated();
        } ).httpBasic( Customizer.withDefaults() );

        http.exceptionHandling( exception -> exception.authenticationEntryPoint( authenticationEntryPoint ) );

        http.addFilterBefore( authenticationFilter, UsernamePasswordAuthenticationFilter.class );

        return http.build();
    }

    /**
     * Returns the AuthenticationManager for the project.
     *
     * @param configuration
     *            configuration information for authentication
     * @return AuthenticationManager
     * @throws Exception
     *             if error
     */
    @Bean
    public AuthenticationManager authenticationManager ( final AuthenticationConfiguration configuration )
            throws Exception {
        return configuration.getAuthenticationManager();
    }

}
