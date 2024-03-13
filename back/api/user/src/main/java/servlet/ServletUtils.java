package servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtils {
  public static String readRequestBody(HttpServletRequest request) throws IOException {
    StringBuilder jsonBody = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        jsonBody.append(line);
      }
    }
    return jsonBody.toString();
  }

  public static Integer extractAndValidateId(String pathInfo, HttpServletResponse response, boolean idAlwaysDemanded) throws IOException {
    if (pathInfo == null || pathInfo.length() <= 1) {
      if (idAlwaysDemanded) {
        ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                "{\"error\":\"L'id est manquant ou invalide.\"}");
      }
      return -1; // ID non spécifié, erreur si on le demandait obligatoirement
    }

    try {
      int id = Integer.parseInt(pathInfo.substring(1));
      if (id < 0) {
        ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                "{\"error\":\"L'id doit être un entier positif.\"}");
        return null;
      }
      return id;
    } catch (NumberFormatException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
              "{\"error\":\"Format de l'ID invalide, n'est pas un chiffre.\"}");
      return null; // il y a un ID, mais il n'est pas un nombre donc erreur
    }
  }

  /**
   * Envoie une réponse JSON avec le code d'état HTTP spécifié.
   *
   * @param response   La réponse HTTP.
   * @param statusCode Le code d'état HTTP.
   * @param message    Le message à envoyer.
   * @throws IOException Si une erreur d'entrée/sortie survient.
   */
  public static void sendJsonResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(statusCode);
    response.getWriter().write(message);
    response.getWriter().flush();
  }


}
