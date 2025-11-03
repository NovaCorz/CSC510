package FoodSeer.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
@AutoConfigureMockMvc
public class RegisterPageTest {

    private ChromeDriver driver;

    private WebDriverWait wait;

    private String baseUrl = "http://localhost:3000/";

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // options.addArguments("--headless");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(2));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testUserRegistration() {
        driver.get(baseUrl + "register");
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/register"));

        // Fill in registration form
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("email")).sendKeys("testuser@valid.com");
        driver.findElement(By.id("password")).sendKeys("Melvin");
        driver.findElement(By.id("confirmPassword")).sendKeys("Melvin");

        // Submit the form
        driver.findElement(By.className("login-button")).click();

        // Wait for redirection to login page
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/"));
        attemptLogin("testuser", "Melvin");
        
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/preferences"));
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/preferences");

    }

    private void attemptLogin(String username, String password) {
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(2));

        System.out.println("Loaded page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());

        // Find username and password fields and login button
        var usernameField = driver.findElement(By.id("username"));
        var passwordField = driver.findElement(By.id("password"));
        var loginButton = driver.findElement(By.className("login-button"));

        // Enter credentials
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        // Click login button
        
        loginButton.click();        
    }
}
