package pets.database.repository;

import static pets.database.utils.Constants.*;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.RefCategoryType;

@Repository
public class RefCategoryTypeDao {
  private final MongoTemplate mongoTemplate;

  public RefCategoryTypeDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<RefCategoryType> getAllRefCategoryTypes() {
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION));
    return mongoTemplate.find(
        query, RefCategoryType.class, COLLECTION_NAME_REF_CATEGORY_TYPE_DETAILS);
  }

  public RefCategoryType getRefCategoryTypeById(String id) {
    return mongoTemplate.findOne(
        Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
        RefCategoryType.class,
        COLLECTION_NAME_REF_CATEGORY_TYPE_DETAILS);
  }

  public RefCategoryType saveNewRefCategoryType(RefCategoryType refCategoryType) {
    return mongoTemplate.save(refCategoryType, COLLECTION_NAME_REF_CATEGORY_TYPE_DETAILS);
  }

  public long updateRefCategoryTypeById(String id, Update update) {
    return mongoTemplate
        .updateFirst(
            Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
            update,
            RefCategoryType.class,
            COLLECTION_NAME_REF_CATEGORY_TYPE_DETAILS)
        .getModifiedCount();
  }

  public long deleteRefCategoryTypeById(String id) {
    return mongoTemplate
        .remove(
            Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
            RefCategoryType.class,
            COLLECTION_NAME_REF_CATEGORY_TYPE_DETAILS)
        .getDeletedCount();
  }
}
