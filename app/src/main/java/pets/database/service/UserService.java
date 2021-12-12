package pets.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pets.database.model.Status;
import pets.database.model.User;
import pets.database.model.UserRequest;
import pets.database.model.UserResponse;
import pets.database.repository.UserDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.hasText;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String ERROR_RETRIEVING_USER = "Error Retrieving User, Please Try Again!!!";
    private static final String ERROR_UPDATING_USER = "Error Updating User, Please Try Again!!!";

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserResponse getAllUsers() {
        logger.info("Before Get All Users");
        List<User> allUsers = new ArrayList<>();
        Status status = null;

        try {
            allUsers = userDao.getAllUsers();
        } catch (Exception ex) {
            logger.error("Get All Users", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Users, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get All Users: {}", allUsers.size());
        return UserResponse.builder()
                .users(allUsers)
                .status(status)
                .build();
    }

    public UserResponse getUserById(String id) {
        logger.info("Before Get User By Id: {}", id);
        User user = null;
        Status status = null;

        try {
            user = userDao.findUserById(id);
        } catch (Exception ex) {
            logger.error("Get User By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_USER)
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get User By Id: {} | {}", id, user);
        return UserResponse.builder()
                .users(user == null ? emptyList() : singletonList(user))
                .status(status)
                .build();
    }

    public UserResponse getUserByUsername(String username) {
        logger.info("Before Get User By User Name: {}", username);
        User user = null;
        Status status = null;

        try {
            user = userDao.findUserByUsername(username);
        } catch (Exception ex) {
            logger.error("Get User By User Name: {}", username, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_USER)
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get User By User Name: {} | {}", username, user);
        return UserResponse.builder()
                .users(user == null ? emptyList() : singletonList(user))
                .status(status)
                .build();
    }

    public UserResponse getUsersByEmailOrPhone(String email, String phone) {
        logger.info("Before Get User By Email Or Phone: {} | {}", email, phone);
        User user = null;
        Status status = null;

        try {
            user = userDao.findByEmailOrPhone(email, phone);
        } catch (Exception ex) {
            logger.error("Get User By Email Or Phone: {} | {}", email, phone, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_USER)
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get User By Email Or Phone: {} | {} | {}", email, phone, user);
        return UserResponse.builder()
                .users(user == null ? emptyList() : singletonList(user))
                .status(status)
                .build();
    }

    public UserResponse saveNewUser(UserRequest userRequest) {
        logger.info("Before Save New User: {}", userRequest);
        User newUser;
        Status status = null;

        try {
            newUser = User.builder()
                    .username(userRequest.getUsername())
                    .password(userRequest.getPassword())
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .streetAddress(userRequest.getStreetAddress())
                    .city(userRequest.getCity())
                    .state(userRequest.getState())
                    .zipcode(userRequest.getZipcode())
                    .email(userRequest.getEmail())
                    .phone(userRequest.getPhone())
                    .status(userRequest.getStatus())
                    .creationDate(LocalDate.now().toString())
                    .lastModified(LocalDateTime.now().toString())
                    .build();

            newUser = userDao.saveNewUser(newUser);

            if (!hasText(newUser.getId())) {
                newUser = null;
                status = Status.builder()
                        .errMsg("Error Saving User, Please Try Again!!!")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Save New User: {}", userRequest, ex);
            newUser = null;
            status = Status.builder()
                    .errMsg("Error Saving User, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Save New User: {}", newUser);
        return UserResponse.builder()
                .users(newUser == null ? emptyList() : singletonList(newUser))
                .status(status)
                .build();
    }

    public UserResponse updateUserById(String id, UserRequest userRequest) {
        logger.info("Before Update User By Id: {} | {}", id, userRequest);
        UserResponse userResponse;
        Status status;

        try {
            Update update = new Update();
            if (hasText(userRequest.getFirstName())) {
                update.set("first_name", userRequest.getFirstName());
            }
            if (hasText(userRequest.getLastName())) {
                update.set("last_name", userRequest.getLastName());
            }
            if (hasText(userRequest.getStreetAddress())) {
                update.set("street_address", userRequest.getStreetAddress());
            }
            if (hasText(userRequest.getCity())) {
                update.set("city", userRequest.getCity());
            }
            if (hasText(userRequest.getState())) {
                update.set("state", userRequest.getState());
            }
            if (hasText(userRequest.getZipcode())) {
                update.set("zip_code", userRequest.getZipcode());
            }
            update.set("lastModified", LocalDateTime.now().toString());

            long modifiedCount = userDao.updateUserById(id, update);

            if (modifiedCount > 0) {
                userResponse = getUserById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_USER)
                        .build();
                userResponse = UserResponse.builder()
                        .users(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Update User By Id: {} | {}", id, userRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_USER)
                    .message(ex.toString())
                    .build();
            userResponse = UserResponse.builder()
                    .users(emptyList())
                    .status(status)
                    .build();
        }

        logger.info("After Update User By Id: {} | user: {}", id, userResponse);
        return userResponse;
    }

    public UserResponse updateUserById(String id, Map<String, Object> newValues) {
        logger.info("Before Update User By Id: {} | {}", id, newValues);
        UserResponse userResponse;
        Status status;

        try {
            Update update = new Update();
            newValues.forEach(update::set);
            update.set("lastModified", LocalDateTime.now().toString());

            long modifiedCount = userDao.updateUserById(id, update);

            if (modifiedCount > 0) {
                userResponse = getUserById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_USER)
                        .build();
                userResponse = UserResponse.builder()
                        .users(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Update User By Id: {} | {}", id, newValues, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_USER)
                    .message(ex.toString())
                    .build();
            userResponse = UserResponse.builder()
                    .users(emptyList())
                    .status(status)
                    .build();
        }

        logger.info("After Update User By Id: {} | user: {}", id, userResponse);
        return userResponse;
    }

    public UserResponse deleteUserById(String id) {
        logger.info("Before Delete User By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            deleteCount = userDao.deleteUserById(id);
        } catch (Exception ex) {
            logger.error("Delete User By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Deleting User, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Delete User By Id: {} | deleteCount: {}", id, deleteCount);
        return UserResponse.builder()
                .users(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
