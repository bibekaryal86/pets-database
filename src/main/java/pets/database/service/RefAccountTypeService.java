package pets.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pets.database.model.RefAccountType;
import pets.database.model.RefAccountTypeRequest;
import pets.database.model.RefAccountTypeResponse;
import pets.database.model.Status;
import pets.database.repository.RefAccountTypeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.hasText;
import static pets.database.utils.Constants.FIELD_NAME_DESCRIPTION;

@Service
public class RefAccountTypeService {
    private static final Logger logger = LoggerFactory.getLogger(RefAccountTypeService.class);

    private final RefAccountTypeDao refAccountTypeDao;

    public RefAccountTypeService(RefAccountTypeDao refAccountTypeDao) {
        this.refAccountTypeDao = refAccountTypeDao;
    }

    public RefAccountTypeResponse getAllRefAccountTypes() {
        logger.info("Before Get All Ref Account Types");
        List<RefAccountType> refAccountTypes = new ArrayList<>();
        Status status = null;

        try {
            refAccountTypes = refAccountTypeDao.getAllRefAccountTypes();
        } catch (Exception ex) {
            logger.error("Get All Ref Account Types");
            status = Status.builder()
                    .errMsg("Error Retrieving All Account Types, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get All Ref Account Types: {}", refAccountTypes.size());
        return RefAccountTypeResponse.builder()
                .refAccountTypes(refAccountTypes)
                .status(status)
                .build();
    }

    public RefAccountTypeResponse getRefAccountTypeById(String id) {
        logger.info("Before Get Ref Account Type By Id: {}", id);
        RefAccountType refAccountType = null;
        Status status = null;

        try {
            refAccountType = refAccountTypeDao.getRefAccountTypeById(id);
        } catch (Exception ex) {
            logger.error("Get Ref Account Type By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Retrieving Account Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get Ref Account Type By Id: {} | {}", id, refAccountType);
        return RefAccountTypeResponse.builder()
                .refAccountTypes(refAccountType == null ? emptyList() : singletonList(refAccountType))
                .status(status)
                .build();
    }

    public RefAccountTypeResponse saveNewRefAccountType(RefAccountTypeRequest refAccountTypeRequest) {
        logger.info("Before Save New Ref Account Type: {}", refAccountTypeRequest);
        RefAccountType newRefAccountType;
        Status status = null;

        try {
            newRefAccountType = RefAccountType.builder()
                    .description(refAccountTypeRequest.getDescription())
                    .creationDate(LocalDate.now().toString())
                    .lastModified(LocalDateTime.now().toString())
                    .build();

            newRefAccountType = refAccountTypeDao.saveNewRefAccountType(newRefAccountType);

            if (!hasText(newRefAccountType.getId())) {
                newRefAccountType = null;
                status = Status.builder()
                        .errMsg("Error Saving Account Type, Please Try Again!!!")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Save New Ref Account Type: {}", refAccountTypeRequest, ex);
            newRefAccountType = null;
            status = Status.builder()
                    .errMsg("Error Saving Account Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Save New Ref Account Type: {}", newRefAccountType);
        return RefAccountTypeResponse.builder()
                .refAccountTypes(newRefAccountType == null ? emptyList() : singletonList(newRefAccountType))
                .status(status)
                .build();
    }

    public RefAccountTypeResponse updateRefAccountTypeById(String id, RefAccountTypeRequest refAccountTypeRequest) {
        logger.info("Before Update Ref Account Type By Id: {} | {}", id, refAccountTypeRequest);
        RefAccountTypeResponse refAccountTypeResponse;
        Status status;

        try {
            Update update = new Update();
            if (hasText(refAccountTypeRequest.getDescription())) {
                update.set(FIELD_NAME_DESCRIPTION, refAccountTypeRequest.getDescription());
            }
            update.set("lastModified", LocalDateTime.now().toString());

            long modifiedCount = refAccountTypeDao.updateRefAccountTypeById(id, update);

            if (modifiedCount > 0) {
                refAccountTypeResponse = getRefAccountTypeById(id);
            } else {
                status = Status.builder()
                        .errMsg("Error Updating Account Type, Please Try Again!!!")
                        .build();
                refAccountTypeResponse = RefAccountTypeResponse.builder()
                        .refAccountTypes(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Update Ref Account Type By Id: {} | {}", id, refAccountTypeRequest, ex);
            status = Status.builder()
                    .errMsg("Error Updating Account Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
            refAccountTypeResponse = RefAccountTypeResponse.builder()
                    .refAccountTypes(emptyList())
                    .status(status)
                    .build();
        }

        logger.info("After Update Ref Account Type By Id: {} | {}", id, refAccountTypeResponse);
        return refAccountTypeResponse;
    }

    public RefAccountTypeResponse deleteRefAccountTypeById(String id) {
        logger.info("Before Delete Ref Account Type By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            deleteCount = refAccountTypeDao.deleteRefAccountTypeById(id);
        } catch (Exception ex) {
            logger.error("Delete Ref Account Type By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Deleting Account Type, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Delete Ref Account Type By Id: {} | {}", id, deleteCount);
        return RefAccountTypeResponse.builder()
                .refAccountTypes(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
