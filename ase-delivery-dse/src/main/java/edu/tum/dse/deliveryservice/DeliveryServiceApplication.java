package edu.tum.dse.deliveryservice;

import com.netflix.appinfo.InstanceInfo;
import edu.tum.dse.deliveryservice.exceptions.BoxNotAvailableException;
import edu.tum.dse.deliveryservice.jwt.PublicKeyCrawler;
import edu.tum.dse.deliveryservice.repository.DeliveryRepository;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.transport.TransportException;
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
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableEurekaClient
public class DeliveryServiceApplication implements CommandLineRunner {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private KeyManager keyManager;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BoxService boxService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TaskExecutor taskExecutor;


    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }

    @Override
    public void run(String... args) {
        taskExecutor.execute(new PublicKeyCrawler(keyManager, discoveryClient, authClient));
        // some dummy values to fill the database
        if (this.accountRepository.findAll().isEmpty()) {
            Account dispatcher = new Account(UserRole.DISPATCHER, "dispatcher@mail.com");
            dispatcher.setRfidToken("asjk23yxnas");
            Account deliverer = new Account(UserRole.DELIVERER, "deliverer@mail.com");
            dispatcher.setRfidToken("asjklaJE13K");
            Account customer = new Account(UserRole.CUSTOMER, "customer@mail.com");
            customer.setRfidToken("asdhghsdgsdj");
            if (this.accountRepository.findAll().size() == 0) {
                this.accountRepository.save(dispatcher);
                this.accountRepository.save(deliverer);
                this.accountRepository.save(customer);
            }
        }
    }


}
