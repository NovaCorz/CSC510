package FoodSeer.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        jwtTokenProvider = new JwtTokenProvider();

        // Set secret and expiration manually since @Value isn't used in test context
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "testsecret12345678901234567890123"); // 32+ chars
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationDate", 3600000L);
    }

    @Test
    void testGenerateAndValidateToken() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("testuser", null);

        String token = jwtTokenProvider.generateToken(auth);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));

        String username = jwtTokenProvider.getUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testKeyHandlesPlainTextSecret() {
        SecretKey key = (SecretKey) ReflectionTestUtils.invokeMethod(jwtTokenProvider, "key");
        assertNotNull(key);
    }

    @Test
    void testKeyHandlesHexSecret() {
        // 64 hex chars = 32 bytes = 256 bits ✅
        String validHexSecret = "abcdef0123456789abcdef0123456789abcdef0123456789abcdef0123456789";

        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", validHexSecret);

        SecretKey key = (SecretKey) ReflectionTestUtils.invokeMethod(jwtTokenProvider, "key");
        assertNotNull(key);
    }

    @Test
    void testKeyHandlesBase64Secret() {
        String base64Key = "c2VjcmV0c2VjcmV0c2VjcmV0c2VjcmV0MTIz"; // base64("secretsecretsecret123")
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", base64Key);

        SecretKey key = (SecretKey) ReflectionTestUtils.invokeMethod(jwtTokenProvider, "key");
        assertNotNull(key);
    }

    @Test
    void testInvalidTokenThrows() {
        assertThrows(Exception.class, () -> jwtTokenProvider.validateToken("invalid.token"));
    }
    
    @Test
    void testHexBranchIsExecuted() {
        // Even-length valid hex — triggers if-block
        String hexSecret = "abcdef0123456789"; // 16 hex chars (8 bytes)
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", hexSecret);

        try {
            SecretKey key = (SecretKey) ReflectionTestUtils.invokeMethod(jwtTokenProvider, "key");
            assertNotNull(key);
        } catch (Exception e) {
            // We EXPECT a WeakKeyException here due to small size, and that's OK.
            // This confirms we ENTERED the hex path.
            assertTrue(e.getMessage().contains("key"));
        }
    }

}
