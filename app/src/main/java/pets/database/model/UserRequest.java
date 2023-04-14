package pets.database.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

@Data
@JsonInclude(NON_NULL)
public class UserRequest implements Serializable {
  @NonNull private String username;
  @NonNull @ToString.Exclude private String password;
  @NonNull private String firstName;
  @NonNull private String lastName;
  private String streetAddress;
  private String city;
  private String state;
  private String zipcode;
  @NonNull private String email;
  @NonNull private String phone;
  @NonNull private String status;
}
