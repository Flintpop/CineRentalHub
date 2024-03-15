package servlet;

import com.google.gson.JsonSyntaxException;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;

import java.io.IOException;

@WebServlet(name = "MovieActivatedServlet", urlPatterns = "/movies/activated/*")
public class MovieActivatedServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getMethod().equalsIgnoreCase("PATCH")) {
      doPatch(req, resp);
    } else {
      super.service(req, resp);
    }
  }

  /**
   * **Désactive** un film par ID
   *
   * @param request  Requête HTTP
   * @param response Réponse HTTP
   * @throws IOException Erreur d'entrée/sortie
   */
  protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId;
    try {
      movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // Erreur déjà envoyée
    }
    try {
      Movie.activateMovie(movieId);

      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"message\":\"Film activé.\"}");
    } catch (JsonSyntaxException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
