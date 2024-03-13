package dto;

import mariadbPojo.UsersPojo;

import java.math.BigDecimal;
import java.sql.Date;

public class UserPasswordDTO {
  private int id;
  private String password;



  // Constructeur qui accepte un UserPojo
  public UserPasswordDTO(UsersPojo user) {
    this.id = user.getId();
    this.password = user.getPassword();
  }

  // Getters et Setters
}
