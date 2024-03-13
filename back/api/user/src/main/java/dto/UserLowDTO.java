package dto;

import mariadbPojo.UsersPojo;

import java.math.BigDecimal;
import java.sql.Date;

public class UserLowDTO {
  private int id;
  private String lastName;
  private String firstName;
  private String email;
  private boolean activated;
  private String role;


  // Constructeur qui accepte un UserPojo
  public UserLowDTO(UsersPojo user) {
    this.id = user.getId();
    this.lastName = user.getLastName();
    this.firstName = user.getFirstName();
    this.email = user.getEmail();
    this.activated = user.isActivated();
    this.role = user.getRole().toString();
  }

  // Getters et Setters
}
