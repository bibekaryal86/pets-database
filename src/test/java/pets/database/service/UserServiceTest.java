package pets.database.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import pets.database.model.Status;
import pets.database.model.User;
import pets.database.model.UserRequest;
import pets.database.model.UserResponse;
import pets.database.repository.UserDao;

import java.util.HashMap;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static support.FixtureReader.readFixture;
import static support.ObjectMapperProvider.objectMapper;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    private UserService userService;
    private User expectedUser;
    private UserRequest userRequest;
    private UserResponse expectedUserResponse;

    @BeforeEach
    public void init() throws JsonProcessingException {
        userService = new UserService(userDao);

        userRequest = new UserRequest("user-name", "pass-word",
                "first-name", "last-name", "email", "phone", "status");
        String userString = readFixture("fixtures/user/user.json");
        expectedUser = objectMapper().readValue(userString, User.class);
        expectedUserResponse = UserResponse.builder()
                .users(singletonList(expectedUser))
                .build();
    }

    @Test
    public void getAllUsers_returnsResponse() {
        expectedUserResponse = UserResponse.builder()
                .users(asList(
                        User.builder()
                                .username("user-name-01")
                                .build(),
                        User.builder()
                                .username("user-name-02")
                                .build()))
                .build();

        when(userDao.getAllUsers())
                .thenReturn(expectedUserResponse.getUsers());

        UserResponse actualUserResponse = userService.getAllUsers();

        assertEquals(expectedUserResponse, actualUserResponse);
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    public void getAllUsers_catchesExceptionAndReturnsStatus() {
        expectedUserResponse = UserResponse.builder()
                .users(emptyList())
                .status(Status.builder()
                        .errMsg("Error Retrieving All Users, Please Try Again!!!")
                        .message("org.mockito.exceptions.base.MockitoException: whatever-so-and-so")
                        .build())
                .build();

        when(userDao.getAllUsers())
                .thenThrow(new MockitoException("whatever-so-and-so"));

        UserResponse actualUserResponse = userService.getAllUsers();

        assertEquals(expectedUserResponse, actualUserResponse);
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    public void getUserById_returnsResponse() {
        when(userDao.findUserById(anyString()))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.getUserById("id");

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void getUserByUsername_returnsResponse() {
        when(userDao.findUserByUsername(anyString()))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.getUserByUsername("username");

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void getUsersByEmailOrPhone_findsByEmail_returnsResponse() {
        when(userDao.findByEmailOrPhone(anyString(), anyString()))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.getUsersByEmailOrPhone("email", "");

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void getUsersByEmailOrPhone_findsByPhone_returnsResponse() {
        when(userDao.findByEmailOrPhone(anyString(), anyString()))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.getUsersByEmailOrPhone("", "phone");

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void saveNewUser_returnsResponse() {
        when(userDao.saveNewUser(any()))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.saveNewUser(userRequest);

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void updateUserById_userRequest_returnsResponse() {
        when(userDao.updateUserById(anyString(), any()))
                .thenReturn(1L);
        when(userDao.findUserById("id"))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.updateUserById("id", userRequest);

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void updateUserById_newValuesMap_returnsResponse() {
        when(userDao.updateUserById(anyString(), any()))
                .thenReturn(1L);
        when(userDao.findUserById("id"))
                .thenReturn(expectedUser);

        UserResponse actualUserResponse = userService.updateUserById("id", new HashMap<>());

        assertEquals(expectedUserResponse, actualUserResponse);
    }

    @Test
    public void deleteUserById_() {
        when(userDao.deleteUserById(anyString()))
                .thenReturn(1L);

        expectedUserResponse = UserResponse.builder()
                .users(emptyList())
                .deleteCount(1L)
                .build();

        UserResponse actualUserResponse = userService.deleteUserById("id");

        assertEquals(expectedUserResponse, actualUserResponse);
    }
}
