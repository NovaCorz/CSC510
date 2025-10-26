package FoodSeer.controller;

// ...existing code...
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// ...existing code...
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// ...existing code...
import org.springframework.boot.test.context.SpringBootTest;
// ...existing code...
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import FoodSeer.config.Roles;
import FoodSeer.config.Roles.UserRoles;
import FoodSeer.dto.RegisterRequestDto;
import FoodSeer.dto.UpdateRoleDto;
import FoodSeer.entity.User;
import FoodSeer.service.AuthService;
import FoodSeer.service.UserService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private User testUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(UserRoles.ROLE_STANDARD.name())
                .build();

        adminUser = User.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .password("adminpass")
                .role(Roles.ROLE_ADMIN)
                .build();

        authService.register(new RegisterRequestDto(testUser.getUsername(), testUser.getEmail(), testUser.getPassword()));
        authService.register(new RegisterRequestDto(adminUser.getUsername(), adminUser.getEmail(), adminUser.getPassword()));
        userService.updateUserRole(userService.getByUsername(adminUser.getUsername()).getId(), "ROLE_ADMIN");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAllUsers() throws Exception {
        mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].username", hasItem(testUser.getUsername())))
            .andExpect(jsonPath("$[*].username", hasItem(adminUser.getUsername())))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "STANDARD")
    void shouldNotAllowNonAdminToListUsers() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserById() throws Exception {
    // Test for testUser
    mockMvc.perform(get("/api/users/" + 2)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.username").value(testUser.getUsername()));

    // Test for adminUser
    mockMvc.perform(get("/api/users/" + 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value(adminUser.getUsername()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenUserNotFound() throws Exception {

        mockMvc.perform(get("/api/users/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUserRole() throws Exception {
        mockMvc.perform(put("/api/users/" + testUser.getId() + "/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateRoleDto(Roles.ROLE_ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(Roles.ROLE_ADMIN));
    }

    @Test
    @WithMockUser(roles = "STANDARD")
    void shouldNotAllowNonAdminToUpdateRole() throws Exception {
        mockMvc.perform(put("/api/users/" + testUser.getId() + "/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateRoleDto(Roles.ROLE_ADMIN))))
                .andExpect(status().isForbidden());
    }
}