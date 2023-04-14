package pets.database.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.hasText;
import static pets.database.utils.Constants.FIELD_NAME_DESCRIPTION;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pets.database.model.*;
import pets.database.repository.RefCategoryDao;

@Service
public class RefCategoryService {
  private static final Logger logger = LoggerFactory.getLogger(RefCategoryService.class);

  private final RefCategoryDao refCategoryDao;

  public RefCategoryService(RefCategoryDao refCategoryDao) {
    this.refCategoryDao = refCategoryDao;
  }

  public RefCategoryResponse getAllRefCategories() {
    logger.info("Before Get All Ref Categories");
    List<RefCategory> refCategories = new ArrayList<>();
    Status status = null;

    try {
      refCategories = refCategoryDao.getAllRefCategories();
    } catch (Exception ex) {
      logger.error("Get All Ref Categories");
      status =
          Status.builder()
              .errMsg("Error Retrieving All Categories, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get All Ref Categories: {}", refCategories.size());
    return RefCategoryResponse.builder().refCategories(refCategories).status(status).build();
  }

  public RefCategoryResponse getRefCategoryById(String id) {
    logger.info("Before Get Ref Category By Id: {}", id);
    RefCategory refCategory = null;
    Status status = null;

    try {
      refCategory = refCategoryDao.getRefCategoryById(id);
    } catch (Exception ex) {
      logger.error("Get Ref Category By Id: {}", id, ex);
      status =
          Status.builder()
              .errMsg("Error Retrieving Category, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get Ref Category By Id: {} | {}", id, refCategory);
    return RefCategoryResponse.builder()
        .refCategories(refCategory == null ? emptyList() : singletonList(refCategory))
        .status(status)
        .build();
  }

  public RefCategoryResponse saveNewRefCategory(RefCategoryRequest refCategoryRequest) {
    logger.info("Before Save New Ref Category: {}", refCategoryRequest);
    RefCategory newRefCategory;
    Status status = null;

    try {
      newRefCategory =
          RefCategory.builder()
              .description(refCategoryRequest.getDescription())
              .refCategoryType(
                  RefCategoryType.builder().id(refCategoryRequest.getCategoryTypeId()).build())
              .creationDate(LocalDate.now().toString())
              .lastModified(LocalDateTime.now().toString())
              .build();

      newRefCategory = refCategoryDao.saveNewRefCategory(newRefCategory);

      if (!hasText(newRefCategory.getId())) {
        newRefCategory = null;
        status = Status.builder().errMsg("Error Saving Category, Please Try Again!!!").build();
      }
    } catch (Exception ex) {
      logger.error("Save New Ref Category: {}", refCategoryRequest, ex);
      newRefCategory = null;
      status =
          Status.builder()
              .errMsg("Error Saving Category, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Save New Ref Category: {}", newRefCategory);
    return RefCategoryResponse.builder()
        .refCategories(newRefCategory == null ? emptyList() : singletonList(newRefCategory))
        .status(status)
        .build();
  }

  public RefCategoryResponse updateRefCategoryById(
      String id, RefCategoryRequest refCategoryRequest) {
    logger.info("Before Update Ref Category By Id: {} | {}", id, refCategoryRequest);
    RefCategoryResponse refCategoryResponse;
    Status status;

    try {
      Update update = new Update();
      if (hasText(refCategoryRequest.getDescription())) {
        update.set(FIELD_NAME_DESCRIPTION, refCategoryRequest.getDescription());
      }
      update.set("lastModified", LocalDateTime.now().toString());

      long modifiedCount = refCategoryDao.updateRefCategoryById(id, update);

      if (modifiedCount > 0) {
        refCategoryResponse = getRefCategoryById(id);
      } else {
        status = Status.builder().errMsg("Error Updating Category, Please Try Again!!!").build();
        refCategoryResponse =
            RefCategoryResponse.builder().refCategories(emptyList()).status(status).build();
      }
    } catch (Exception ex) {
      logger.error("Update Ref Category By Id: {} | {}", id, refCategoryRequest, ex);
      status =
          Status.builder()
              .errMsg("Error Updating Category, Please Try Again!!!")
              .message(ex.toString())
              .build();
      refCategoryResponse =
          RefCategoryResponse.builder().refCategories(emptyList()).status(status).build();
    }

    logger.info("After Update Ref Category By Id: {} | {}", id, refCategoryResponse);
    return refCategoryResponse;
  }

  public RefCategoryResponse deleteRefCategoryById(String id) {
    logger.info("Before Delete Ref Category By Id: {}", id);
    long deleteCount = 0;
    Status status = null;

    try {
      deleteCount = refCategoryDao.deleteRefCategoryById(id);
    } catch (Exception ex) {
      logger.error("Delete Ref Category By Id: {}", id, ex);
      status =
          Status.builder()
              .errMsg("Error Deleting Category, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Delete Ref Category By Id: {} | {}", id, deleteCount);
    return RefCategoryResponse.builder()
        .refCategories(emptyList())
        .deleteCount(deleteCount)
        .status(status)
        .build();
  }
}
