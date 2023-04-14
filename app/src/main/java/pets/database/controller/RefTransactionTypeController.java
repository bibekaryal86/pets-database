package pets.database.controller;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.database.model.RefTransactionTypeRequest;
import pets.database.model.RefTransactionTypeResponse;
import pets.database.model.Status;
import pets.database.service.RefTransactionTypeService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/reftransactiontypes")
public class RefTransactionTypeController {
  private final RefTransactionTypeService refTransactionTypeService;

  public RefTransactionTypeController(RefTransactionTypeService refTransactionTypeService) {
    this.refTransactionTypeService = refTransactionTypeService;
  }

  @GetMapping(value = "/reftransactiontype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> getAllRefTransactionTypes() {
    return response(refTransactionTypeService.getAllRefTransactionTypes());
  }

  @ApiIgnore
  @GetMapping(value = "/reftransactiontype/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> getRefTransactionTypeById(
      @PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving Transaction Type by Invalid id: %s", id));
    } else {
      return response(refTransactionTypeService.getRefTransactionTypeById(id));
    }
  }

  @ApiIgnore
  @PostMapping(value = "/reftransactiontype", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> saveNewRefTransactionType(
      @RequestBody RefTransactionTypeRequest refTransactionTypeRequest) {
    if (refTransactionTypeRequest == null) {
      return response("Error Saving Transaction Type by Invalid Request!!!");
    } else {
      return response(
          refTransactionTypeService.saveNewRefTransactionType(refTransactionTypeRequest));
    }
  }

  @ApiIgnore
  @PutMapping(value = "/reftransactiontype/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> updateRefTransactionType(
      @PathVariable("id") String id,
      @RequestBody RefTransactionTypeRequest refTransactionTypeRequest) {
    if (!hasText(id) || refTransactionTypeRequest == null) {
      return response(format("Error Updating Transaction Type by Invalid id / request: %s", id));
    } else {
      return response(
          refTransactionTypeService.updateRefTransactionTypeById(id, refTransactionTypeRequest));
    }
  }

  @ApiIgnore
  @DeleteMapping(value = "/reftransactiontype/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefTransactionTypeResponse> deleteRefTransactionType(
      @PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting Transaction Type by Invalid id: %s", id));
    } else {
      return response(refTransactionTypeService.deleteRefTransactionTypeById(id));
    }
  }

  private ResponseEntity<RefTransactionTypeResponse> response(
      RefTransactionTypeResponse refTransactionTypeResponse) {
    if (refTransactionTypeResponse.getStatus() == null) {
      return new ResponseEntity<>(refTransactionTypeResponse, OK);
    } else {
      return new ResponseEntity<>(refTransactionTypeResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<RefTransactionTypeResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefTransactionTypeResponse.builder()
            .refTransactionTypes(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
