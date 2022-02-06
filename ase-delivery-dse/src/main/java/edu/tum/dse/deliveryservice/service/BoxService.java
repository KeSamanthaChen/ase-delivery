package edu.tum.dse.deliveryservice.service;

import edu.tum.dse.deliveryservice.exceptions.BoxNotAvailableException;
import edu.tum.dse.deliveryservice.model.Account;
import edu.tum.dse.deliveryservice.model.Box;
import edu.tum.dse.deliveryservice.repository.AccountRepository;
import edu.tum.dse.deliveryservice.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoxService {

    @Autowired
    BoxRepository boxRepository;

    @Autowired
    AccountRepository accountRepository;

    public boolean authBoxWithAccount(String boxId, String rfid) {
        Account user = accountRepository.findAccountByRfidToken(rfid);
        Box box = boxRepository.findById(boxId).orElse(null);
        if (box != null && user != null) {
            String username = user.getUsername();
            Box.BoxStateEnum boxState = box.getState();
            if (boxState == Box.BoxStateEnum.ASSIGNED && username.equals(box.getDelivererName())) {
                return true;
            } else if (boxState == Box.BoxStateEnum.FILLED && username.equals(box.getCustomerName())) {
                return true;
            }
        }
        return false;
    }

    public Box updateBox(Box box) {
        return boxRepository.save(box);
    }

    public Box createBox(Box box) {
        return boxRepository.insert(box);
    }

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public Box getBoxById(String id) {
        return boxRepository.findById(id).orElse(null);
    }

    public void deleteBox(String id) throws BoxNotAvailableException {
        Optional<Box> optionalBox = this.boxRepository.findById(id);
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            // Allow only to delete boxes without assigned or filled deliveries
            if(box.getState() == Box.BoxStateEnum.AVAILABLE) {
                this.boxRepository.deleteById(id);
            } else {
                throw new BoxNotAvailableException();
            }
        }
    }

}
