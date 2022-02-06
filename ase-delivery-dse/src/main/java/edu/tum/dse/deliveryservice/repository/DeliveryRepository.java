package edu.tum.dse.deliveryservice.repository;

import edu.tum.dse.deliveryservice.model.Delivery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeliveryRepository extends MongoRepository<Delivery, String> {

    List<Delivery> getAllByTargetBox(String box_id);

    List<Delivery> findAllByCustomer_IdOrDeliverer_Id(String customer_id, String deliverer_id);

}
