package exceptions;

import lombok.Getter;

/**
 * Exception générique pour les erreurs de l'API, elle contient un status code.
 */
@Getter
public class ApiException extends Exception {
  private final int statusCode;

  public ApiException(String message, int statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

}
