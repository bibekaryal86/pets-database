package pets.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.RefTransactionType;

import java.util.List;

import static pets.database.utils.Constants.*;

@Repository
public class RefTransactionTypeDao {
    private final MongoTemplate mongoTemplate;

    public RefTransactionTypeDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<RefTransactionType> getAllRefTransactionTypes() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION));
        return mongoTemplate.find(query, RefTransactionType.class, COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS);
    }

    public RefTransactionType getRefTransactionTypeById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), RefTransactionType.class,
                COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS);
    }

    public RefTransactionType saveNewRefTransactionType(RefTransactionType refTransactionType) {
        return mongoTemplate.save(refTransactionType, COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS);
    }

    public long updateRefTransactionTypeById(String id, Update update) {
        return mongoTemplate.updateFirst(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), update,
                RefTransactionType.class, COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS).getModifiedCount();
    }

    public long deleteRefTransactionTypeById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), RefTransactionType.class,
                COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS).getDeletedCount();
    }
}
