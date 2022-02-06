package edu.tum.dse.deliveryservice;

import edu.tum.dse.deliveryservice.client.AuthClient;
import edu.tum.dse.deliveryservice.jwt.KeyManager;
import edu.tum.dse.deliveryservice.model.*;
import edu.tum.dse.deliveryservice.repository.AccountRepository;
import edu.tum.dse.deliveryservice.service.BoxService;
import edu.tum.dse.deliveryservice.service.DeliveryService;
import edu.tum.dse.deliveryservice.service.EmailService;
import edu.tum.dse.deliveryservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class DeliveryServiceApplication implements CommandLineRunner {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private KeyManager keyManager;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BoxService boxService;

    @Autowired
    private EmailService emailService;


    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        byte[] publicKeyEncoded = this.authClient.getPublicKey();
        this.keyManager.setPublicKey(publicKeyEncoded);

//       Account dispatcher = new Account(UserRole.DISPATCHER, "dispatcher@dispatcher.com");
//       Account deliverer = new Account(UserRole.DELIVERER, "deliverer@dispatcher.com");
//       Account customer = new Account(UserRole.CUSTOMER, "dispatcher@dispatcher.com");
//       Account customer2 = new Account(UserRole.CUSTOMER + "2", "dispatcher@dispatcher.com");
//
//       Box box = new Box("my-box", "my box");
//
//       Delivery delivery = new Delivery(customer, deliverer, box);
//
//       if (this.accountRepository.findAll().size() == 0) {
//           this.accountRepository.save(dispatcher);
//           this.accountRepository.save(deliverer);
//           this.accountRepository.save(customer);
//           this.accountRepository.save(customer2);
//
//           this.boxService.createBox(box);
//           this.deliveryService.createDelivery(delivery);
//       }


    }


}
