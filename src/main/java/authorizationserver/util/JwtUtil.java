package authorizationserver.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import authorizationserver.config.YAMLConfig;
import authorizationserver.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
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
				.setIssuer(config.getAddress() +":"+config.getPort())
				.setAudience(audience)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10))
				.signWith(SignatureAlgorithm.HS256, config.getBase64UrlSecret()).compact();
	}
	
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		System.out.println(username);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
