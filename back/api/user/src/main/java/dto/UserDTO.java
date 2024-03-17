package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

@Getter
@Setter
public class UserDTO {
  private Integer id;
  private String last_name;
  private String first_name;
  private String email;
  private Byte activated;
  private String password;
  private String role;


  // Constructeur qui accepte un UsersPojo
  public UserDTO(UsersPojo user) {
    this.id = user.getId();
    this.last_name = user.getLastName();
    this.first_name = user.getFirstName();
    this.email = user.getEmail();
    this.activated = user.getActivated();
    this.password = user.getPassword();
    this.role = user.getRole().toString();
  }
}
