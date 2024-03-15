package servlet;

import com.google.gson.Gson;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cart;

import java.io.IOException;

//@WebServlet
@WebServlet(name = "CartServlet", urlPatterns = "/cart/delete/*")
public class CartDeleteServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer cartId;
    try {
      cartId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    try {
      Cart.deleteEntireCart(cartId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"message\":\" \"Suppression réussie\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
