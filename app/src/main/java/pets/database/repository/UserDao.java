package pets.database.repository;

import static org.springframework.util.StringUtils.hasText;
import static pets.database.utils.Constants.COLLECTION_NAME_USER_DETAILS;
import static pets.database.utils.Constants.FIELD_NAME_ID;

import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import pets.database.model.User;

@Repository
public class UserDao {
  private final MongoTemplate mongoTemplate;

  public UserDao(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public List<User> getAllUsers() {
    return mongoTemplate.findAll(User.class);
  }

  public User findUserById(String id) {
    return mongoTemplate.findOne(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), User.class);
  }

  public User findUserByUsername(String username) {
    return mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), User.class);
  }

  public User findByEmailOrPhone(String email, String phone) {
    if (hasText(email)) {
      return mongoTemplate.findOne(Query.query(Criteria.where("email").is(email)), User.class);
    } else {
      return mongoTemplate.findOne(Query.query(Criteria.where("phone").is(phone)), User.class);
    }
  }

  public User saveNewUser(User newUser) {
    return mongoTemplate.save(newUser, COLLECTION_NAME_USER_DETAILS);
  }

  public long updateUserById(String id, Update update) {
    return mongoTemplate
        .updateFirst(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), update, User.class)
        .getModifiedCount();
  }

  public long deleteUserById(String id) {
    return mongoTemplate
        .remove(Query.query(Criteria.where(FIELD_NAME_ID).is(id)), User.class)
        .getDeletedCount();
  }
}
