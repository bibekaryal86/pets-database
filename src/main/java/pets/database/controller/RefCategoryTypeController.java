package pets.database.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pets.database.model.RefCategoryTypeRequest;
import pets.database.model.RefCategoryTypeResponse;
import pets.database.model.Status;
import pets.database.service.RefCategoryTypeService;
import springfox.documentation.annotations.ApiIgnore;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/refcategorytypes")
public class RefCategoryTypeController {
    private final RefCategoryTypeService refCategoryTypeService;

    public RefCategoryTypeController(RefCategoryTypeService refCategoryTypeService) {
        this.refCategoryTypeService = refCategoryTypeService;
    }

    @GetMapping(value = "/refcategorytype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefCategoryTypeResponse> getAllRefCategoryTypes() {
        return response(refCategoryTypeService.getAllRefCategoryTypes());
    }

    @GetMapping(value = "/refcategorytype/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefCategoryTypeResponse> getRefCategoryTypeById(@PathVariable("id") String id) {
        if (!hasText(id)) {
            return response(format("Error Retrieving Category Type by Invalid id: %s", id));
        } else {
            return response(refCategoryTypeService.getRefCategoryTypeById(id));
        }
    }

    @ApiIgnore
    @PostMapping(value = "/refcategorytype", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefCategoryTypeResponse> saveNewRefCategoryType(@RequestBody RefCategoryTypeRequest refCategoryTypeRequest) {
        if (refCategoryTypeRequest == null) {
            return response("Error Saving Category Type by Invalid Request!!!");
        } else {
            return response(refCategoryTypeService.saveNewRefCategoryType(refCategoryTypeRequest));
        }
    }

    @ApiIgnore
    @PutMapping(value = "/refcategorytype/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefCategoryTypeResponse> updateRefCategoryType(@PathVariable("id") String id,
                                                                         @RequestBody RefCategoryTypeRequest refCategoryTypeRequest) {
        if (!hasText(id) || refCategoryTypeRequest == null) {
            return response(format("Error Updating Category Type by Invalid id / request: %s", id));
        } else {
            return response(refCategoryTypeService.updateRefCategoryTypeById(id, refCategoryTypeRequest));
        }
    }

    @ApiIgnore
    @DeleteMapping(value = "/refcategorytype/id/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<RefCategoryTypeResponse> deleteRefCategoryType(@PathVariable("id") String id) {
        if (!hasText(id)) {
            return response(format("Error Deleting Category Type by Invalid id: %s", id));
        } else {
            return response(refCategoryTypeService.deleteRefCategoryTypeById(id));
        }
    }

    private ResponseEntity<RefCategoryTypeResponse> response(RefCategoryTypeResponse refCategoryTypeResponse) {
        if (refCategoryTypeResponse.getStatus() == null) {
            return new ResponseEntity<>(refCategoryTypeResponse, OK);
        } else {
            return new ResponseEntity<>(refCategoryTypeResponse, INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<RefCategoryTypeResponse> response(String errMsg) {
        return new ResponseEntity<>(RefCategoryTypeResponse.builder()
                .refCategoryTypes(emptyList())
                .deleteCount(0L)
                .status(Status.builder()
                        .errMsg(errMsg)
                        .build())
                .build(),
                BAD_REQUEST);
    }
}
