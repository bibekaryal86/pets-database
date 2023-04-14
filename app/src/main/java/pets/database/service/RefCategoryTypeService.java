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
import pets.database.model.RefCategoryType;
import pets.database.model.RefCategoryTypeRequest;
import pets.database.model.RefCategoryTypeResponse;
import pets.database.model.Status;
import pets.database.repository.RefCategoryTypeDao;

@Service
public class RefCategoryTypeService {
  private static final Logger logger = LoggerFactory.getLogger(RefCategoryTypeService.class);

  private final RefCategoryTypeDao refCategoryTypeDao;

  public RefCategoryTypeService(RefCategoryTypeDao refCategoryTypeDao) {
    this.refCategoryTypeDao = refCategoryTypeDao;
  }

  public RefCategoryTypeResponse getAllRefCategoryTypes() {
    logger.info("Before Get All Ref Category Types");
    List<RefCategoryType> refCategoryTypes = new ArrayList<>();
    Status status = null;

    try {
      refCategoryTypes = refCategoryTypeDao.getAllRefCategoryTypes();
    } catch (Exception ex) {
      logger.error("Get All Ref Category Types");
      status =
          Status.builder()
              .errMsg("Error Retrieving All Category Types, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get All Ref Category Types: {}", refCategoryTypes.size());
    return RefCategoryTypeResponse.builder()
        .refCategoryTypes(refCategoryTypes)
        .status(status)
        .build();
  }

  public RefCategoryTypeResponse getRefCategoryTypeById(String id) {
    logger.info("Before Get Ref Category Type By Id: {}", id);
    RefCategoryType refCategoryType = null;
    Status status = null;

    try {
      refCategoryType = refCategoryTypeDao.getRefCategoryTypeById(id);
    } catch (Exception ex) {
      logger.error("Get Ref Category Type By Id: {}", id, ex);
      status =
          Status.builder()
              .errMsg("Error Retrieving Category Types, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get Ref Category Type By Id: {} | {}", id, refCategoryType);
    return RefCategoryTypeResponse.builder()
        .refCategoryTypes(refCategoryType == null ? emptyList() : singletonList(refCategoryType))
        .status(status)
        .build();
  }

  public RefCategoryTypeResponse saveNewRefCategoryType(
      RefCategoryTypeRequest refCategoryTypeRequest) {
    logger.info("Before Save New Ref Category Type: {}", refCategoryTypeRequest);
    RefCategoryType newRefCategoryType;
    Status status = null;

    try {
      newRefCategoryType =
          RefCategoryType.builder()
              .description(refCategoryTypeRequest.getDescription())
              .creationDate(LocalDate.now().toString())
              .lastModified(LocalDateTime.now().toString())
              .build();

      newRefCategoryType = refCategoryTypeDao.saveNewRefCategoryType(newRefCategoryType);

      if (!hasText(newRefCategoryType.getId())) {
        newRefCategoryType = null;
        status = Status.builder().errMsg("Error Saving Category Type, Please Try Again!!!").build();
      }
    } catch (Exception ex) {
      logger.error("Save New Ref Category Type: {}", refCategoryTypeRequest, ex);
      newRefCategoryType = null;
      status =
          Status.builder()
              .errMsg("Error Saving Category Type, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Save New Ref Category Type: {}", newRefCategoryType);
    return RefCategoryTypeResponse.builder()
        .refCategoryTypes(
            newRefCategoryType == null ? emptyList() : singletonList(newRefCategoryType))
        .status(status)
        .build();
  }

  public RefCategoryTypeResponse updateRefCategoryTypeById(
      String id, RefCategoryTypeRequest refCategoryTypeRequest) {
    logger.info("Before Update Ref Category Type By Id: {} | {}", id, refCategoryTypeRequest);
    RefCategoryTypeResponse refCategoryTypeResponse;
    Status status;

    try {
      Update update = new Update();
      if (hasText(refCategoryTypeRequest.getDescription())) {
        update.set(FIELD_NAME_DESCRIPTION, refCategoryTypeRequest.getDescription());
      }
      update.set("lastModified", LocalDateTime.now().toString());

      long modifiedCount = refCategoryTypeDao.updateRefCategoryTypeById(id, update);

      if (modifiedCount > 0) {
        refCategoryTypeResponse = getRefCategoryTypeById(id);
      } else {
        status =
            Status.builder().errMsg("Error Updating Category Type, Please Try Again!!!").build();
        refCategoryTypeResponse =
            RefCategoryTypeResponse.builder().refCategoryTypes(emptyList()).status(status).build();
      }
    } catch (Exception ex) {
      logger.error("Update Ref Category Type By Id: {} | {}", id, refCategoryTypeRequest, ex);
      status =
          Status.builder()
              .errMsg("Error Updating Category Type, Please Try Again!!!")
              .message(ex.toString())
              .build();
      refCategoryTypeResponse =
          RefCategoryTypeResponse.builder().refCategoryTypes(emptyList()).status(status).build();
    }

    logger.info("After Update Ref Category Type By Id: {} | {}", id, refCategoryTypeResponse);
    return refCategoryTypeResponse;
  }

  public RefCategoryTypeResponse deleteRefCategoryTypeById(String id) {
    logger.info("Before Delete Ref Category Type By Id: {}", id);
    long deleteCount = 0;
    Status status = null;

    try {
      deleteCount = refCategoryTypeDao.deleteRefCategoryTypeById(id);
    } catch (Exception ex) {
      logger.error("Delete Ref Category Type By Id: {}", id, ex);
      status =
          Status.builder()
              .errMsg("Error Deleting Category Type, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Delete Ref Category Type By Id: {} | {}", id, deleteCount);
    return RefCategoryTypeResponse.builder()
        .refCategoryTypes(emptyList())
        .deleteCount(deleteCount)
        .status(status)
        .build();
  }
}
