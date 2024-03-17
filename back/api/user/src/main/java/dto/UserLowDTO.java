package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

@Getter
@Setter
public class UserLowDTO {
  private Integer id;
  private String last_name;
  private String first_name;
  private String email;
  private String role;


  // Constructeur qui accepte un UserPojo
  public UserLowDTO(UsersPojo user) {
    this.id = user.getId();
    this.last_name = user.getLastName();
    this.first_name = user.getFirstName();
    this.email = user.getEmail();
    this.role = user.getRole().toString();
  }
}
