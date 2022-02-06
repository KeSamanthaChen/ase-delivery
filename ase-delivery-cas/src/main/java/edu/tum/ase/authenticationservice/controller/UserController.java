package edu.tum.ase.authenticationservice.controller;

import edu.tum.ase.authenticationservice.model.AseDeliveryUser;
import edu.tum.ase.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/credentials")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<String> getUsersByRole(@RequestParam String role) {
        return this.userService.getUsersByRole(role).stream().map(AseDeliveryUser::getUsername).collect(Collectors.toList());
    }


    @GetMapping("/{username}/role")
    public ResponseEntity<String> getUserRole(@PathVariable String username) {
        AseDeliveryUser aseDeliveryUser = this.userService.getUser(username);
        if (aseDeliveryUser != null) {
            return new ResponseEntity<>(aseDeliveryUser.getRole(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public AseDeliveryUser createUser(@RequestBody AseDeliveryUser user) {
        return this.userService.saveUser(user);
    }

    @PutMapping("")
    public AseDeliveryUser updateUser(@RequestBody AseDeliveryUser user) {
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        this.userService.deleteUser(username);
    }
}
