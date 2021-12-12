package pets.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.RefAccountType;

import java.util.List;

import static pets.database.utils.Constants.*;

@Repository
public class RefAccountTypeDao {
    private final MongoTemplate mongoTemplate;

    public RefAccountTypeDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<RefAccountType> getAllRefAccountTypes() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION));
        return mongoTemplate.find(query, RefAccountType.class, COLLECTION_NAME_REF_ACCOUNT_TYPE_DETAILS);
    }

    public RefAccountType getRefAccountTypeById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), RefAccountType.class,
                COLLECTION_NAME_REF_ACCOUNT_TYPE_DETAILS);
    }

    public RefAccountType saveNewRefAccountType(RefAccountType refAccountType) {
        return mongoTemplate.save(refAccountType, COLLECTION_NAME_REF_ACCOUNT_TYPE_DETAILS);
    }

    public long updateRefAccountTypeById(String id, Update update) {
        return mongoTemplate.updateFirst(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), update,
                RefAccountType.class, COLLECTION_NAME_REF_ACCOUNT_TYPE_DETAILS).getModifiedCount();
    }

    public long deleteRefAccountTypeById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), RefAccountType.class,
                COLLECTION_NAME_REF_ACCOUNT_TYPE_DETAILS).getDeletedCount();
    }
}
