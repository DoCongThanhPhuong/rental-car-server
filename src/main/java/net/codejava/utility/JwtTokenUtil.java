package net.codejava.utility;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import net.codejava.domain.entity.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtil {
    private final String BEARER_TOKEN = "Bearer ";

    @NonFinal
    @Value("${app.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${app.jwt.expire-seconds}")
    private Long JWT_EXPIRE_SECONDS;

    @Value("${app.jwt.issuer}")
    private String ISSUER;

    public String generateToken(User user) {
        Map<String, Object> map = Map.ofEntries(Map.entry("id", user.getId().toString()));

        return Jwts.builder()
                .setClaims(map)
                .setSubject(user.getEmail())
                .setIssuer(ISSUER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now()
                        .plus(JWT_EXPIRE_SECONDS, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            final Claims claims = this.extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error(ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error(ex.getMessage());
        } catch (SignatureException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public String getSubject(String token) {
        return this.extractAllClaims(token).getSubject();
    }

    public String getAccountId(String bearerToken) {
        return (String) this.extractAllClaims(bearerToken.substring(BEARER_TOKEN.length()))
                .get("id");
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
