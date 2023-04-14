package pets.database.repository;

import static pets.database.utils.Constants.*;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.RefBank;

@Repository
public class RefBankDao {
  private final MongoTemplate mongoTemplate;

  public RefBankDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<RefBank> getAllRefBanks() {
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIPTION));
    return mongoTemplate.find(query, RefBank.class, COLLECTION_NAME_REF_BANK_DETAILS);
  }

  public RefBank getRefBankById(String id) {
    return mongoTemplate.findOne(
        Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
        RefBank.class,
        COLLECTION_NAME_REF_BANK_DETAILS);
  }

  public RefBank saveNewRefBank(RefBank refAccountType) {
    return mongoTemplate.save(refAccountType, COLLECTION_NAME_REF_BANK_DETAILS);
  }

  public long updateRefBankById(String id, Update update) {
    return mongoTemplate
        .updateFirst(
            Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
            update,
            RefBank.class,
            COLLECTION_NAME_REF_BANK_DETAILS)
        .getModifiedCount();
  }

  public long deleteRefBankById(String id) {
    return mongoTemplate
        .remove(
            Query.query(Criteria.where(FIELD_NAME_ID).is(id)),
            RefBank.class,
            COLLECTION_NAME_REF_BANK_DETAILS)
        .getDeletedCount();
  }
}
