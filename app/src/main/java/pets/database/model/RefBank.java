package pets.database.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static pets.database.utils.Constants.COLLECTION_NAME_REF_BANK_DETAILS;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Document(collection = COLLECTION_NAME_REF_BANK_DETAILS)
public class RefBank implements Serializable {
  @Id private String id;
  private String description;
  private String creationDate;
  private String lastModified;
}
