package exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class RessourceNotFoundException extends ApiException {
  public RessourceNotFoundException(String message) {
    super(message, HttpServletResponse.SC_NOT_FOUND);
  }
}
