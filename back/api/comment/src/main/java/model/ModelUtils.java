package model;

import exceptions.ApiException;
import jakarta.servlet.http.HttpServletResponse;


public class ModelUtils {
  public static void generateException(Exception e) throws Exception {
    if (e.getCause() != null) {
      if (e.getCause().getCause() != null) {
        throw new ApiException(e.getCause().getCause().getMessage(), HttpServletResponse.SC_BAD_REQUEST);
      } else {
        throw new Exception(e.getCause().getMessage());
      }
    } else {
      throw new Exception(e.getMessage(), e);
    }
  }
}
