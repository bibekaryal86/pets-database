package pets.database.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.RefCategory;

import java.util.List;

import static pets.database.utils.Constants.*;

@Repository
public class RefCategoryDao {
    private final MongoTemplate mongoTemplate;

    public RefCategoryDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<RefCategory> getAllRefCategories() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.asc("refCategoryType." + FIELD_NAME_DESCRIPTION)));
        return mongoTemplate.find(query, RefCategory.class, COLLECTION_NAME_REF_CATEGORY_DETAILS);
    }

    public RefCategory getRefCategoryById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), RefCategory.class,
                COLLECTION_NAME_REF_CATEGORY_DETAILS);
    }

    public RefCategory saveNewRefCategory(RefCategory refAccountType) {
        return mongoTemplate.save(refAccountType, COLLECTION_NAME_REF_CATEGORY_DETAILS);
    }

    public long updateRefCategoryById(String id, Update update) {
        return mongoTemplate.updateFirst(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), update, RefCategory.class,
                COLLECTION_NAME_REF_CATEGORY_DETAILS).getModifiedCount();
    }

    public long deleteRefCategoryById(String id) {
        return mongoTemplate.remove(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), RefCategory.class,
                COLLECTION_NAME_REF_CATEGORY_DETAILS).getDeletedCount();
    }
}
