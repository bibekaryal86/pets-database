package pets.database.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pets.database.model.*;
import pets.database.repository.AccountDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.util.StringUtils.hasText;
import static pets.database.utils.Constants.FIELD_NAME_DESCRIPTION;

@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private static final String ERROR_UPDATING_ACCOUNT = "Error Updating Account, Please Try Again!!!";

    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public AccountResponse getAllAccounts() {
        logger.info("Before Get All Accounts");
        List<Account> allAccounts = new ArrayList<>();
        Status status = null;

        try {
            allAccounts = accountDao.getAllAccounts();
        } catch (Exception ex) {
            logger.error("Get All Accounts", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Accounts, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get All Accounts: {}", allAccounts.size());
        return AccountResponse.builder()
                .accounts(allAccounts)
                .status(status)
                .build();
    }

    public AccountResponse getAccountById(String id) {
        logger.info("Before Get Account By Id: {}", id);
        Account account = null;
        Status status = null;

        try {
            account = accountDao.getAccountById(id);
        } catch (Exception ex) {
            logger.error("Get Account By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Retrieving Account, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get Account By Id: {} | {}", id, account);
        return AccountResponse.builder()
                .accounts(account == null ? emptyList() : singletonList(account))
                .status(status)
                .build();
    }

    public AccountResponse getAccountsByUsername(String username) {
        logger.info("Before Get Accounts By User Name: {}", username);
        List<Account> accounts = new ArrayList<>();
        Status status = null;

        try {
            accounts = accountDao.getAllAccountsByUsername(username);
        } catch (Exception ex) {
            logger.error("Get Account By User Name: {}", username, ex);
            status = Status.builder()
                    .errMsg("Error Retrieving Account, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Get Account By User Name: {} | {}", username, accounts.size());
        return AccountResponse.builder()
                .accounts(accounts)
                .status(status)
                .build();
    }

    public AccountResponse saveNewAccount(AccountRequest accountRequest) {
        logger.info("Before Save New Account: {}", accountRequest);
        Account newAccount;
        Status status = null;

        try {
            newAccount = Account.builder()
                    .refAccountType(RefAccountType.builder()
                            .id(accountRequest.getTypeId())
                            .build())
                    .refBank(RefBank.builder()
                            .id(accountRequest.getBankId())
                            .build())
                    .user(User.builder()
                            .username(accountRequest.getUsername())
                            .build())
                    .description(accountRequest.getDescription())
                    .openingBalance(accountRequest.getOpeningBalance())
                    .status(accountRequest.getStatus())
                    .creationDate(LocalDate.now().toString())
                    .lastModified(LocalDateTime.now().toString())
                    .build();

            newAccount = accountDao.saveNewAccount(newAccount);

            if (!hasText(newAccount.getId())) {
                newAccount = null;
                status = Status.builder()
                        .errMsg("Error Saving Account, Please Try Again!!!")
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Save New Account: {}", accountRequest, ex);
            newAccount = null;
            status = Status.builder()
                    .errMsg("Error Saving Account, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Save New Account: {}", newAccount);
        return AccountResponse.builder()
                .accounts(newAccount == null ? emptyList() : singletonList(newAccount))
                .status(status)
                .build();
    }

    public AccountResponse updateAccountById(String id, AccountRequest accountRequest) {
        logger.info("Before Update Account By Id: {} | {}", id, accountRequest);
        AccountResponse accountResponse;
        Status status;

        try {
            Update update = new Update();
            update.set("lastModified", LocalDateTime.now().toString());
            update.set("openingBalance", accountRequest.getOpeningBalance());

            if (hasText(accountRequest.getTypeId())) {
                update.set("refAccountType.id", accountRequest.getTypeId());
            }
            if (hasText(accountRequest.getBankId())) {
                update.set("refBank.id", accountRequest.getBankId());
            }
            if (hasText(accountRequest.getDescription())) {
                update.set(FIELD_NAME_DESCRIPTION, accountRequest.getDescription());
            }
            if (hasText(accountRequest.getStatus())) {
                update.set("status", accountRequest.getStatus());
            }

            long modifiedCount = accountDao.updateAccountById(id, update);

            if (modifiedCount > 0) {
                accountResponse = getAccountById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_ACCOUNT)
                        .build();
                accountResponse = AccountResponse.builder()
                        .accounts(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("Update Account By Id: {} | {}", id, accountRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_ACCOUNT)
                    .message(ex.toString())
                    .build();
            accountResponse = AccountResponse.builder()
                    .accounts(emptyList())
                    .status(status)
                    .build();
        }

        logger.info("After Update Account By Id: {} | account: {}", id, accountResponse);
        return accountResponse;
    }

    public AccountResponse deleteAccountById(String id) {
        logger.info("Before Delete Account By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            deleteCount = accountDao.deleteAccountById(id);
        } catch (Exception ex) {
            logger.error("Delete Account By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg("Error Deleting Account, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        logger.info("After Delete Account By Id: {} | deleteCount: {}", id, deleteCount);
        return AccountResponse.builder()
                .accounts(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
