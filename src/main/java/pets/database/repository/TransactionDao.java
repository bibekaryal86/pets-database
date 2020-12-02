package pets.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.Transaction;

import java.util.List;

import static pets.database.utils.Constants.*;

@Repository
public class TransactionDao {
    private final MongoTemplate mongoTemplate;

    public TransactionDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Transaction> getAllTransactions() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "date"));
        return mongoTemplate.findAll(Transaction.class, COLLECTION_NAME_TRANSACTION_DETAILS);
    }

    public Transaction getTransactionById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), Transaction.class,
                COLLECTION_NAME_TRANSACTION_DETAILS);
    }

    public List<Transaction> getTransactionsByUser(String username) {
        return mongoTemplate.find(Query.query(Criteria.where("user." + FIELD_NAME_USERNAME).is(username))
                        .with(Sort.by(Sort.Direction.DESC, "date")), Transaction.class,
                COLLECTION_NAME_TRANSACTION_DETAILS);
    }

    public Transaction saveNewTransaction(Transaction transaction) {
        return mongoTemplate.save(transaction, COLLECTION_NAME_TRANSACTION_DETAILS);
    }

    public long updateTransactionById(String id, Update update) {
        return mongoTemplate.updateFirst(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), update, Transaction.class,
                COLLECTION_NAME_TRANSACTION_DETAILS).getModifiedCount();
    }

    public long deleteTransactionById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), Transaction.class,
                COLLECTION_NAME_TRANSACTION_DETAILS).getDeletedCount();
    }

    public long deleteTransactionsByAccountId(String accountId) {
        return mongoTemplate.remove(Query.query(Criteria.where("account.id").is(accountId)), Transaction.class,
                COLLECTION_NAME_TRANSACTION_DETAILS).getDeletedCount();
    }
}
