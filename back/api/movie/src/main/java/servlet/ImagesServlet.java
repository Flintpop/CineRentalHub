package servlet;

import com.google.gson.Gson;
import dto.ImageDTO;
import dto.ImagePostDTO;
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
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId;
    try {
      movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    try {
      ImagePostDTO image = ServletUtils.readRequestBodyAndGetObject(request, ImagePostDTO.class);
      Movie.addImage(movieId, image.getImage_url());
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(image));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer imageId;
    try {
      imageId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    try {
      Movie.deleteImage(imageId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"message\":\"Image supprimée.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
