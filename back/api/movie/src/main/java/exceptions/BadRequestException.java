package exceptions;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends ApiException {
  public BadRequestException(String message) {
    super(message, HttpServletResponse.SC_BAD_REQUEST);
  }
}
