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
      this.password = bytesToHex(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Problème lors du hachage du mot de passe", e);
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder(2 * hash.length);
    for (int i = 0; i < hash.length; i++) {
      String hex = Integer.toHexString(0xff & hash[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
