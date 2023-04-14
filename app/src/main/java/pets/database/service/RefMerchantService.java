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
import pets.database.repository.RefMerchantDao;

@Service
public class RefMerchantService {
  private static final Logger logger = LoggerFactory.getLogger(RefMerchantService.class);

  private final RefMerchantDao refMerchantDao;

  public RefMerchantService(RefMerchantDao refMerchantDao) {
    this.refMerchantDao = refMerchantDao;
  }

  public RefMerchantResponse getAllRefMerchants() {
    logger.info("Before Get All Ref Merchants");
    List<RefMerchant> refMerchants = new ArrayList<>();
    Status status = null;

    try {
      refMerchants = refMerchantDao.getAllRefMerchants();
    } catch (Exception ex) {
      logger.error("Get All Ref Merchants");
      status =
          Status.builder()
              .errMsg("Error Retrieving All Merchants, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get All Ref Merchants: {}", refMerchants.size());
    return RefMerchantResponse.builder().refMerchants(refMerchants).status(status).build();
  }

  public RefMerchantResponse getAllRefMerchantsByUsername(String username) {
    logger.info("Before Get All Ref Merchants By Username: {}", username);
    List<RefMerchant> refMerchants = new ArrayList<>();
    Status status = null;

    try {
      refMerchants = refMerchantDao.getAllRefMerchantsByUsername(username);
    } catch (Exception ex) {
      logger.error("Get All Ref Merchants By Username: {}", username, ex);
      status =
          Status.builder()
              .errMsg("Error Retrieving All Merchants By Username, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get All Ref Merchants: {}", refMerchants.size());
    return RefMerchantResponse.builder().refMerchants(refMerchants).status(status).build();
  }

  public RefMerchantResponse getRefMerchantById(String id) {
    logger.info("Before Get Ref Merchant By Id: {}", id);
    RefMerchant refMerchant = null;
    Status status = null;

    try {
      refMerchant = refMerchantDao.getRefMerchantById(id);
    } catch (Exception ex) {
      logger.error("Get Ref Merchant By Id: {}", id, ex);
      status =
          Status.builder()
              .errMsg("Error Retrieving Merchant, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Get Ref Merchant By Id: {} | {}", id, refMerchant);
    return RefMerchantResponse.builder()
        .refMerchants(refMerchant == null ? emptyList() : singletonList(refMerchant))
        .status(status)
        .build();
  }

  public RefMerchantResponse saveNewRefMerchant(RefMerchantRequest refMerchantRequest) {
    logger.info("Before Save New Ref Merchant: {}", refMerchantRequest);
    RefMerchant newRefMerchant;
    Status status = null;

    try {
      newRefMerchant =
          RefMerchant.builder()
              .description(refMerchantRequest.getDescription())
              .user(User.builder().username(refMerchantRequest.getUsername()).build())
              .creationDate(LocalDate.now().toString())
              .lastModified(LocalDateTime.now().toString())
              .build();

      newRefMerchant = refMerchantDao.saveNewRefMerchant(newRefMerchant);

      if (!hasText(newRefMerchant.getId())) {
        newRefMerchant = null;
        status = Status.builder().errMsg("Error Saving Merchant, Please Try Again!!!").build();
      }
    } catch (Exception ex) {
      logger.error("Save New Ref Merchant: {}", refMerchantRequest, ex);
      newRefMerchant = null;
      status =
          Status.builder()
              .errMsg("Error Saving Merchant, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Save New Ref Merchant: {}", newRefMerchant);
    return RefMerchantResponse.builder()
        .refMerchants(newRefMerchant == null ? emptyList() : singletonList(newRefMerchant))
        .status(status)
        .build();
  }

  public RefMerchantResponse updateRefMerchantById(
      String id, RefMerchantRequest refMerchantRequest) {
    logger.info("Before Update Ref Merchant By Id: {} | {}", id, refMerchantRequest);
    RefMerchantResponse refMerchantResponse;
    Status status;

    try {
      Update update = new Update();
      if (hasText(refMerchantRequest.getDescription())) {
        update.set(FIELD_NAME_DESCRIPTION, refMerchantRequest.getDescription());
      }
      update.set("lastModified", LocalDateTime.now().toString());

      long modifiedCount = refMerchantDao.updateRefMerchantById(id, update);

      if (modifiedCount > 0) {
        refMerchantResponse = getRefMerchantById(id);
      } else {
        status = Status.builder().errMsg("Error Updating Merchant, Please Try Again!!!").build();
        refMerchantResponse =
            RefMerchantResponse.builder().refMerchants(emptyList()).status(status).build();
      }
    } catch (Exception ex) {
      logger.error("Update Ref Merchant By Id: {} | {}", id, refMerchantRequest, ex);
      status =
          Status.builder()
              .errMsg("Error Updating Merchant, Please Try Again!!!")
              .message(ex.toString())
              .build();
      refMerchantResponse =
          RefMerchantResponse.builder().refMerchants(emptyList()).status(status).build();
    }

    logger.info("After Update Ref Merchant By Id: {} | {}", id, refMerchantResponse);
    return refMerchantResponse;
  }

  public RefMerchantResponse deleteRefMerchantById(String id) {
    logger.info("Before Delete Ref Merchant By Id: {}", id);
    long deleteCount = 0;
    Status status = null;

    try {
      deleteCount = refMerchantDao.deleteRefMerchantById(id);
    } catch (Exception ex) {
      logger.error("Delete Ref Merchant By Id: {}", id, ex);
      status =
          Status.builder()
              .errMsg("Error Deleting Merchant, Please Try Again!!!")
              .message(ex.toString())
              .build();
    }

    logger.info("After Delete Ref Merchant By Id: {} | {}", id, deleteCount);
    return RefMerchantResponse.builder()
        .refMerchants(emptyList())
        .deleteCount(deleteCount)
        .status(status)
        .build();
  }
}
