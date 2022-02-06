package edu.tum.ase.authenticationservice.service;

import edu.tum.ase.authenticationservice.jwt.JWTUtil;
import edu.tum.ase.authenticationservice.model.AseDeliveryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;


    public ResponseEntity<String> authenticateUser(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader("Authorization");
        String base64EncodedUserCredentials = header.replace("Basic ", "");
        byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedUserCredentials);
        String[] userCredentials = new String(decodedBytes).split(":");
        String username = userCredentials[0];
        String password = userCredentials[1];

        UserDetails userDetails;

        try {
            userDetails = this.userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return this.sendError();
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            final String jwt = jwtUtil.generateToken(userDetails);

            Cookie jwtCookie = this.configureJwtCookie(jwt);
            response.addCookie(jwtCookie);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AuthenticationException e) {
            return this.sendError();
        }
    }

    private Cookie configureJwtCookie(String jwt) {
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // TODO configure to https?
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration

        return jwtCookie;
    }

    private ResponseEntity<String> sendError() {
        return new ResponseEntity<>("Authentication went wrong", HttpStatus.UNAUTHORIZED);
    }

    public byte[] getPublicKey() {
        return jwtUtil.getPublicKey().getEncoded();
    }

    public AseDeliveryUser getCurrentSession() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        AseDeliveryUser user = this.userService.getUser(username);
        user.setPassword(null);
        return user;
    }

}
