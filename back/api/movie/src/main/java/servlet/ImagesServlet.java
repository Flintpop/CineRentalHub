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
import java.util.List;

@WebServlet(name = "ImagesServlet", urlPatterns = "/movies/images/*")
public class ImagesServlet extends HttpServlet {
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
      List<ImageDTO> mainImage = Movie.getImagesByMovieId(movieId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(mainImage));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
