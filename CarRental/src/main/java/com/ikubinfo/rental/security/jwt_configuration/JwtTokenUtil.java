package com.ikubinfo.rental.security.jwt_configuration;

import com.ikubinfo.rental.entity.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    private LinkedHashMap<?, ?> role;

    private String username;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public LinkedHashMap<?, ?> getRoleFromToken(String token) {
        return (LinkedHashMap<?, ?>) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("role");
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }


    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public String generateToken(UserDetails userDetails, RoleEntity role) {
        return doGenerateToken(userDetails.getUsername(), role);
    }


    private String doGenerateToken(String username, RoleEntity role) {
        Claims customClaims = Jwts.claims();
        customClaims.put("username", username);
        customClaims.put("role", role);
        return Jwts.builder().setClaims(customClaims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        this.username = getUsernameFromToken(token);
        this.role = getRoleFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsername() {
        return this.username;
    }

    public LinkedHashMap<?, ?> getRole() {
        return this.role;
    }
}
