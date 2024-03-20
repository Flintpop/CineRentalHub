package servlet;

import com.google.gson.Gson;
import dto.RentalsDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MovieRentalsServlet", urlPatterns = "/movies/rentals/*")
public class MovieRentalsServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Integer userId;
    try {
      userId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, false);
    } catch (IdValidationException | IdMissingException | NumberFormatException e) {
      return; // Erreur déjà envoyée
    }

    // Requête pour un film spécifique par ID
    try {
      List<RentalsDTO> rentalsEntity = Movie.getMoviesRentedByUserId(userId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(rentalsEntity));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
