package pets.database.repository;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static support.FixtureReader.readFixture;
import static support.ObjectMapperProvider.objectMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import org.bson.BsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import pets.database.model.User;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {
  @Mock private MongoTemplate mongoTemplate;

  private UserDao userDao;
  private User expectedUser;

  @BeforeEach
  void init() throws JsonProcessingException {
    userDao = new UserDao(mongoTemplate);

    String userString = readFixture("fixtures/user/user.json");
    expectedUser = objectMapper().readValue(userString, User.class);
  }

  @Test
  void getAllUsers_returnsUsers() {
    List<User> expectedUserList =
        asList(
            User.builder().username("user-name-01").build(),
            User.builder().username("user-name-02").build());

    when(mongoTemplate.findAll(User.class)).thenReturn(expectedUserList);

    List<User> actualUserList = userDao.getAllUsers();

    assertEquals(expectedUserList, actualUserList);
    verify(mongoTemplate, times(1)).findAll(User.class);
  }

  @Test
  void findUserById_returnsUser() {
    when(mongoTemplate.findOne(any(), eq(User.class))).thenReturn(expectedUser);

    User actualUser = userDao.findUserById("id");

    assertEquals(expectedUser, actualUser);
    verify(mongoTemplate, times(1)).findOne(any(), any());
  }

  @Test
  void findUserByUsername_returnsUser() {
    when(mongoTemplate.findOne(any(), eq(User.class))).thenReturn(expectedUser);

    User actualUser = userDao.findUserByUsername("username");

    assertEquals(expectedUser, actualUser);
    verify(mongoTemplate, times(1)).findOne(any(), any());
  }

  @Test
  void findByEmailOrPhone_findsByEmail_returnsUser() {
    when(mongoTemplate.findOne(any(), eq(User.class))).thenReturn(expectedUser);

    User actualUser = userDao.findByEmailOrPhone("email", null);

    assertEquals(expectedUser, actualUser);
    verify(mongoTemplate, times(1)).findOne(any(), any());
  }

  @Test
  void findByEmailOrPhone_findsByPhone_returnsUser() {
    when(mongoTemplate.findOne(any(), eq(User.class))).thenReturn(expectedUser);

    User actualUser = userDao.findByEmailOrPhone("", "phone");

    assertEquals(expectedUser, actualUser);
    verify(mongoTemplate, times(1)).findOne(any(), any());
  }

  @Test
  void saveNewUser_returnsUser() {
    when(mongoTemplate.save(any(), anyString())).thenReturn(expectedUser);

    User actualUser = userDao.saveNewUser(expectedUser);

    assertEquals(expectedUser, actualUser);
    verify(mongoTemplate, times(1)).save(any(), any());
  }

  @Test
  void updateUserById_returnsUpdateCount() {
    UpdateResult updateResult =
        new UpdateResult() {
          public boolean wasAcknowledged() {
            return false;
          }

          public long getMatchedCount() {
            return 0;
          }

          public long getModifiedCount() {
            return 1;
          }

          public BsonValue getUpsertedId() {
            return null;
          }
        };

    when(mongoTemplate.updateFirst(any(), any(), eq(User.class))).thenReturn(updateResult);

    long actualRowsUpdated = userDao.updateUserById("id", new Update());

    assertEquals(1, actualRowsUpdated);
    verify(mongoTemplate, times(1)).updateFirst(any(), any(), eq(User.class));
  }

  @Test
  void deleteUserById_returnsDeleteCount() {
    DeleteResult deleteResult =
        new DeleteResult() {
          public boolean wasAcknowledged() {
            return false;
          }

          public long getDeletedCount() {
            return 1;
          }
        };

    when(mongoTemplate.remove(any(), eq(User.class))).thenReturn(deleteResult);

    long actualRowsDeleted = userDao.deleteUserById("id");

    assertEquals(1, actualRowsDeleted);
    verify(mongoTemplate, times(1)).remove(any(), eq(User.class));
  }
}
