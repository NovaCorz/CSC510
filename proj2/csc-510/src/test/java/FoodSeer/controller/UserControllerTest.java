package FoodSeer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import FoodSeer.config.Roles;
import FoodSeer.config.Roles.UserRoles;
import FoodSeer.config.TestJpaConfig;
import FoodSeer.config.TestSecurityConfig;
import FoodSeer.dto.UpdateRoleDto;
import FoodSeer.entity.User;
import FoodSeer.service.UserService;

@WebMvcTest(UserController.class)
@Import({TestSecurityConfig.class, TestJpaConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldListAllUsers() throws Exception {
        when(userService.listUsers()).thenReturn(Arrays.asList(testUser, adminUser));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testUser.getId()))
                .andExpect(jsonPath("$[0].username").value(testUser.getUsername()))
                .andExpect(jsonPath("$[1].id").value(adminUser.getId()))
                .andExpect(jsonPath("$[1].username").value(adminUser.getUsername()));
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
        when(userService.findById(testUser.getId())).thenReturn(testUser);

        mockMvc.perform(get("/api/users/" + testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.username").value(testUser.getUsername()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(userService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/users/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUserRole() throws Exception {
        User updatedUser = User.builder()
                .id(testUser.getId())
                .username(testUser.getUsername())
                .email(testUser.getEmail())
                .role(Roles.ROLE_ADMIN)
                .build();

        when(userService.updateUserRole(eq(testUser.getId()), any(String.class))).thenReturn(updatedUser);

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