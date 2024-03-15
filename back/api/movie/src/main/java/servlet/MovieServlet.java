package servlet;

import com.google.gson.Gson;
import dto.MovieDTO;
import dto.MoviePostPutDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Integer movieId;
    try {
      movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, false);
    } catch (IdMissingException e) {
      // Aucun ID spécifique, renvoie la liste de tous les films
      List<MovieDTO> movies = Movie.getListMovie();
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(movies));
      return;
    } catch (IdValidationException | NumberFormatException e) {
      return; // Erreur déjà envoyée
    }

    // Requête pour un film spécifique par ID
    MovieDTO movieEntity = Movie.getMovieById(movieId);
    if (movieEntity == null) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"Film non trouvé.\"}");
    } else {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(movieEntity));
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      MoviePostPutDTO movieEntity = ServletUtils.readRequestBodyAndGetObject(request, MoviePostPutDTO.class);
      // Ici, implémentez la validation du film si nécessaire
      MoviePostPutDTO createdMovie = Movie.addMovie(movieEntity);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(createdMovie));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId;
    try {
      movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // Erreur déjà envoyée
    }

    try {
      MoviePostPutDTO movieToUpdate = ServletUtils.readRequestBodyAndGetObject(request, MoviePostPutDTO.class);
      MovieDTO updatedMovie = Movie.updateMovie(movieId, movieToUpdate);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(updatedMovie));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
