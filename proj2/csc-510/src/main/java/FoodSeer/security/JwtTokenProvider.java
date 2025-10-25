package FoodSeer.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.nio.charset.StandardCharsets;

/**
 * Provides a token for the user.
 */
@Component
public class JwtTokenProvider {

	/** Pulls secret from application.properties */
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    /** Pulls experiation of user login from application.properties */
    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    /**
     * Generates the token
     * @param authentication authentication object
     * @return the generated token
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);


        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        // jwtSecret in properties might be base64, hex, or plain text.
        try {
            // Try base64 first
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        } catch ( final Exception e ) {
            // If it's hex, decode hex string to bytes
            if ( jwtSecret.matches("[0-9a-fA-F]+") && (jwtSecret.length() % 2 == 0) ) {
                final int len = jwtSecret.length();
                final byte[] data = new byte[len / 2];
                for ( int i = 0; i < len; i += 2 ) {
                    data[i / 2] = (byte) ( (Character.digit(jwtSecret.charAt(i), 16) << 4)
                            + Character.digit(jwtSecret.charAt(i + 1), 16) );
                }
                return Keys.hmacShaKeyFor(data);
            }

            // Fallback to raw bytes of the string
            return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Returns the username
     * @param token token to use for authentication
     * @return the username that is authenticated
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); //username

    }

    /**
     * Checks the token is valid.
     * @param token token to check
     * @return true if valid
     */
    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
        return true;
    }
}
