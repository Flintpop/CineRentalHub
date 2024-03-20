package servlet;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import exceptions.ApiException;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ServletUtils {
  public static <T> T readRequestBodyAndGetObject(HttpServletRequest request, Class<T> clazz) throws IOException, JsonSyntaxException {
    StringBuilder jsonBody = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        jsonBody.append(line);
      }
    }
    return convertObjectToJson(jsonBody.toString(), clazz);
  }

  private static <T> T convertObjectToJson(String json, Class<T> clazz) {
    GsonBuilder builder = new GsonBuilder();

    // Ajoute un désérialiseur personnalisé pour les objets Date
    JsonDeserializer<Date> deserializer = (jsonDeserialize, typeOfT, context) -> {
      try {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(jsonDeserialize.getAsJsonPrimitive().getAsString());
      } catch (ParseException e) {
        throw new RuntimeException(e); // Gérer l'exception comme il convient
      }
    };

    Gson gson = builder.registerTypeAdapter(Date.class, deserializer).create();
    T object = gson.fromJson(json, clazz);

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(object);
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
          throw new JsonSyntaxException("Le champ '" + field.getName() + "' est vide.");
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Erreur lors de l'accès au champ '" + field.getName() + "'.", e);
      }
    }
    return object;
  }

  /**
   * Extrait et valide l'ID de la requête HTTP. Envoie une réponse JSON avec le code d'état HTTP approprié
   * si l'ID est manquant ou invalide, et en fonction d'idAlwaysDemanded.
   *
   * @param pathInfo         Le chemin de la requête HTTP.
   * @param response         La réponse HTTP.
   * @param idAlwaysDemanded Si true, envoie une réponse JSON avec le code d'état HTTP approprié si l'ID est manquant.
   * @return L'ID extrait de la requête HTTP.
   * @throws IOException           Si une erreur d'entrée/sortie survient.
   * @throws NumberFormatException Si l'ID n'est pas un chiffre.
   * @throws IdMissingException    Si l'ID est manquant.
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


  public static void sendErrorJsonResponseWithTraceback(HttpServletResponse response, int statusCode, Exception e) throws IOException {
    JsonObject jsonResponse = new JsonObject();
    Gson gson = new Gson();

    String cleanMessage = cleanErrorMessage(e.getMessage());

    if (e.getCause() instanceof java.text.ParseException pe) {
      jsonResponse.addProperty("error", "Format de date incorrect.");
      jsonResponse.addProperty("expected_format", "MMM DD, YYYY");
      jsonResponse.addProperty("example", "Jan 01, 2021");
      jsonResponse.addProperty("received_value", cleanErrorMessage(pe.getMessage().split(": ")[1]));
    } else if (e instanceof JsonSyntaxException json) {
      jsonResponse.addProperty("error", "Format JSON invalide, données incorrect.");
      jsonResponse.addProperty("message", cleanErrorMessage(json.getMessage()));
    } else {
      if (e instanceof ApiException apiException) {
        statusCode = apiException.getStatusCode();
      } else if (isEntityNotFound(e)) {
        statusCode = HttpServletResponse.SC_NOT_FOUND;
      }
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      String exceptionAsString = sw.toString();

      jsonResponse.addProperty("error", cleanMessage);
      jsonResponse.addProperty("traceback", exceptionAsString.replace("\n", "\\n").replace("\r", "\\r"));
    }

    sendJsonResponse(response, statusCode, gson.toJson(jsonResponse));
  }

  /**
   * Nettoie le message d'erreur en retirant le numéro de connexion.
   * @param errorMessage Le message d'erreur.
   * @return Le message d'erreur nettoyé.
   */
  private static String cleanErrorMessage(String errorMessage) {
    if (errorMessage != null) {
      return errorMessage.replaceAll("\\(conn=\\d+\\)\\s*", "");
    }
    return "Erreur inconnue"; // Fallback pour les messages d'erreur null
  }

  // Implémentation de isEntityNotFound reste inchangée

  /**
   * Envoie une réponse JSON avec le code d'état HTTP spécifié.
   *
   * @param response   La réponse HTTP.
   * @param statusCode Le code d'état HTTP.
   * @param jsonResponse La réponse JSON.
   * @throws IOException Si une erreur d'entrée/sortie survient.
   */
  public static void sendJsonResponse(HttpServletResponse response, int statusCode, String jsonResponse) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(statusCode);
    response.getWriter().write(jsonResponse);
    response.getWriter().flush();
  }

  private static boolean isEntityNotFound(Exception e) {
    String[] filters = {"Entity not found", "Aucun", "Not found", "No entity", "No result", "No entity found",
            "Aucune entité", "Aucun résultat", "Entité non trouvée", "Résultat non trouvé", "non trouvé", "non trouvée",
            "existe pas"
    };

    for (String filter : filters) {
      if (e.getMessage().toLowerCase().contains(filter.toLowerCase())) {
        return true;
      }
    }
    return false;
  }
}
