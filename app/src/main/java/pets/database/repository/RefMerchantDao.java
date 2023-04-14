package pets.database.repository;

import static pets.database.utils.Constants.*;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.RefMerchant;

@Repository
public class RefMerchantDao {
  private final MongoTemplate mongoTemplate;

  public RefMerchantDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<RefMerchant> getAllRefMerchants() {
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION));
    return mongoTemplate.find(query, RefMerchant.class, COLLECTION_NAME_REF_MERCHANT_DETAILS);
  }

  public List<RefMerchant> getAllRefMerchantsByUsername(String username) {
    return mongoTemplate.find(
        Query.query(Criteria.where("user." + FIELD_NAME_USERNAME).is(username))
            .with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION)),
        RefMerchant.class,
        COLLECTION_NAME_REF_MERCHANT_DETAILS);
  }

  public RefMerchant getRefMerchantById(String id) {
    return mongoTemplate.findOne(
        Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
        RefMerchant.class,
        COLLECTION_NAME_REF_MERCHANT_DETAILS);
  }

  public RefMerchant saveNewRefMerchant(RefMerchant refMerchant) {
    return mongoTemplate.save(refMerchant, COLLECTION_NAME_REF_MERCHANT_DETAILS);
  }

  public long updateRefMerchantById(String id, Update update) {
    return mongoTemplate
        .updateFirst(
            Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
            update,
            RefMerchant.class,
            COLLECTION_NAME_REF_MERCHANT_DETAILS)
        .getModifiedCount();
  }

  public long deleteRefMerchantById(String id) {
    return mongoTemplate
        .remove(
            Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
            RefMerchant.class,
            COLLECTION_NAME_REF_MERCHANT_DETAILS)
        .getDeletedCount();
  }
}
