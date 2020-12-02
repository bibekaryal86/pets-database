package pets.database.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class AccountRequest implements Serializable {
    @NonNull
    private String typeId;
    @NonNull
    private String bankId;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal openingBalance;
    @NonNull
    private String status;
    @NonNull
    private String username;
}
