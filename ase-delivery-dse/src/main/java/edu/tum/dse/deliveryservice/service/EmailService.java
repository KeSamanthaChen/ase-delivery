package edu.tum.dse.deliveryservice.service;

import edu.tum.dse.deliveryservice.model.Account;
import edu.tum.dse.deliveryservice.model.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private void sendMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ase.delivery.team.8.2021@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sentDeliveryCreatedMail(Account account, Delivery delivery) {
        String text = "A new delivery was created and will arrive soon!";
        this.sendMessage(account.getEmail(), "Delivery created", text);
    }

    public void sentDeliveryInsideBoxMail(Account account, Delivery delivery)  {
        String text = "The delivery was just placed in the box at " + delivery.getTargetBox().getAddress()  + "! You can pick it up!";
        this.sendMessage(account.getEmail(), "Delivery can be picked up", text);
    }

    public void sentDeliveriesCollected(Account account, List<Delivery> deliveries) {
        String text = "You collected your deliveries!";
        this.sendMessage(account.getEmail(), "Delivery collected", text);
    }
}
