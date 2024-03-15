package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Getter
@Setter
public class UserPasswordDTO {
  private String password;


  // Constructeur qui accepte un UserPojo
  public UserPasswordDTO(UsersPojo user) {
    this.password = user.getPassword();
  }

  public void hashPassword() {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
      this.password = Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Problème lors du hachage du mot de passe", e);
    }
  }
}
