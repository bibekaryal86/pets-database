package pets.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class RefAccountTypeRequest implements Serializable {
    @NonNull
    private String description;

    @JsonCreator
    public RefAccountTypeRequest(@JsonProperty("description") String description) {
        this.description = description;
    }
}
