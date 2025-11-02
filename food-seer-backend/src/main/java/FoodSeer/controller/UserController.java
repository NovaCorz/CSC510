package FoodSeer.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import FoodSeer.dto.UpdateRoleDto;
import FoodSeer.dto.UserDto;
import FoodSeer.dto.UserPreferencesDto;
import FoodSeer.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin("*")
@RequestMapping("/api/users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    // Admin-only: list all users
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> listUsers() {
        return userService.listUsers().stream().map(UserDto::fromEntity).collect(Collectors.toList());
    }

    // Admin-only: get user by id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        final var u = userService.findById(id);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserDto.fromEntity(u));
    }

    // Admin-only: update a user's role
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateRole(@PathVariable Long id, @RequestBody UpdateRoleDto req) {
        final var updated = userService.updateUserRole(id, req.role());
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserDto.fromEntity(updated));
    }

    // Admin-only: delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping ( "/me" )
    public ResponseEntity<UserDto> getCurrentUser () {
        return ResponseEntity.ok( UserDto.fromEntity(userService.getCurrentUser()) );
    }

    @PutMapping ( "/me/preferences" )
    public ResponseEntity<UserDto> updatePreferences (@RequestBody final UserPreferencesDto preferences) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        final String username = auth.getName();
        final var updatedUser = userService.updateUserPreferences(
            username, 
            preferences.costPreference(), 
            preferences.dietaryRestrictions()
        );
        
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(UserDto.fromEntity(updatedUser));
    }

}
