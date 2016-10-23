package soselab.easylearn.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${cerberus.token.secret}")
    private String secret;

    @Value("${cerberus.token.expiration}")
    private Long expiration;

    public String getUserIdFromToken(String token) {
        String id;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            id = claims.getSubject();
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateToken(String id) {
        return Jwts.builder()
                .setIssuer("Easylearn")
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, this.secret)
                .compact();
    }

    public Boolean validateToken(String token, String userId) {
        final String id = this.getUserIdFromToken(token);
        return (id.equals(userId) && !(this.isTokenExpired(token)));
    }

}
