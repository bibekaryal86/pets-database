package pets.database.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonInclude(NON_NULL)
public class AccountRequest implements Serializable {
  @NonNull private String typeId;
  @NonNull private String bankId;
  @NonNull private String description;
  @NonNull private BigDecimal openingBalance;
  @NonNull private String status;
  @NonNull private String username;
}
