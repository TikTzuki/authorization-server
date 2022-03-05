package authorizationserver.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwt;
import lombok.extern.java.Log;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import authorizationserver.config.YAMLConfig;
import authorizationserver.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.server.ResponseStatusException;

@Service
@Log
public class JwtUtil {
    @Autowired
    UserRepository userRepository;

    @Autowired
    YAMLConfig config;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(config.getBase64UrlSecret())
                .parseClaimsJws(token).getBody();
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String audience) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", userRepository.findByUserName(username).getScope());
        return createToken(claims, username, audience);
    }

    private String createToken(Map<String, Object> claims, String subject, String audience) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(config.getAddress())
                .setAudience(audience)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, config.getBase64UrlSecret()).compact();
    }

    /**
     * Kiem tra username, expiration
     */
    public void validateToken(Map<String, String> headers, Function<String, Boolean> isExistsUserResolver) {
        String token = getTokenFromHeaders(headers);
        logToken(token);
        final String username = extractUsername(token);
        if (!isExistsUserResolver.apply(username) && isTokenExpired(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public String getTokenFromHeaders(Map<String, String> headers) {
        String authorizationHeader = headers.get("authorization");
        if (authorizationHeader == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authorization header required");
        return authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;
    }

    private void logToken(String token) {
        Jwt jwt = Jwts.parser()
                .setSigningKey(config.getBase64UrlSecret())
                .parseClaimsJws(token);
        log.info(jwt.toString());
    }
}
