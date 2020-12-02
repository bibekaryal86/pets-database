package pets.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.Account;

import java.util.List;

import static pets.database.utils.Constants.*;

@Repository
public class AccountDao {
    private final MongoTemplate mongoTemplate;

    public AccountDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Account> getAllAccounts() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION));
        return mongoTemplate.find(query, Account.class, COLLECTION_NAME_ACCOUNT_DETAILS);
    }

    public Account getAccountById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), Account.class,
                COLLECTION_NAME_ACCOUNT_DETAILS);
    }

    public List<Account> getAllAccountsByUsername(String username) {
        return mongoTemplate.find(Query.query(Criteria.where("user." + FIELD_NAME_USERNAME)
                        .is(username)).with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION)), Account.class,
                COLLECTION_NAME_ACCOUNT_DETAILS);
    }

    public Account saveNewAccount(Account account) {
        return mongoTemplate.save(account, COLLECTION_NAME_ACCOUNT_DETAILS);
    }

    public long updateAccountById(String id, Update update) {
        return mongoTemplate.updateFirst(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), update, Account.class,
                COLLECTION_NAME_ACCOUNT_DETAILS).getModifiedCount();
    }

    public long deleteAccountById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), Account.class,
                COLLECTION_NAME_ACCOUNT_DETAILS).getDeletedCount();
    }
}
