package dto;

import mariadbPojo.UsersPojo;

public class UserDTO {
  private int id;
  private String last_name;
  private String first_name;
  private String email;
  private boolean activated;
  private String password;
  private String role;


  // Constructeur qui accepte un UsersPojo
  public UserDTO(UsersPojo user) {
    this.id = user.getId();
    this.last_name = user.getLastName();
    this.first_name = user.getFirstName();
    this.email = user.getEmail();
    this.activated = user.isActivated();
    this.password = user.getPassword();
    this.role = user.getRole().toString();
  }

  public int getId() {
    return id;
  }

  public String getLast_name() {
    return last_name;
  }

  public String getFirst_name() {
    return first_name;
  }

  public String getEmail() {
    return email;
  }

  public boolean isActivated() {
    return activated;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }
// Getters et Setters
}
