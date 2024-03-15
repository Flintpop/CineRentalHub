package servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UserActivateServlet", urlPatterns = "/user/activate/*")
public class UserActivateServlet extends HttpServlet {

  private final Gson gson = new Gson();


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getMethod().equalsIgnoreCase("PATCH")) {
      doPatch(req, resp);
    } else {
      super.service(req, resp);
    }
  }

  /**
   * **Active** un user par ID
   *
   * @param request  Requête HTTP
   * @param response Réponse HTTP
   * @throws IOException Erreur d'entrée/sortie
   */
  protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Récupérer l'id
    Integer userId;
    try {
      userId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (Exception e) {
      return; // Erreur déjà envoyée
    }

    // Activer l'utilisateur
    try {
      model.User.enableUser(userId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"message\":\"Utilisateur activé.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
