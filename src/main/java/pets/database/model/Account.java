package pets.database.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static pets.database.utils.Constants.COLLECTION_NAME_ACCOUNT_DETAILS;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Document(collection = COLLECTION_NAME_ACCOUNT_DETAILS)
public class Account implements Serializable {
    @Id
    private String id;
    private RefAccountType refAccountType;
    private RefBank refBank;
    private String description;
    private User user;
    @Field(name = "opening_balance")
    private BigDecimal openingBalance;
    private String status;
    private String creationDate;
    private String lastModified;
}
