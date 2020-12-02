package pets.database.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static pets.database.utils.Constants.COLLECTION_NAME_TRANSACTION_DETAILS;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Document(collection = COLLECTION_NAME_TRANSACTION_DETAILS)
public class Transaction implements Serializable {
    @Id
    private String id;
    private String description;
    private Account account;
    private Account trfAccount;
    private RefTransactionType refTransactionType;
    private RefCategory refCategory;
    private RefMerchant refMerchant;
    private User user;
    private String date;
    private BigDecimal amount;
    private Boolean regular;
    private Boolean necessary;
    private String creationDate;
    private String lastModified;
}
