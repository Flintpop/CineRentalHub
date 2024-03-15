package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

@Getter
@Setter
public class UserLowDTO {
  private String last_name;
  private String first_name;
  private String email;
  private byte activated;
  private String role;


  // Constructeur qui accepte un UserPojo
  public UserLowDTO(UsersPojo user) {
    this.last_name = user.getLastName();
    this.first_name = user.getFirstName();
    this.email = user.getEmail();
    this.activated = user.getActivated();
    this.role = user.getRole().toString();
  }
}
