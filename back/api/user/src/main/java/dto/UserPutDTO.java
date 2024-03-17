package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

@Getter
@Setter
public class UserPutDTO {
  private String last_name;
  private String first_name;
  private String email;
  private String role;
/*
Json pour tester requete :

{
    "last_name": "Last name",
    "first_name": "first Name",
    "email": "email",
    "activated": 1,
    "password": "password",
    "role": "ADMIN"
}
 */

  // Constructeur qui accepte un UsersPojo
  public UserPutDTO(UsersPojo user) {
    this.last_name = user.getLastName();
    this.first_name = user.getFirstName();
    this.email = user.getEmail();
    this.role = user.getRole().toString();
  }

  public UserPutDTO() {
  }
}
