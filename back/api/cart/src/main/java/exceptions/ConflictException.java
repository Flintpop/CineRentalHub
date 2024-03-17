package exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class ConflictException extends ApiException {
  public ConflictException(String message) {
    super(message, HttpServletResponse.SC_CONFLICT);
  }
}
