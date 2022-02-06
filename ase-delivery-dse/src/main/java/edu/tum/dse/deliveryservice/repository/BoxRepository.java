package edu.tum.dse.deliveryservice.repository;

import edu.tum.dse.deliveryservice.model.Box;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoxRepository extends MongoRepository<Box, String> {
}
