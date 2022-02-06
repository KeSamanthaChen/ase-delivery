package edu.tum.ase.authenticationservice.controller;

import edu.tum.ase.authenticationservice.jwt.KeyStoreManager;
import edu.tum.ase.authenticationservice.model.AseDeliveryUser;
import edu.tum.ase.authenticationservice.service.AuthService;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;
import java.security.cert.Certificate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/current-session")
    public AseDeliveryUser getCurrentUserSession() {
        return authService.getCurrentSession();
    }

    @GetMapping("/csrf")
    public ResponseEntity<String> getCSRFToken() {
        return new ResponseEntity<>("CSRF token received", HttpStatus.OK);
    }

    @GetMapping("/public-key")
    public byte[] getPublicKey() {
        return authService.getPublicKey();
    }

    @PostMapping
    public ResponseEntity<String> authenticate(HttpServletRequest request, HttpServletResponse response) {
        return this.authService.authenticateUser(request, response);
    }

}
