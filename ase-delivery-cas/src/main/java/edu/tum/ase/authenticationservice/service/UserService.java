package edu.tum.ase.authenticationservice.service;

import edu.tum.ase.authenticationservice.model.AseDeliveryUser;
import edu.tum.ase.authenticationservice.repositary.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AseDeliveryUser aseDeliveryUser = this.userRepository.findUserByUsername(username);
        if (aseDeliveryUser != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(aseDeliveryUser.getRole()));

            return new User(aseDeliveryUser.getUsername(), aseDeliveryUser.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Username not found!");
        }
    }

    public List<AseDeliveryUser> getUsers() {
        return this.userRepository.findAll();
    }

    public List<AseDeliveryUser> getUsersByRole(String role) {
        return this.userRepository.findAllByRole(role);
    }

    public AseDeliveryUser getUser(String username) {
        return this.userRepository.findUserByUsername(username);
    }

    public AseDeliveryUser saveUser(AseDeliveryUser user) {
        String encryptedPassword = this.bcryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return this.userRepository.save(user);
    }

    public AseDeliveryUser updateUser(AseDeliveryUser userUpdate) {
        AseDeliveryUser user = this.userRepository.findUserByUsername(userUpdate.getUsername());
        userUpdate.setId(user.getId());
        userUpdate.setPassword(user.getPassword());
        this.userRepository.save(userUpdate);
        userUpdate.setPassword(null);
        return userUpdate;
    }

    public void deleteUser(String username) {
        this.userRepository.deleteByUsername(username);
    }

}
