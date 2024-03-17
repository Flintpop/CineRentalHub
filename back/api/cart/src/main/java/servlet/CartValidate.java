package servlet;

import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cart;

import java.io.IOException;

//@WebServlet
@WebServlet(name = "CartValidateServlet", urlPatterns = "/cart/validate/*")
public class CartValidate extends HttpServlet {
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    if (req.getMethod().equalsIgnoreCase("PATCH")) {
      doPatch(req, resp);
    } else {
      super.service(req, resp);
    }
  }

  /**
   * **Vide et loue / achète** les films par user ID
   *
   * @param request  Requête HTTP
   * @param response Réponse HTTP
   * @throws IOException Erreur d'entrée/sortie
   */
  protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer userId;
    try {
      userId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // Erreur déjà envoyée
    }
    try {
      Cart.validateCartByUseId(userId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"message\":\"Panier vidé et films achetés / loués.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
