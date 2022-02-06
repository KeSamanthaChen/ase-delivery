package edu.tum.ase.authenticationservice.jwt;

import com.google.gson.internal.LinkedTreeMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JWTUtil {

    @Autowired
    private KeyStoreManager keyStoreManager;

    private final static String ISSUER = "aseDeliveryCASService";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put("roles", authorities);
        return createToken(claims, userDetails.getUsername());
    }

    // Create JWS with both custom and registered claims, signed by
    // a private key.
    private String createToken(Map<String, Object> claims, String subject) {
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5)) // Expires after 5 hours
                .signWith(keyStoreManager.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
        return jwt;
    }

    private JwtParser loadJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(keyStoreManager.getPublicKey())
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
        return this.keyStoreManager.getPublicKey();
    }



}
