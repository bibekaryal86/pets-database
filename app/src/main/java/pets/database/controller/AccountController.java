package pets.database.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pets.database.model.AccountRequest;
import pets.database.model.AccountResponse;
import pets.database.model.Status;
import pets.database.service.AccountService;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/accounts")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @Hidden
  @GetMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountResponse> getAllAccounts() {
    return response(accountService.getAllAccounts());
  }

  @GetMapping(value = "/account/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving Account by Invalid id: %s", id));
    } else {
      return response(accountService.getAccountById(id));
    }
  }

  @Hidden
  @GetMapping(value = "/account/user/{username}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountResponse> getAccountsByUsername(
      @PathVariable("username") String username) {
    if (!hasText(username)) {
      return response(format("Error Retrieving Account by Invalid Username: %s", username));
    } else {
      return response(accountService.getAccountsByUsername(username));
    }
  }

  @Hidden
  @PostMapping(value = "/account", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountResponse> saveNewAccount(
      @RequestBody AccountRequest accountRequest) {
    if (accountRequest == null) {
      return response("Error Saving Account by Invalid Request!!!");
    } else {
      return response(accountService.saveNewAccount(accountRequest));
    }
  }

  /**
   * @param id id of the account object in db
   * @param accountRequest object with new values to update
   * @return Account object with updated values
   * @apiNote used to update ancillary attributes like description, type, bank and status
   */
  @Hidden
  @PutMapping(value = "/account/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountResponse> updateAccount(
      @PathVariable("id") String id, @RequestBody AccountRequest accountRequest) {
    if (!hasText(id) || accountRequest == null) {
      return response(format("Error Updating Account by Invalid id / request: %s", id));
    } else {
      return response(accountService.updateAccountById(id, accountRequest));
    }
  }

  /**
   * @param id id of the account object in db
   * @return number of rows deleted
   * @apiNote account should not be deleted, the status should be changed to INACTIVE
   */
  @Hidden
  @DeleteMapping(value = "/account/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AccountResponse> deleteAccount(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting Account by Invalid id: %s", id));
    } else {
      return response(accountService.deleteAccountById(id));
    }
  }

  private ResponseEntity<AccountResponse> response(AccountResponse accountResponse) {
    if (accountResponse.getStatus() == null) {
      return new ResponseEntity<>(accountResponse, OK);
    } else {
      return new ResponseEntity<>(accountResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<AccountResponse> response(String errMsg) {
    return new ResponseEntity<>(
        AccountResponse.builder()
            .accounts(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
