package pets.database.controller;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.database.model.RefBankRequest;
import pets.database.model.RefBankResponse;
import pets.database.model.Status;
import pets.database.service.RefBankService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/refbanks")
public class RefBankController {
  private final RefBankService refBankService;

  public RefBankController(RefBankService refBankService) {
    this.refBankService = refBankService;
  }

  @GetMapping(value = "/refbank", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> getAllRefBanks() {
    return response(refBankService.getAllRefBanks());
  }

  @GetMapping(value = "/refbank/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> getRefBankById(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving Bank by Invalid id: %s", id));
    } else {
      return response(refBankService.getRefBankById(id));
    }
  }

  @ApiIgnore
  @PostMapping(value = "/refbank", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> saveNewRefBank(
      @RequestBody RefBankRequest refBankRequest) {
    if (refBankRequest == null) {
      return response("Error Saving Bank by Invalid Request!!!");
    } else {
      return response(refBankService.saveNewRefBank(refBankRequest));
    }
  }

  @ApiIgnore
  @PutMapping(value = "/refbank/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> updateRefBank(
      @PathVariable("id") String id, @RequestBody RefBankRequest refBankRequest) {
    if (!hasText(id) || refBankRequest == null) {
      return response(format("Error Updating Bank by Invalid id / request: %s", id));
    } else {
      return response(refBankService.updateRefBankById(id, refBankRequest));
    }
  }

  @ApiIgnore
  @DeleteMapping(value = "/refbank/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefBankResponse> deleteRefBank(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting Bank by Invalid id: %s", id));
    } else {
      return response(refBankService.deleteRefBankById(id));
    }
  }

  private ResponseEntity<RefBankResponse> response(RefBankResponse refBankResponse) {
    if (refBankResponse.getStatus() == null) {
      return new ResponseEntity<>(refBankResponse, OK);
    } else {
      return new ResponseEntity<>(refBankResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<RefBankResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefBankResponse.builder()
            .refBanks(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
