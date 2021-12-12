package pets.database.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pets.database.model.Status;
import pets.database.model.User;
import pets.database.model.UserRequest;
import pets.database.model.UserResponse;
import pets.database.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static support.FixtureReader.readFixture;
import static support.ObjectMapperProvider.objectMapper;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    private UserController userController;
    private UserRequest userRequest;
    private UserResponse expectedUserResponse;
    private UserResponse expectedUserResponseError;
    Map<String, Object> newValuesMap = new HashMap<>();

    private static final String USER_NAME = "user-name";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";

    private static final String ERROR_RETRIEVING_USER = "Error Retrieving User, Please Try Again!!!";

    @BeforeEach
    void init() throws JsonProcessingException {
        userController = new UserController(userService);

        userRequest = new UserRequest(USER_NAME, "pass-word",
                "first-name", "last-name", EMAIL, PHONE, "status");
        expectedUserResponse = UserResponse.builder()
                .users(singletonList(objectMapper()
                        .readValue(readFixture("fixtures/user/user.json"), User.class)))
                .build();
        newValuesMap.put("password", "password");
    }

    @Test
    void getAllUsers_returnsError() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Retrieving All Users, Please Try Again!!!")
                        .build())
                .build();

        when(userService.getAllUsers())
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.getAllUsers();

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void getAllUsers_returnsResponse() {
        expectedUserResponse = UserResponse.builder()
                .users(asList(
                        User.builder()
                                .username("user-name-01")
                                .build(),
                        User.builder()
                                .username("user-name-02")
                                .build()))
                .build();

        when(userService.getAllUsers())
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.getAllUsers();

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserById_returnsError_400() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Retrieving User by Invalid id: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserById("");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserById_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(ERROR_RETRIEVING_USER)
                        .build())
                .build();

        when(userService.getUserById(any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserById("id");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserById_returnsResponse() {
        when(userService.getUserById(any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserById("id");

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByUsername_returnsError_400_noUsername() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Retrieving User by Invalid Username: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByUserName("", "user-header");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByUsername_returnsError_400_userNameDifferentHeader() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Retrieving User by Invalid Username and Header: user-name | user-header")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByUserName(USER_NAME, "user-header");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByUsername_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(ERROR_RETRIEVING_USER)
                        .build())
                .build();

        when(userService.getUserByUsername(any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByUserName(USER_NAME, USER_NAME);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByUsername_returnsResponse() {
        when(userService.getUserByUsername(any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByUserName(USER_NAME, USER_NAME);

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByEmail_returnsError_400() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Retrieving User by Invalid Email: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByEmail("");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByEmail_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(ERROR_RETRIEVING_USER)
                        .build())
                .build();

        when(userService.getUsersByEmailOrPhone(any(), any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByEmail(EMAIL);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByEmail_returnsResponse() {
        when(userService.getUsersByEmailOrPhone(any(), any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByEmail(EMAIL);

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByPhone_returnsError_400() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Retrieving User by Invalid Phone: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByPhone("");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByPhone_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(ERROR_RETRIEVING_USER)
                        .build())
                .build();

        when(userService.getUsersByEmailOrPhone(any(), any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByPhone(PHONE);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void getUserByPhone_returnsResponse() {
        when(userService.getUsersByEmailOrPhone(any(), any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.getUserByPhone(PHONE);

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void saveNewUser_returnsError_400() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Saving User by Invalid Request!!!")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.saveNewUser(null);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void saveNewUser_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Saving User, Please Try Again!!!")
                        .build())
                .build();

        when(userService.saveNewUser(any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.saveNewUser(userRequest);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void saveNewUser_returnsResponse() {
        when(userService.saveNewUser(any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.saveNewUser(userRequest);

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void updateUser_userRequest_returnsError_400_noId() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Updating User by Invalid id / request: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.updateUser("", userRequest);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void updateUser_userRequest_returnsError_400_noRequest() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Updating User by Invalid id / request: id")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.updateUser("id", (UserRequest) null);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void updateUser_userRequest_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Updating User, Please Try Again!!!")
                        .build())
                .build();

        when(userService.updateUserById(any(), (UserRequest) any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.updateUser("id", userRequest);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void updateUser_userRequest_returnsResponse() {
        when(userService.updateUserById(any(), (UserRequest) any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.updateUser("id", userRequest);

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }

    @Test
    void updateUser_newValuesMap_returnsError_400_noId() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Updating User by Invalid id / newValues: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.updateUser("", newValuesMap);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    // DID NOT TEST remaining newValuesMap because
    // when(userService.updateUserById(any(), (Map<String, Object>) any())).thenReturn(expectedUserResponseError);  //NOSONAR
    // gives warning "uses unchecked or unsafe operations - warning only, test passes
    // still did not want to keep it as is
    // best to update the controller class

    @Test
    void updateUser_newValuesMap_returnsError_400_noRequest() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Updating User by Invalid id / newValues: id")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.updateUser("id", (Map<String, Object>) null);

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void deleteUser_returnsError_400() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Deleting User by Invalid id: ")
                        .build())
                .build();

        ResponseEntity<UserResponse> actualUserResponse = userController.deleteUser("");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(BAD_REQUEST, actualUserResponse.getStatusCode());
    }

    @Test
    void deleteUser_returnsError_500() {
        expectedUserResponseError = UserResponse.builder()
                .users(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg("Error Deleting User, Please Try Again!!!")
                        .build())
                .build();

        when(userService.deleteUserById(any()))
                .thenReturn(expectedUserResponseError);

        ResponseEntity<UserResponse> actualUserResponse = userController.deleteUser("id");

        assertEquals(expectedUserResponseError, actualUserResponse.getBody());
        assertNotNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(INTERNAL_SERVER_ERROR, actualUserResponse.getStatusCode());
    }

    @Test
    void deleteUser_returnsResponse() {
        when(userService.deleteUserById(any()))
                .thenReturn(expectedUserResponse);

        ResponseEntity<UserResponse> actualUserResponse = userController.deleteUser("id");

        assertEquals(expectedUserResponse, actualUserResponse.getBody());
        assertNull(requireNonNull(actualUserResponse.getBody()).getStatus());
        assertEquals(OK, actualUserResponse.getStatusCode());
    }
}
