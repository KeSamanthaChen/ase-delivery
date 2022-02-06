package edu.tum.dse.deliveryservice.repository;

import edu.tum.dse.deliveryservice.model.Account;

public interface CustomAccountRepository {

    /**
     Update only email and role of an given account by username
     **/

    void updateAccount(Account account);
}
