package pets.database.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.database.model.RefAccountTypeRequest;
import pets.database.model.RefAccountTypeResponse;
import pets.database.model.Status;
import pets.database.service.RefAccountTypeService;
import springfox.documentation.annotations.ApiIgnore;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/refaccounttypes")
public class RefAccountTypeController {
    private final RefAccountTypeService refAccountTypeService;

    public RefAccountTypeController(RefAccountTypeService refAccountTypeService) {
        this.refAccountTypeService = refAccountTypeService;
    }

    @GetMapping(value = "/refaccounttype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> getAllRefAccountTypes() {
        return response(refAccountTypeService.getAllRefAccountTypes());
    }

    @GetMapping(value = "/refaccounttype/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> getRefAccountTypeById(@PathVariable("id") String id) {
        if (!hasText(id)) {
            return response(format("Error Retrieving Account Type by Invalid id: %s", id));
        } else {
            return response(refAccountTypeService.getRefAccountTypeById(id));
        }
    }

    @ApiIgnore
    @PostMapping(value = "/refaccounttype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> saveNewRefAccountType(@RequestBody RefAccountTypeRequest refAccountTypeRequest) {
        if (refAccountTypeRequest == null) {
            return response("Error Saving Account Type by Invalid Request!!!");
        } else {
            return response(refAccountTypeService.saveNewRefAccountType(refAccountTypeRequest));
        }
    }

    @ApiIgnore
    @PutMapping(value = "/refaccounttype/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> updateRefAccountType(@PathVariable("id") String id,
                                                                       @RequestBody RefAccountTypeRequest refAccountTypeRequest) {
        if (!hasText(id) || refAccountTypeRequest == null) {
            return response(format("Error Updating Account by Invalid id / request: %s", id));
        } else {
            return response(refAccountTypeService.updateRefAccountTypeById(id, refAccountTypeRequest));
        }
    }

    @ApiIgnore
    @DeleteMapping(value = "/refaccounttype/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefAccountTypeResponse> deleteRefAccountType(@PathVariable("id") String id) {
        if (!hasText(id)) {
            return response(format("Error Deleting Account Type by Invalid id: %s", id));
        } else {
            return response(refAccountTypeService.deleteRefAccountTypeById(id));
        }
    }

    private ResponseEntity<RefAccountTypeResponse> response(RefAccountTypeResponse refAccountTypeResponse) {
        if (refAccountTypeResponse.getStatus() == null) {
            return new ResponseEntity<>(refAccountTypeResponse, OK);
        } else {
            return new ResponseEntity<>(refAccountTypeResponse, INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<RefAccountTypeResponse> response(String errMsg) {
        return new ResponseEntity<>(RefAccountTypeResponse.builder()
                .refAccountTypes(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .build())
                .build(),
                BAD_REQUEST);
    }
}
