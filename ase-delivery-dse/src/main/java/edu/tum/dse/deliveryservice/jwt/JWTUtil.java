package edu.tum.dse.deliveryservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Autowired
    private KeyManager keyManager;

    private final static String ISSUER = "aseDeliveryCASService";


    private JwtParser loadJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(keyManager.getPublicKey())
                .build();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<GrantedAuthority> extractRoles(String token) {
        List<String> authoritiesRaw = extractClaim(token, (claimResolver) -> claimResolver.get("roles", List.class));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authoritiesRaw.forEach((a) -> authorities.add(new SimpleGrantedAuthority(a)));
        return authorities;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return loadJwtParser()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public boolean verifyJwtSignature(String token) {
        return loadJwtParser().isSigned(token) && !isTokenExpired(token);
    }

    public PublicKey getPublicKey() {
        return this.keyManager.getPublicKey();
    }
}
