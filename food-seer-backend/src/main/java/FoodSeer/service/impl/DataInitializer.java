package FoodSeer.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import FoodSeer.entity.User;
import FoodSeer.entity.Role;
import FoodSeer.entity.Food;
import FoodSeer.repositories.UserRepository;
import FoodSeer.repositories.RoleRepository;
import FoodSeer.repositories.FoodRepository;

/**
 * Initializes application data such as a default admin user.
 */
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FoodRepository foodRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin-user-password:admin}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           FoodRepository foodRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.foodRepository = foodRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // Ensure roles exist
        if (roleRepository.findByName("ROLE_ADMIN") == null) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
        }
        if (roleRepository.findByName("ROLE_CUSTOMER") == null) {
            roleRepository.save(new Role(null, "ROLE_CUSTOMER"));
        }
        if (roleRepository.findByName("ROLE_STAFF") == null) {
            roleRepository.save(new Role(null, "ROLE_STAFF"));
        }

        // Ensure admin user exists or update password
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            final String hash = passwordEncoder.encode(adminPassword);
            admin = User.builder()
                    .username("admin")
                    .email("admin@localhost")
                    .password(hash)
                    .role("ROLE_ADMIN")
                    .build();
            userRepository.save(admin);
            System.out.println("Created default admin user 'admin' with password: " + adminPassword);
        } else {
            // Update admin password if it has changed
            final String hash = passwordEncoder.encode(adminPassword);
            admin.setPassword(hash);
            userRepository.save(admin);
            System.out.println("Updated admin user password to: " + adminPassword);
        }

        // Initialize sample food data if database is empty
        if (foodRepository.count() == 0) {
            System.out.println("Database empty - initializing sample food data...");
            
            List<Food> sampleFoods = new ArrayList<>();
            
            // Budget-friendly options (under $10)
            sampleFoods.add(new Food("COFFEE", 50, 3, Arrays.asList("CAFFEINE")));
            sampleFoods.add(new Food("TEA", 40, 2, Arrays.asList("CAFFEINE")));
            sampleFoods.add(new Food("BAGEL", 30, 4, Arrays.asList("GLUTEN", "WHEAT")));
            sampleFoods.add(new Food("BANANA", 60, 1, new ArrayList<>()));
            sampleFoods.add(new Food("APPLE", 50, 2, new ArrayList<>()));
            sampleFoods.add(new Food("ORANGE JUICE", 25, 5, new ArrayList<>()));
            sampleFoods.add(new Food("YOGURT", 35, 4, Arrays.asList("DAIRY", "LACTOSE")));
            sampleFoods.add(new Food("GRANOLA BAR", 45, 3, Arrays.asList("NUTS", "GLUTEN")));
            
            // Mid-range options ($10-$20)
            sampleFoods.add(new Food("TURKEY SANDWICH", 20, 12, Arrays.asList("GLUTEN", "WHEAT", "MEAT", "POULTRY")));
            sampleFoods.add(new Food("GARDEN SALAD", 15, 10, new ArrayList<>())); // Vegan-friendly
            sampleFoods.add(new Food("CAESAR SALAD", 15, 11, Arrays.asList("DAIRY", "EGGS"))); // Has cheese and dressing
            sampleFoods.add(new Food("PASTA", 18, 14, Arrays.asList("GLUTEN", "WHEAT", "EGGS"))); // Most pasta has eggs
            sampleFoods.add(new Food("PIZZA SLICE", 25, 8, Arrays.asList("GLUTEN", "DAIRY", "WHEAT", "LACTOSE")));
            sampleFoods.add(new Food("BURRITO", 22, 11, Arrays.asList("GLUTEN", "DAIRY", "WHEAT", "MEAT", "BEEF")));
            sampleFoods.add(new Food("VEGETABLE SOUP", 20, 9, new ArrayList<>())); // Vegan-friendly
            sampleFoods.add(new Food("CHICKEN NOODLE SOUP", 18, 10, Arrays.asList("GLUTEN", "WHEAT", "MEAT", "POULTRY")));
            sampleFoods.add(new Food("SUSHI ROLL", 15, 13, Arrays.asList("FISH", "SOY")));
            sampleFoods.add(new Food("CHICKEN WRAP", 18, 10, Arrays.asList("GLUTEN", "WHEAT", "MEAT", "POULTRY")));
            
            // Premium options (over $20)
            sampleFoods.add(new Food("STEAK", 10, 28, Arrays.asList("MEAT", "BEEF")));
            sampleFoods.add(new Food("SALMON", 12, 24, Arrays.asList("FISH")));
            sampleFoods.add(new Food("LOBSTER", 8, 35, Arrays.asList("SHELLFISH")));
            sampleFoods.add(new Food("SUSHI PLATTER", 10, 32, Arrays.asList("FISH", "SOY")));
            sampleFoods.add(new Food("RIBEYE", 8, 30, Arrays.asList("MEAT", "BEEF")));
            
            // Vegan/Vegetarian options
            sampleFoods.add(new Food("VEGGIE BURGER", 20, 11, Arrays.asList("GLUTEN", "SOY", "WHEAT")));
            sampleFoods.add(new Food("TOFU BOWL", 18, 12, Arrays.asList("SOY")));
            sampleFoods.add(new Food("QUINOA SALAD", 15, 13, new ArrayList<>()));
            sampleFoods.add(new Food("HUMMUS WRAP", 20, 9, Arrays.asList("GLUTEN", "WHEAT")));
            
            // Desserts
            sampleFoods.add(new Food("CHOCOLATE CAKE", 12, 7, Arrays.asList("GLUTEN", "DAIRY", "EGGS", "WHEAT")));
            sampleFoods.add(new Food("ICE CREAM", 25, 6, Arrays.asList("DAIRY", "LACTOSE")));
            sampleFoods.add(new Food("COOKIES", 30, 5, Arrays.asList("GLUTEN", "DAIRY", "EGGS", "WHEAT")));
            sampleFoods.add(new Food("FRUIT SALAD", 20, 8, new ArrayList<>()));
            
            // Save all sample foods
            foodRepository.saveAll(sampleFoods);
            System.out.println("Successfully created " + sampleFoods.size() + " sample food items!");
        } else {
            System.out.println("Food database already contains " + foodRepository.count() + " items - skipping sample data creation.");
        }
    }
}
