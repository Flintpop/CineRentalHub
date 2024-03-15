package servlet;

import com.google.gson.JsonSyntaxException;
import dto.MovieDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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

  /**
   * Extrait et valide l'ID de la requête HTTP. Envoie une réponse JSON avec le code d'état HTTP approprié
   * si l'ID est manquant ou invalide, et en fonction d'idAlwaysDemanded.
   * @param pathInfo Le chemin de la requête HTTP.
   * @param response La réponse HTTP.
   * @param idAlwaysDemanded Si true, envoie une réponse JSON avec le code d'état HTTP approprié si l'ID est manquant.
   * @return L'ID extrait de la requête HTTP.
   * @throws IOException Si une erreur d'entrée/sortie survient.
   * @throws NumberFormatException Si l'ID n'est pas un chiffre.
   * @throws IdMissingException Si l'ID est manquant.
   * @throws IdValidationException Si l'ID est invalide.
   */
  public static Integer extractAndValidateId(String pathInfo, HttpServletResponse response, boolean idAlwaysDemanded)
          throws IOException, NumberFormatException, IdMissingException, IdValidationException {
    if (pathInfo == null || pathInfo.length() <= 1) {
      if (idAlwaysDemanded) {
        ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                "{\"error\":\"L'id est manquant ou invalide.\"}");
      }
      throw new IdMissingException("L'ID est manquant ou invalide.");
    }

    try {
      int id = Integer.parseInt(pathInfo.substring(1));
      if (id < 0) {
        ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                "{\"error\":\"L'id doit être un entier positif.\"}");
        throw new IdValidationException("L'ID doit être un entier positif.");
      }
      return id;
    } catch (NumberFormatException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST,
              "{\"error\":\"Format de l'ID invalide, n'est pas un chiffre.\"}");
      throw new NumberFormatException(e.getMessage());
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

  /**
   * En plus d'envoyer classiquement un json, supprime l'indication de la connexion si présente dans
   * le message d'erreur.
   * @param response La réponse HTTP.
   * @param statusCode Le code d'état HTTP.
   * @param errorMessage Le message d'erreur, en brut.
   * @throws IOException Si une erreur d'entrée/sortie survient.
   */
  public static void sendErrorJsonResponse(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
    if (errorMessage != null) {
      errorMessage = errorMessage.replaceAll("\\(conn=\\d+\\)\\s*", ""); // Supprime l'indication de la connexion
    } else {
      throw new IllegalArgumentException("Le message d'erreur ne peut pas être null.");
    }
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(statusCode);
    response.getWriter().write(errorMessage);
    response.getWriter().flush();
  }

  public static void sendErrorJsonResponseWithTraceback(HttpServletResponse response, int statusCode, Exception e) throws IOException {
    if (e.getCause() instanceof java.text.ParseException pe) {
      String jsonResponse = """
                {
                  "error": "Format de date incorrect.",
                  "expected_format": "MMM DD, YYYY",
                  "example": "Jan 01, 2021",
                  "received_value": %s
                }
                """.formatted(pe.getMessage().split(": ")[1]);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, jsonResponse);
    } else if (e instanceof JsonSyntaxException json) {
      String jsonReponse = """
              {
                "error": "Format JSON invalide, données incorrect.",
                "message": %s
              }
              """.formatted(json.getMessage());
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, jsonReponse);
    } else {
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();
      String jsonResponse = """
              {
                "error": "%s",
                "traceback": "%s"
              }
              """.formatted(e.getMessage(), exceptionAsString);
      sendErrorJsonResponse(response, statusCode, jsonResponse);
    }
}
}
