package edu.tum.dse.deliveryservice.jwt;

import edu.tum.dse.deliveryservice.client.AuthClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

public class PublicKeyCrawler implements Runnable {


    private KeyManager keyManager;
    private DiscoveryClient discoveryClient;
    private AuthClient authClient;

    public PublicKeyCrawler(KeyManager keyManager, DiscoveryClient discoveryClient, AuthClient authClient) {
        this.keyManager = keyManager;
        this.discoveryClient = discoveryClient;
        this.authClient = authClient;
    }

    @Override
    public void run() {
        boolean publicKeyReceived = false;
        // Recovery mechanism if due to race condition the auth service is not registered
        while (!publicKeyReceived) {

            List<ServiceInstance> instances = this.discoveryClient.getInstances("authentication-service");
            if (!instances.isEmpty()) {
                byte[] publicKeyEncoded = this.authClient.getPublicKey();
                this.keyManager.setPublicKey(publicKeyEncoded);
                publicKeyReceived = true;
                System.out.println("Received public key from AUTHENTICATION-SERVICE!");
            } else {
                System.out.println("Waiting for public key from AUTHENTICATION-SERVICE...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
