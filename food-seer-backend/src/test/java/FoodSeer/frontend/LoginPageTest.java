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
public class LoginPageTest {

    private ChromeDriver driver;

    private String baseUrl = "http://localhost:3000/";

    private String adminUsername = "admin";

    private WebDriverWait wait;

    @Value("${app.admin-user-password}")
    private String adminPassword;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
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
    public void testAdminLogin() {
        attemptLogin(adminUsername, adminPassword);

        // Assert login is successful by checking we make it to the order management page
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/order-management"));
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/order-management");
    }

    @Test
    public void testInvalidLogin() {
        attemptLogin("WrongUser", "WrongPass");

        // Assert login failed by checking we are still on the login page
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/"));
        assertEquals(driver.getCurrentUrl(), baseUrl);
        assertEquals(driver.findElement(By.className("error-message")).getText(), "Invalid username or password");
    }

    @Test
    public void testEmptyCredentials() {
        attemptLogin("", "");

        // Assert login failed by checking we are still on the login page
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/"));
        assertEquals(driver.getCurrentUrl(), baseUrl);
        // assertEquals(driver.findElement(By.id("error-message")).getText(), "Username and password cannot be empty");
    }

    @Test
    public void testPartialCredentials() {  
        attemptLogin(adminUsername, "");

        // Assert login failed by checking we are still on the login page
        assertEquals(driver.getCurrentUrl(), baseUrl);
        // assertEquals(driver.findElement(By.id("error-message")).getText(), "Username and password cannot be empty");

        attemptLogin("", adminPassword);

        // Assert login failed by checking we are still on the login page
        assertEquals(driver.getCurrentUrl(), baseUrl);
        // assertEquals(driver.findElement(By.id("error-message")).getText(), "Username and password cannot be empty");
    }

    @Test
    public void testRegisterLink(){
        driver.get(baseUrl);

        // Click the register link
        var registerLink = driver.findElement(By.cssSelector("a[href='/register']"));
        registerLink.click();

        // Assert we are on the registration page
        wait.until(d -> d.getCurrentUrl().equals("http://localhost:3000/register"));
        assertEquals(driver.getCurrentUrl(), "http://localhost:3000/register");
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
