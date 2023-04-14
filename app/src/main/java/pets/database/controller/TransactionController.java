package pets.database.controller;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.database.model.Status;
import pets.database.model.TransactionRequest;
import pets.database.model.TransactionResponse;
import pets.database.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping(value = "/transaction", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> getAllTransactions() {
    return response(transactionService.getAllTransactions());
  }

  @GetMapping(value = "/transaction/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving Transaction by Invalid id: %s", id));
    } else {
      return response(transactionService.getTransactionById(id));
    }
  }

  @GetMapping(value = "/transaction/user/{username}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> getTransactionsByUser(
      @PathVariable("username") String username) {
    if (!hasText(username)) {
      return response(format("Error Retrieving Transaction by Invalid Username: %s", username));
    } else {
      return response(transactionService.getTransactionsByUser(username));
    }
  }

  @PostMapping(value = "/transaction", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> saveNewTransaction(
      @RequestBody TransactionRequest transactionRequest) {
    if (transactionRequest == null) {
      return response("Error Saving Transaction by Invalid Request!!!");
    } else {
      return response(transactionService.saveNewTransaction(transactionRequest));
    }
  }

  @PutMapping(value = "/transaction/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> updateTransaction(
      @PathVariable("id") String id, @RequestBody TransactionRequest transactionRequest) {
    if (!hasText(id) || transactionRequest == null) {
      return response(format("Error Updating Transaction by Invalid id / request: %s", id));
    } else {
      return response(transactionService.updateTransactionById(id, transactionRequest));
    }
  }

  @DeleteMapping(value = "/transaction/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> deleteTransaction(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting Transaction by Invalid id: %s", id));
    } else {
      return response(transactionService.deleteTransactionById(id));
    }
  }

  @DeleteMapping(value = "/transaction/accountid/{accountid}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<TransactionResponse> deleteTransactionsByAccountId(
      @PathVariable("accountid") String accountId) {
    if (!hasText(accountId)) {
      return response(format("Error Deleting Transactions by Invalid Account id: %s", accountId));
    } else {
      return response(transactionService.deleteTransactionsByAccountId(accountId));
    }
  }

  private ResponseEntity<TransactionResponse> response(TransactionResponse transactionResponse) {
    if (transactionResponse.getStatus() == null) {
      return new ResponseEntity<>(transactionResponse, OK);
    } else {
      return new ResponseEntity<>(transactionResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<TransactionResponse> response(String errMsg) {
    return new ResponseEntity<>(
        TransactionResponse.builder()
            .transactions(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
