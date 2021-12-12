package pets.database.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static pets.database.utils.Constants.COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Document(collection = COLLECTION_NAME_REF_TRANSACTION_TYPE_DETAILS)
public class RefTransactionType implements Serializable {
    @Id
    private String id;
    private String description;
    private String creationDate;
    private String lastModified;
}
