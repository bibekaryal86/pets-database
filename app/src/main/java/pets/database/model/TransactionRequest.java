package pets.database.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class TransactionRequest implements Serializable {
    private String description;
    @NonNull
    private String accountId;
    private String trfAccountId;
    @NonNull
    private String typeId;
    @NonNull
    private String categoryId;
    @NonNull
    private String merchantId;
    @NonNull
    private String username;
    @NonNull
    private String date;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Boolean regular;
    @NonNull
    private Boolean necessary;
}
