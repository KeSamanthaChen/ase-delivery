package edu.tum.dse.deliveryservice.repository;

import edu.tum.dse.deliveryservice.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updateAccount(Account account) {
        Query query = new Query().addCriteria(where("username").is(account.getUsername()));
        Update update = new Update();
        update.set("email", account.getEmail());
        mongoTemplate.update(Account.class).matching(query).apply(update).first();
    }
}
