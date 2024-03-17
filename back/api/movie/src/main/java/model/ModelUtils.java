package model;

public class ModelUtils {
  public static void generateException(Exception e) throws Exception {
    if (e.getCause() != null) {
      if (e.getCause().getCause() != null) {
        throw new Exception(e.getCause().getCause().getMessage());
      } else {
        throw new Exception(e.getCause().getMessage());
      }
    } else {
      throw new Exception(e.getMessage());
    }
  }
}
