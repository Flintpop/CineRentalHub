package servlet;

import com.google.gson.Gson;
import dto.ImageDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;

import java.io.IOException;

@WebServlet(name = "MainImageServlet", urlPatterns = "/movies/main_image/*")
public class MainImageServlet extends HttpServlet {
  Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId;
    try {
      movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    try {
      ImageDTO mainImage = Movie.getMainImage(movieId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(mainImage));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
