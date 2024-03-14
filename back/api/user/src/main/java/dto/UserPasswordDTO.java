package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

@Getter
@Setter
public class UserPasswordDTO {
  private int id;
  private String password;


  // Constructeur qui accepte un UserPojo
  public UserPasswordDTO(UsersPojo user) {
    this.id = user.getId();
    this.password = user.getPassword();
  }
}
