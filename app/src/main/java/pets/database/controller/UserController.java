package pets.database.controller;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.database.model.Status;
import pets.database.model.UserRequest;
import pets.database.model.UserResponse;
import pets.database.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> getAllUsers() {
    return response(userService.getAllUsers());
  }

  @GetMapping(value = "/user/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving User by Invalid id: %s", id));
    } else {
      return response(userService.getUserById(id));
    }
  }

  @GetMapping(value = "/user/username/{username}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> getUserByUserName(
      @PathVariable("username") String username,
      @RequestHeader(value = "user-header") String userHeader) {
    if (!hasText(username)) {
      return response(format("Error Retrieving User by Invalid Username: %s", username));
    } else if (!username.equals(userHeader)) {
      return response(
          format(
              "Error Retrieving User by Invalid Username and Header: %s | %s",
              username, userHeader));
    } else {
      return response(userService.getUserByUsername(username));
    }
  }

  @GetMapping(value = "/user/email", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> getUserByEmail(@RequestParam("email") String email) {
    if (!hasText(email)) {
      return response(format("Error Retrieving User by Invalid Email: %s", email));
    } else {
      return response(userService.getUsersByEmailOrPhone(email, null));
    }
  }

  @GetMapping(value = "/user/phone", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> getUserByPhone(@RequestParam("phone") String phone) {
    if (!hasText(phone)) {
      return response(format("Error Retrieving User by Invalid Phone: %s", phone));
    } else {
      return response(userService.getUsersByEmailOrPhone(null, phone));
    }
  }

  @PostMapping(value = "/user", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> saveNewUser(@RequestBody UserRequest userRequest) {
    if (userRequest == null) {
      return response("Error Saving User by Invalid Request!!!");
    } else {
      return response(userService.saveNewUser(userRequest));
    }
  }

  /**
   * @param id id of the user object in db
   * @param userRequest object with new values to update
   * @return User object with updated values
   * @apiNote used to update ancillary attributes like name and address
   */
  @PutMapping(value = "/user/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable("id") String id, @RequestBody UserRequest userRequest) {
    if (!hasText(id) || userRequest == null) {
      return response(format("Error Updating User by Invalid id / request: %s", id));
    } else {
      return response(userService.updateUserById(id, userRequest));
    }
  }

  /**
   * @param id id of the user object in db
   * @param newValues (map of attribute to be updated and their values)
   * @return User object with updated values
   * @apiNote used to update non-null attributes like password, email, phone, status
   */
  @PatchMapping(value = "/user/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable("id") String id, @RequestBody Map<String, Object> newValues) {
    if (!hasText(id) || newValues == null) {
      return response(format("Error Updating User by Invalid id / newValues: %s", id));
    } else {
      return response(userService.updateUserById(id, newValues));
    }
  }

  /**
   * @param id id of the user object in db
   * @return number of rows deleted
   * @apiNote user should not be deleted, the status should be changed to INACTIVE
   */
  @DeleteMapping(value = "/user/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> deleteUser(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting User by Invalid id: %s", id));
    } else {
      return response(userService.deleteUserById(id));
    }
  }

  private ResponseEntity<UserResponse> response(UserResponse userResponse) {
    if (userResponse.getStatus() == null) {
      return new ResponseEntity<>(userResponse, OK);
    } else {
      return new ResponseEntity<>(userResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<UserResponse> response(String errMsg) {
    return new ResponseEntity<>(
        UserResponse.builder()
            .users(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
