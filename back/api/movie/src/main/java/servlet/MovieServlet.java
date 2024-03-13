package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dto.MovieDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Movie;
import mariadbPojo.MoviesPojo;
//import validation.MovieValidateur;
//import validation.ValidateurResultat;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MovieServlet", urlPatterns = "/movies/*")
public class MovieServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Integer movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, false);
    if (movieId == null) {
      return; // La validation de l'ID a échoué.
    }

    if (movieId == -1) {
      // Aucun ID spécifique, renvoie la liste de tous les films
      List<MovieDTO> movies = Movie.getListMovie();
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(movies));
      return;
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
    String jsonBody = ServletUtils.readRequestBody(request);
    try {
      MoviesPojo movieEntity = gson.fromJson(jsonBody, MoviesPojo.class);
      // Ici, implémentez la validation du film si nécessaire
      MoviesPojo createdMovie = Movie.createMovie(movieEntity);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(createdMovie));
    } catch (JsonSyntaxException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    if (movieId == null || movieId < 0) {
      return; // La validation de l'ID a échoué
    }

    String jsonBody = ServletUtils.readRequestBody(request);
    try {
      MoviesPojo movieToUpdate = gson.fromJson(jsonBody, MoviesPojo.class);
      MoviesPojo updatedMovie = Movie.updateMovie(movieId, movieToUpdate);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(updatedMovie));
    } catch (JsonSyntaxException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    }
  }
}
