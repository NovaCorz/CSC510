package FoodSeer.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class PreferencesPageTest {

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
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));

        registerAndLogin("preftest", "testpass123", "preftest@test.com");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Workaround since transactional doesn't work. Delete all additions
        for(User user : userRepository.findAll()){
            if( !user.getUsername().equals("admin")){
                userRepository.delete(user);
            }
        }
    }

    /**
     * Tests the complete flow of setting user preferences.
     * Verifies that:
     * - User can select budget preference (Under $10)
     * - User can select multiple dietary restrictions (Vegetarian, Gluten Free)
     * - Preferences are saved correctly in the database
     * - User is redirected to recommendations after completing
     */
    @Test
    public void testCompletePreferencesFlow() {
        // Should be redirected to preferences page
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "preferences"));
        
        // Test budget selection
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card '][.//div[text()='Budget (Under $10)']]")));
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Budget (Under $10)']]")).click();
        
        // Click next to go to dietary restrictions
        WebElement nextButton = driver.findElement(By.className("next-button"));
        assertTrue(nextButton.isEnabled());
        nextButton.click();
        
        // Select dietary restrictions
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card '][.//div[text()='Vegetarian']]")));
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Vegetarian']]")).click();
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Gluten Free']]")).click();
        
        // Finish and save preferences
        WebElement finishButton = driver.findElement(By.className("next-button"));
        assertTrue(finishButton.isEnabled());
        finishButton.click();
        
        // Should be redirected to recommendations page
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "recommendations"));
        assertEquals(baseUrl + "recommendations", driver.getCurrentUrl());
        
        // Verify preferences were saved
        User updatedUser = userRepository.findByUsername("preftest").orElseThrow();
        assertEquals("budget", updatedUser.getCostPreference());
        assertTrue(updatedUser.getDietaryRestrictions().contains("vegetarian"));
        assertTrue(updatedUser.getDietaryRestrictions().contains("gluten-free"));
    }

    /**
     * Tests that existing user preferences are loaded correctly.
     * Verifies that:
     * - Previously selected budget option is pre-selected
     * - Previously chosen dietary restrictions are pre-selected
     * - UI reflects the correct state when revisiting preferences
     * - All selections persist between page navigations
     */
    @Test
    public void testLoadExistingPreferences() {
        
        // Set preferences
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card '][.//div[text()='Premium ($20+)']")));
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Premium ($20+)']]")).click();
        driver.findElement(By.className("next-button")).click();
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card '][.//div[text()='Vegan']]")));
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Vegan']]")).click();
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Gluten Free']]")).click();
        driver.findElement(By.className("next-button")).click();
        // Wait here
        wait.until(d -> !d.getCurrentUrl().equals(baseUrl + "preferences"));
        // Go back to preferences page to check if they loaded
        driver.get(baseUrl + "preferences");
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "preferences"));
        
        // Verify budget selection is pre-selected
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card selected'][.//div[text()='Premium ($20+)']]")));
        WebElement budgetHigh = driver.findElement(By.xpath("//div[@class='option-card selected'][.//div[text()='Premium ($20+)']]"));
        assertTrue(budgetHigh != null);
        
        // Go to dietary restrictions
        driver.findElement(By.className("next-button")).click();
        
        // Verify dietary restrictions are pre-selected
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card selected'][.//div[text()='Vegan']]")));
        WebElement veganCheckbox = driver.findElement(By.xpath("//div[@class='option-card selected'][.//div[text()='Vegan']]"));
        WebElement kosherCheckbox = driver.findElement(By.xpath("//div[@class='option-card selected'][.//div[text()='Gluten Free']]"));
        assertTrue(veganCheckbox != null);
        assertTrue(kosherCheckbox != null);
    }

    /**
     * Tests navigation between preference selection steps.
     * Verifies that:
     * - Next button is disabled until a selection is made
     * - Selected options are preserved when navigating back
     * - Previous button returns to the correct step
     * - UI state accurately reflects current selections
     */
    @Test
    public void testNavigationBetweenSteps() {
        wait.until(d -> d.getCurrentUrl().equals(baseUrl + "preferences"));
        
        // Start at budget step
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card '][.//div[text()='Moderate ($10-$20)']]")));
        WebElement nextButton = driver.findElement(By.className("next-button"));
        assertTrue(!nextButton.isEnabled()); // Should be disabled until selection made
        
        // Select budget and go next
        driver.findElement(By.xpath("//div[@class='option-card '][.//div[text()='Moderate ($10-$20)']]")).click();
        assertTrue(nextButton.isEnabled());
        nextButton.click();
        
        // At dietary restrictions step
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card '][.//div[text()='Vegetarian']]")));
        
        // Go back to budget step
        WebElement prevButton = driver.findElement(By.className("previous-button"));
        prevButton.click();
        
        // Verify we're back at budget step and selection is preserved
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='option-card selected'][.//div[text()='Moderate ($10-$20)']]")));
        WebElement budgetMedium = driver.findElement(By.xpath("//div[@class='option-card selected'][.//div[text()='Moderate ($10-$20)']]"));
        assertTrue(budgetMedium != null);
    }

    private void registerAndLogin(String username, String password, String email) {
        // Go to register page
        driver.get(baseUrl + "register");
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/register"));

        // Fill in registration form
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("confirmPassword")).sendKeys(password);

        // Submit registration form
        driver.findElement(By.className("login-button")).click();

        // Wait for redirection to login page and log in
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/"));
        
        // Login with new credentials
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.className("login-button")).click();
    }
}
