package pets.database.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static pets.database.utils.Constants.COLLECTION_NAME_USER_DETAILS;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Document(collection = COLLECTION_NAME_USER_DETAILS)
public class User implements Serializable {
  @Id private String id;
  private String username;
  private String password;

  @Field(name = "first_name")
  private String firstName;

  @Field(name = "last_name")
  private String lastName;

  @Field(name = "street_address")
  private String streetAddress;

  private String city;
  private String state;

  @Field(name = "zip_code")
  private String zipcode;

  private String email;
  private String phone;
  private String status;
  private String creationDate;
  private String lastModified;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;
    User that = (User) o;
    return Objects.equals(this.id, that.id)
        && Objects.equals(this.username, that.username)
        && Objects.equals(this.password, that.password)
        && Objects.equals(this.firstName, that.firstName)
        && Objects.equals(this.lastName, that.lastName)
        && Objects.equals(this.streetAddress, that.streetAddress)
        && Objects.equals(this.city, that.city)
        && Objects.equals(this.state, that.state)
        && Objects.equals(this.zipcode, that.zipcode)
        && Objects.equals(this.email, that.email)
        && Objects.equals(this.phone, that.phone)
        && Objects.equals(this.creationDate, that.creationDate)
        && Objects.equals(this.lastModified, that.lastModified);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        username,
        password,
        firstName,
        lastName,
        streetAddress,
        city,
        state,
        zipcode,
        email,
        phone,
        status,
        creationDate,
        lastModified);
  }

  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + ", username='"
        + username
        + '\''
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", streetAddress='"
        + streetAddress
        + '\''
        + ", city='"
        + city
        + '\''
        + ", state='"
        + state
        + '\''
        + ", zipcode='"
        + zipcode
        + '\''
        + ", email='"
        + email
        + '\''
        + ", phone='"
        + phone
        + '\''
        + ", status='"
        + status
        + '\''
        + ", creationDate='"
        + creationDate
        + '\''
        + ", lastModified='"
        + lastModified
        + '\''
        + "}";
  }
}
