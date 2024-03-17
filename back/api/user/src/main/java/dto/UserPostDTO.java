package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.UsersPojo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
public class UserPostDTO {
  private String last_name;
  private String first_name;
  private String email;
  private String password;
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
  public UserPostDTO(UsersPojo user) {
    this.last_name = user.getLastName();
    this.first_name = user.getFirstName();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.role = user.getRole().toString();
  }

  public UserPostDTO() {
  }

  // Fonction pour hacher le mot de passe
  public void hashPassword() {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
      this.password = bytesToHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Problème lors du hachage du mot de passe", e);
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
