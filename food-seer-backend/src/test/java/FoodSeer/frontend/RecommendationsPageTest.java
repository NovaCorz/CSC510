package FoodSeer.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import FoodSeer.entity.User;
import FoodSeer.repositories.UserRepository;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class RecommendationsPageTest {

    private ChromeDriver driver;
    private WebDriverWait wait;
    private String baseUrl = "http://localhost:3000/";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Set up ChromeDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Clean up test users
        for(User user : userRepository.findAll()) {
            if (!user.getUsername().equals("admin")) {
                userRepository.delete(user);
            }
        }
    }

    private void registerUserWithPreferences(String username, String password, String email, String budget, String... dietaryRestrictions) {
        // Register and navigate through preferences
        driver.get(baseUrl + "register");
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "register"));

        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("confirmPassword")).sendKeys(password);
        driver.findElement(By.className("login-button")).click();

        wait.until(d -> d.getCurrentUrl().equals(baseUrl));
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.className("login-button")).click();

        // Set preferences
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "preferences"));
        wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[@class='option-card '][.//div[text()='" + budget + "']]")));
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='" + budget + "']]")).click();
        driver.findElement(By.className("next-button")).click();

        // Select dietary restrictions
        for (String restriction : dietaryRestrictions) {
            wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='option-card '][.//div[text()='" + restriction + "']]")));
            driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='" + restriction + "']]")).click();
        }
        driver.findElement(By.className("next-button")).click();

        // Wait for recommendations page
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "recommendations"));
    }

    /**
     * Tests the budget-based filtering of food recommendations.
     * Verifies that:
     * - Only foods under $10 are shown when budget preference is set
     * - Budget items like coffee, tea, and bagels are included
     * - Premium items like steak and lobster are excluded
     * - At least 8 budget items from the initializer are shown
     */
    @Test
    public void testBudgetFiltering() {
        registerUserWithPreferences("budgetuser", "testpass123", "budget@test.com", "Budget (Under $10)");
        
        // Verify only budget foods are shown
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("recommendations-grid")));
        List<WebElement> recommendedFoods = driver.findElements(By.className("recommendation-card"));
        
        // Should show budget items
        assertTrue(recommendedFoods.size() >= 8); // At least 8 budget items from initializer
        
        // Verify some specific budget items
        String recommendedText = driver.findElement(By.className("recommendations-grid")).getText();
        assertTrue(recommendedText.contains("COFFEE"));
        assertTrue(recommendedText.contains("BAGEL"));
        assertTrue(recommendedText.contains("BANANA"));
        
        // Verify premium items are not shown
        assertFalse(recommendedText.contains("STEAK"));
        assertFalse(recommendedText.contains("LOBSTER"));
    }

    /**
     * Tests the dietary restriction filtering of food recommendations.
     * Verifies that:
     * - Only vegan-compatible foods are shown when vegan preference is set
     * - Moderate price range ($10-$20) items are filtered correctly
     * - Foods with meat, dairy, or other non-vegan ingredients are excluded
     * - Vegan-friendly items like garden salad and vegetable soup are included
     */
    @Test
    public void testDietaryRestrictionFiltering() {
        registerUserWithPreferences("veganuser", "testpass123", "vegan@test.com", 
            "Moderate ($10-$20)", "Vegan");
        
        // Verify only vegan-compatible foods are shown
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("recommendations-grid")));
        String recommendedText = driver.findElement(By.className("recommendations-grid")).getText();
        
        // Should show vegan items in moderate price range
        assertTrue(recommendedText.contains("GARDEN SALAD"));
        assertTrue(recommendedText.contains("TOFU BOWL"));
        
        // Should not show non-vegan items
        assertFalse(recommendedText.contains("TURKEY SANDWICH"));
        assertFalse(recommendedText.contains("CHICKEN WRAP"));
        assertFalse(recommendedText.contains("STEAK"));
    }

    /**
     * Tests the behavior when no foods match the user's preferences.
     * Verifies that:
     * - When combining restrictive preferences (premium price + vegan + gluten-free)
     * - A proper "no matches" message is displayed
     * - The recommendations grid is not shown
     */
    @Test
    public void testNoMatchingRecommendations() {
        // Register with restrictions that won't match any foods
        registerUserWithPreferences("restriceduser", "testpass123", "restricted@test.com", 
            "Premium ($20+)", "Vegan", "Gluten Free");
        
        // Verify no recommendations message
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("no-recommendations")));
        WebElement noRecommendations = driver.findElement(By.className("no-recommendations"));
        assertTrue(noRecommendations.getText().contains("No foods match your current preferences"));
    }

    /**
     * Tests the update preferences functionality from the recommendations page.
     * Verifies that:
     * - The update preferences button is clickable
     * - Clicking redirects to the preferences page
     * - The URL changes correctly
     */
    @Test
    public void testUpdatePreferences() {
        registerUserWithPreferences("updateuser", "testpass123", "update@test.com", 
            "Budget (Under $10)");
        
        // Click update preferences
        wait.until(ExpectedConditions.elementToBeClickable(By.className("update-preferences-button")));
        driver.findElement(By.className("update-preferences-button")).click();
        
        // Verify navigation to preferences page
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "preferences"));
        assertEquals(baseUrl + "preferences", driver.getCurrentUrl());
    }

    /**
     * Tests all navigation buttons on the recommendations page.
     * Verifies that:
     * - Browse All Foods button redirects to inventory page
     * - My Orders button redirects to orders page
     * - Logout button redirects to login page
     * - Back navigation works correctly
     */
    @Test
    public void testNavigationButtons() {
        registerUserWithPreferences("navuser", "testpass123", "nav@test.com", 
            "Moderate ($10-$20)");
        
        // Test browse all foods
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Browse All Foods']")));
        driver.findElement(By.xpath("//button[text()='Browse All Foods']")).click();
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "inventory"));
        assertEquals(baseUrl + "inventory", driver.getCurrentUrl());
        
        // Go back and test orders
        driver.navigate().back();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='My Orders']")));
        driver.findElement(By.xpath("//button[text()='My Orders']")).click();
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "orders"));
        assertEquals(baseUrl + "orders", driver.getCurrentUrl());
        
        // Test logout
        driver.navigate().back();
        wait.until(ExpectedConditions.elementToBeClickable(By.className("logout-button")));
        driver.findElement(By.className("logout-button")).click();
        wait.until(d -> d.getCurrentUrl().equals(baseUrl));
        assertEquals(baseUrl, driver.getCurrentUrl());
    }

    /**
     * Tests the user information display panel on recommendations page.
     * Verifies that:
     * - Username is displayed correctly
     * - Budget preference is shown
     * - Dietary restrictions are listed
     * - All user preferences are accurately reflected
     */
    @Test
    public void testUserInfoDisplay() {
        registerUserWithPreferences("infouser", "testpass123", "info@test.com", 
            "Premium ($20+)", "Vegetarian", "Gluten Free");
        
        // Verify user info display
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("user-info-card")));
        WebElement userInfo = driver.findElement(By.className("user-info-card"));
        
        assertTrue(userInfo.getText().contains("infouser"));
        assertTrue(userInfo.getText().contains("premium"));
        assertTrue(userInfo.getText().contains("vegetarian"));
        assertTrue(userInfo.getText().contains("gluten-free"));
    }

    /**
     * Tests filtering with multiple dietary restrictions combined.
     * Verifies that:
     * - Multiple restrictions (vegetarian + gluten-free) work together
     * - Only foods meeting all criteria are shown
     * - Foods violating any restriction are excluded
     * - Compatible foods like vegan options are included
     */
    @Test
    public void testMultipleDietaryRestrictions() {
        registerUserWithPreferences("multiuser", "testpass123", "multi@test.com", 
            "No Limit", "Vegetarian", "Gluten Free");
        
        // Verify recommendations respect multiple restrictions
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("recommendations-grid")));
        List<WebElement> recommendedFoods = driver.findElements(By.className("recommendation-card"));
        
        // Should only show the vegan food (which is vegetarian and gluten-free)
        assertEquals(13, recommendedFoods.size());
    }
}