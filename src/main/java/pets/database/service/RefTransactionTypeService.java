package pets.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pets.database.model.RefTransactionType;
import pets.database.model.RefTransactionTypeRequest;
import pets.database.model.RefTransactionTypeResponse;
import pets.database.model.Status;
import pets.database.repository.RefTransactionTypeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.hasText;
import static pets.database.utils.Constants.FIELD_NAME_DESCRIPTION;

@Service
public class RefTransactionTypeService {
    private static final Logger logger = LoggerFactory.getLogger(RefTransactionTypeService.class);

    private final RefTransactionTypeDao refTransactionTypeDao;

    public RefTransactionTypeService(RefTransactionTypeDao refTransactionTypeDao) {
        this.refTransactionTypeDao = refTransactionTypeDao;
    }

    public RefTransactionTypeResponse getAllRefTransactionTypes() {
        logger.info("Before Get All Ref Transaction Types");
        List<RefTransactionType> refTransactionTypes = new ArrayList<>();
        Status status = null;

        try {
            refTransactionTypes = refTransactionTypeDao.getAllRefTransactionTypes();
        } catch (Exception ex) {
            logger.error("Get All Ref Transaction Types");
            status = Status.builder()
                    .errMsg("Error Retrieving All Transaction Types, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get All Ref Transaction Types: {}", refTransactionTypes.size());
        return RefTransactionTypeResponse.builder()
                .refTransactionTypes(refTransactionTypes)
                .status(status)
                .build();
    }

    public RefTransactionTypeResponse getRefTransactionTypeById(String id) {
        logger.info("Before Get Ref Transaction Type By Id: {}", id);
        RefTransactionType refTransactionType = null;
        Status status = null;

        try {
            refTransactionType = refTransactionTypeDao.getRefTransactionTypeById(id);
        } catch (Exception ex) {
            logger.error("Get Ref Transaction Type By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Retrieving Transaction Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get Ref Transaction Type By Id: {} | {}", id, refTransactionType);
        return RefTransactionTypeResponse.builder()
                .refTransactionTypes(refTransactionType == null ? emptyList() : singletonList(refTransactionType))
                .status(status)
                .build();
    }

    public RefTransactionTypeResponse saveNewRefTransactionType(RefTransactionTypeRequest refTransactionTypeRequest) {
        logger.info("Before Save New Ref Transaction Type: {}", refTransactionTypeRequest);
        RefTransactionType newRefTransactionType;
        Status status = null;

        try {
            newRefTransactionType = RefTransactionType.builder()
                    .description(refTransactionTypeRequest.getDescription())
                    .creationDate(LocalDate.now().toString())
                    .lastModified(LocalDateTime.now().toString())
                    .build();

            newRefTransactionType = refTransactionTypeDao.saveNewRefTransactionType(newRefTransactionType);

            if (!hasText(newRefTransactionType.getId())) {
                newRefTransactionType = null;
                status = Status.builder()
                        .errMsg("Error Saving Transaction Type, Please Try Again!!!")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Save New Ref Transaction Type: {}", refTransactionTypeRequest, ex);
            newRefTransactionType = null;
            status = Status.builder()
                    .errMsg("Error Saving Transaction Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Save New Ref Transaction Type: {}", newRefTransactionType);
        return RefTransactionTypeResponse.builder()
                .refTransactionTypes(newRefTransactionType == null ? emptyList() : singletonList(newRefTransactionType))
                .status(status)
                .build();
    }

    public RefTransactionTypeResponse updateRefTransactionTypeById(String id, RefTransactionTypeRequest refTransactionTypeRequest) {
        logger.info("Before Update Ref Transaction Type By Id: {} | {}", id, refTransactionTypeRequest);
        RefTransactionTypeResponse refTransactionTypeResponse;
        Status status;

        try {
            Update update = new Update();
            if (hasText(refTransactionTypeRequest.getDescription())) {
                update.set(FIELD_NAME_DESCRIPTION, refTransactionTypeRequest.getDescription());
            }
            update.set("lastModified", LocalDateTime.now().toString());

            long modifiedCount = refTransactionTypeDao.updateRefTransactionTypeById(id, update);

            if (modifiedCount > 0) {
                refTransactionTypeResponse = getRefTransactionTypeById(id);
            } else {
                status = Status.builder()
                        .errMsg("Error Updating Transaction Type, Please Try Again!!!")
                        .build();
                refTransactionTypeResponse = RefTransactionTypeResponse.builder()
                        .refTransactionTypes(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Update Ref Transaction Type By Id: {} | {}", id, refTransactionTypeRequest, ex);
            status = Status.builder()
                    .errMsg("Error Updating Transaction Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
            refTransactionTypeResponse = RefTransactionTypeResponse.builder()
                    .refTransactionTypes(emptyList())
                    .status(status)
                    .build();
        }

        logger.info("After Update Ref Transaction Type By Id: {} | {}", id, refTransactionTypeResponse);
        return refTransactionTypeResponse;
    }

    public RefTransactionTypeResponse deleteRefTransactionTypeById(String id) {
        logger.info("Before Delete Ref Transaction Type By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            deleteCount = refTransactionTypeDao.deleteRefTransactionTypeById(id);
        } catch (Exception ex) {
            logger.error("Delete Ref Transaction Type By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Deleting Transaction Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Delete Ref Transaction Type By Id: {} | {}", id, deleteCount);
        return RefTransactionTypeResponse.builder()
                .refTransactionTypes(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
