package FoodSeer.service;

import org.springframework.http.ResponseEntity;

import FoodSeer.dto.AuthResponseDto;
import FoodSeer.dto.LoginRequestDto;
import FoodSeer.dto.RegisterRequestDto;

public interface AuthService {
    public ResponseEntity< ? > register ( final RegisterRequestDto req );

    public ResponseEntity<AuthResponseDto> login ( final LoginRequestDto req );
}
