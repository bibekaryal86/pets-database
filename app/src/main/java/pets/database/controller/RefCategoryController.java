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
import pets.database.model.RefCategoryRequest;
import pets.database.model.RefCategoryResponse;
import pets.database.model.Status;
import pets.database.service.RefCategoryService;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/refcategories")
public class RefCategoryController {
  private final RefCategoryService refCategoryService;

  public RefCategoryController(RefCategoryService refCategoryService) {
    this.refCategoryService = refCategoryService;
  }

  @GetMapping(value = "/refcategory", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> getAllRefCategories() {
    return response(refCategoryService.getAllRefCategories());
  }

  @GetMapping(value = "/refcategory/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> getRefCategoryById(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Retrieving Category by Invalid id: %s", id));
    } else {
      return response(refCategoryService.getRefCategoryById(id));
    }
  }

  @Hidden
  @PostMapping(value = "/refcategory", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> saveNewRefCategory(
      @RequestBody RefCategoryRequest refCategoryRequest) {
    if (refCategoryRequest == null) {
      return response("Error Saving Category by Invalid Request!!!");
    } else {
      return response(refCategoryService.saveNewRefCategory(refCategoryRequest));
    }
  }

  @Hidden
  @PutMapping(value = "/refcategory/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> updateRefCategory(
      @PathVariable("id") String id, @RequestBody RefCategoryRequest refCategoryRequest) {
    if (!hasText(id) || refCategoryRequest == null) {
      return response(format("Error Updating Category by Invalid id / request: %s", id));
    } else {
      return response(refCategoryService.updateRefCategoryById(id, refCategoryRequest));
    }
  }

  @Hidden
  @DeleteMapping(value = "/refcategory/id/{id}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<RefCategoryResponse> deleteRefCategory(@PathVariable("id") String id) {
    if (!hasText(id)) {
      return response(format("Error Deleting Category by Invalid id: %s", id));
    } else {
      return response(refCategoryService.deleteRefCategoryById(id));
    }
  }

  private ResponseEntity<RefCategoryResponse> response(RefCategoryResponse refCategoryResponse) {
    if (refCategoryResponse.getStatus() == null) {
      return new ResponseEntity<>(refCategoryResponse, OK);
    } else {
      return new ResponseEntity<>(refCategoryResponse, INTERNAL_SERVER_ERROR);
    }
  }

  private ResponseEntity<RefCategoryResponse> response(String errMsg) {
    return new ResponseEntity<>(
        RefCategoryResponse.builder()
            .refCategories(emptyList())
            .deleteCount(0L)
            .status(Status.builder().errMsg(errMsg).build())
            .build(),
        BAD_REQUEST);
  }
}
