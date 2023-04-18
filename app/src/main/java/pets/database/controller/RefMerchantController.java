package pets.database.controller;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

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
import pets.database.model.RefMerchantRequest;
import pets.database.model.RefMerchantResponse;
import pets.database.model.Status;
import pets.database.service.RefMerchantService;

@RestController
@RequestMapping("/refmerchants")
public class RefMerchantController {
  private final RefMerchantService refMerchantService;

  public RefMerchantController(RefMerchantService refMerchantService) {
    this.refMerchantService = refMerchantService;
  }

  @GetMapping(value = "/refmerchant", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> getAllRefMerchants() {
    RefMerchantResponse refMerchantResponse = refMerchantService.getAllRefMerchants();

    if (refMerchantResponse.getStatus() == null) {
      return new ResponseEntity<>(refMerchantResponse, OK);
    } else {
      return new ResponseEntity<>(refMerchantResponse, INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(value = "/refmerchant/user/{username}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> getAllRefMerchantsByUsername(
      @PathVariable("username") String username) {
    if (!hasText(username)) {
      return response(format("Error Retrieving Merchants by Invalid Username: %s", username));
    } else {
      return response(refMerchantService.getAllRefMerchantsByUsername(username));
    }
  }

  @GetMapping(value = "/refmerchant/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> getRefMerchantById(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving Merchant by Invalid id: %s", id));
    } else {
      return response(refMerchantService.getRefMerchantById(id));
    }
  }

  @Hidden
  @PostMapping(value = "/refmerchant", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> saveNewRefMerchant(
      @RequestBody RefMerchantRequest refMerchantRequest) {
    if (refMerchantRequest == null) {
      return response("Error Saving Merchant by Invalid Request!!!");
    } else {
      return response(refMerchantService.saveNewRefMerchant(refMerchantRequest));
    }
  }

  @Hidden
  @PutMapping(value = "/refmerchant/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> updateRefMerchant(
      @PathVariable("id") String id, @RequestBody RefMerchantRequest refMerchantRequest) {
    if (!hasText(id) || refMerchantRequest == null) {
      return response(format("Error Updating Merchant by Invalid id / request: %s", id));
    } else {
      return response(refMerchantService.updateRefMerchantById(id, refMerchantRequest));
    }
  }

  @Hidden
  @DeleteMapping(value = "/refmerchant/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefMerchantResponse> deleteRefMerchant(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting Merchant by Invalid id: %s", id));
    } else {
      return response(refMerchantService.deleteRefMerchantById(id));
    }
  }

  private ResponseEntity<RefMerchantResponse> response(RefMerchantResponse refMerchantResponse) {
    if (refMerchantResponse.getStatus() == null) {
      return new ResponseEntity<>(refMerchantResponse, OK);
    } else {
      return new ResponseEntity<>(refMerchantResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<RefMerchantResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefMerchantResponse.builder()
            .refMerchants(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
