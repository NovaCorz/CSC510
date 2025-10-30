package FoodSeer.dto;

public record UserDto(Long id, String username, String email, String role, String costPreference, String dietaryRestrictions) {
    public static UserDto fromEntity(final FoodSeer.entity.User u) {
        if (u == null) return null;
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getCostPreference(), u.getDietaryRestrictions());
    }
}
