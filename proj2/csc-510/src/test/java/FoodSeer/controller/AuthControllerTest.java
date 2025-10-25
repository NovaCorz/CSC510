package FoodSeer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import FoodSeer.config.TestJpaConfig;
import FoodSeer.config.TestSecurityConfig;
import FoodSeer.dto.AuthResponseDto;
import FoodSeer.dto.LoginRequestDto;
import FoodSeer.dto.RegisterRequestDto;
import FoodSeer.service.AuthService;
import FoodSeer.service.UserService;

@WebMvcTest(AuthController.class)
@Import({TestSecurityConfig.class, TestJpaConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;



    private LoginRequestDto loginRequest;
    private RegisterRequestDto registerRequest;
    private String testToken;

    @BeforeEach
    void setUp() {
        testToken = "test.jwt.token";
        loginRequest = new LoginRequestDto("testuser", "password123");
        registerRequest = new RegisterRequestDto("testuser", "test@example.com", "password123");
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        when(authService.login(any(LoginRequestDto.class)))
                .thenReturn(ResponseEntity.ok(new AuthResponseDto(testToken)));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(testToken));
    }

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        when(authService.register(any(RegisterRequestDto.class))).thenAnswer(invocation -> {
            return ResponseEntity.ok(new AuthResponseDto(testToken));
        });

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(testToken));
    }

    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequestDto.class)))
                .thenReturn(ResponseEntity.status(401).build());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailRegisterWithExistingUsername() throws Exception {
        when(authService.register(any(RegisterRequestDto.class)))
                .thenReturn(ResponseEntity.badRequest().body(Map.of("error", "Username already taken")));

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already taken"));
    }
}
