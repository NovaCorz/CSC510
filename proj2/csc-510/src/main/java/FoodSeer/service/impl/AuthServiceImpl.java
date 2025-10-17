package FoodSeer.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import FoodSeer.dto.AuthResponseDto;
import FoodSeer.dto.LoginRequestDto;
import FoodSeer.dto.RegisterRequestDto;
import FoodSeer.entity.User;
import FoodSeer.repositories.UserRepository;
import FoodSeer.security.JwtTokenProvider;
import FoodSeer.service.AuthService;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final UserRepository        userRepository;

    private final PasswordEncoder       passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider      jwtService;

    @Override
    public ResponseEntity< ? > register ( final RegisterRequestDto req ) {
        System.out.println( "PasswordEncoder bean is: " + passwordEncoder.getClass() );
        if ( userRepository.existsByUsername( req.username() ) ) {
            return ResponseEntity.badRequest().body( Map.of( "error", "Username already taken" ) );
        }
        final String hash = passwordEncoder.encode( req.password() );
        final User hashedUser = new User( req, hash );
        userRepository.save( hashedUser );
        return ResponseEntity.ok( Map.of( "message", "Registered" ) );
    }

    @Override
    public ResponseEntity<AuthResponseDto> login ( final LoginRequestDto req ) {
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken( req.username(),
                req.password() );
        final Authentication authentication = authManager.authenticate( auth );

        final String token = jwtService.generateToken( authentication );
        return ResponseEntity.ok( new AuthResponseDto( token ) );
    }
}
