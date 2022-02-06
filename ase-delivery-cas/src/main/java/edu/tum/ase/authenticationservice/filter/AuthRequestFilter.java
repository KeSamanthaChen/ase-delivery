package edu.tum.ase.authenticationservice.filter;

import edu.tum.ase.authenticationservice.jwt.JWTUtil;
import edu.tum.ase.authenticationservice.service.AuthService;
import edu.tum.ase.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String jwt = null;
        Optional<Cookie> jwtCookie = Optional.empty();

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            jwtCookie = Arrays.stream(cookies).filter((cookie -> "jwt".equals(cookie.getName()))).findAny();
        }


        if (jwtCookie.isPresent()) {
            // If the JWT expires or not signed by us, send an error to the client
            jwt = jwtCookie.get().getValue();
            if (jwt != null) {
                if (jwtUtil.verifyJwtSignature(jwt)) {
                    username = jwtUtil.extractUsername(jwt);
                } else {
                    response.sendError(HttpStatus.BAD_REQUEST.value(), "JWT invalid or expired");
                }
            }
        } else {
                response.sendError(HttpStatus.BAD_REQUEST.value(), "No JWT Token or Basic Auth Info Found");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.createUserDetailsFromJWT(jwt);
            this.setAuthentication(userDetails);
        }
        filterChain.doFilter(request, response);
    }

    private UserDetails createUserDetailsFromJWT(String jwt) {
        String username = jwtUtil.extractUsername(jwt);
        List<GrantedAuthority> authorities = jwtUtil.extractRoles(jwt);
        return new User(username, "", authorities);
    }

    public void setAuthentication(UserDetails userDetails) {
        Authentication auth = new
                UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
