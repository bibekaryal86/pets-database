package pets.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pets.database.model.RefBank;
import pets.database.model.RefBankRequest;
import pets.database.model.RefBankResponse;
import pets.database.model.Status;
import pets.database.repository.RefBankDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.hasText;
import static pets.database.utils.Constants.FIELD_NAME_DESCRIPTION;

@Service
public class RefBankService {
    private static final Logger logger = LoggerFactory.getLogger(RefBankService.class);

    private final RefBankDao refBankDao;

    public RefBankService(RefBankDao refBankDao) {
        this.refBankDao = refBankDao;
    }

    public RefBankResponse getAllRefBanks() {
        logger.info("Before Get All Ref Banks");
        List<RefBank> refBanks = new ArrayList<>();
        Status status = null;

        try {
            refBanks = refBankDao.getAllRefBanks();
        } catch (Exception ex) {
            logger.error("Get All Ref Banks");
            status = Status.builder()
                    .errMsg("Error Retrieving All Banks, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get All Ref Banks: {}", refBanks.size());
        return RefBankResponse.builder()
                .refBanks(refBanks)
                .status(status)
                .build();
    }

    public RefBankResponse getRefBankById(String id) {
        logger.info("Before Get Ref Bank By Id: {}", id);
        RefBank refBank = null;
        Status status = null;

        try {
            refBank = refBankDao.getRefBankById(id);
        } catch (Exception ex) {
            logger.error("Get Ref Bank By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Retrieving Bank, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get Ref Bank By Id: {} | {}", id, refBank);
        return RefBankResponse.builder()
                .refBanks(refBank == null ? emptyList() : singletonList(refBank))
                .status(status)
                .build();
    }

    public RefBankResponse saveNewRefBank(RefBankRequest refBankRequest) {
        logger.info("Before Save New Ref Bank: {}", refBankRequest);
        RefBank newRefBank;
        Status status = null;

        try {
            newRefBank = RefBank.builder()
                    .description(refBankRequest.getDescription())
                    .creationDate(LocalDate.now().toString())
                    .lastModified(LocalDateTime.now().toString())
                    .build();

            newRefBank = refBankDao.saveNewRefBank(newRefBank);

            if (!hasText(newRefBank.getId())) {
                newRefBank = null;
                status = Status.builder()
                        .errMsg("Error Saving Bank, Please Try Again!!!")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Save New Ref Bank: {}", refBankRequest, ex);
            newRefBank = null;
            status = Status.builder()
                    .errMsg("Error Saving Bank, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Save New Ref Bank: {}", newRefBank);
        return RefBankResponse.builder()
                .refBanks(newRefBank == null ? emptyList() : singletonList(newRefBank))
                .status(status)
                .build();
    }

    public RefBankResponse updateRefBankById(String id, RefBankRequest refBankRequest) {
        logger.info("Before Update Ref Bank By Id: {} | {}", id, refBankRequest);
        RefBankResponse refBankResponse;
        Status status;

        try {
            Update update = new Update();
            if (hasText(refBankRequest.getDescription())) {
                update.set(FIELD_NAME_DESCRIPTION, refBankRequest.getDescription());
            }
            update.set("lastModified", LocalDateTime.now().toString());

            long modifiedCount = refBankDao.updateRefBankById(id, update);

            if (modifiedCount > 0) {
                refBankResponse = getRefBankById(id);
            } else {
                status = Status.builder()
                        .errMsg("Error Updating Bank, Please try again!!!")
                        .build();
                refBankResponse = RefBankResponse.builder()
                        .refBanks(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Update Ref Bank By Id: {} | {}", id, refBankRequest, ex);
            status = Status.builder()
                    .errMsg("Error Updating Bank, Please try again!!!")
                    .message(ex.toString())
                    .build();
            refBankResponse = RefBankResponse.builder()
                    .refBanks(emptyList())
                    .status(status)
                    .build();
        }

        logger.info("After Update Ref Bank By Id: {} | {}", id, refBankResponse);
        return refBankResponse;
    }

    public RefBankResponse deleteRefBankById(String id) {
        logger.info("Before Delete Ref Bank By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            deleteCount = refBankDao.deleteRefBankById(id);
        } catch (Exception ex) {
            logger.error("Delete Ref Bank By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Deleting Bank, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Delete Ref Bank By Id: {} | {}", id, deleteCount);
        return RefBankResponse.builder()
                .refBanks(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
