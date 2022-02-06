package edu.tum.ase.authenticationservice.repositary;

import edu.tum.ase.authenticationservice.model.AseDeliveryUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<AseDeliveryUser, String> {

    AseDeliveryUser findUserByUsername(String username);

    List<AseDeliveryUser> findAllByRole(String role);

    void deleteByUsername(String username);
}
