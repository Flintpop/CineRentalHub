package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

    Integer movieId = ServletUtils.extraireEtValiderId(request.getPathInfo(), response, false);
    if (movieId == null) {
      return; // La validation de l'ID a échoué.
    }

    if (movieId == -1) {
      // Aucun ID spécifique, renvoie la liste de tous les films
      List<MoviesPojo> movies = Movie.getListMovie();
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_OK, gson.toJson(movies));
      return;
    }

    // Requête pour un film spécifique par ID
    MoviesPojo movieEntity = Movie.getMovieById(movieId);
    if (movieEntity == null) {
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"Film non trouvé.\"}");
    } else {
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_OK, gson.toJson(movieEntity));
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String jsonBody = ServletUtils.lireCorpsRequete(request);
    try {
      MoviesPojo movieEntity = gson.fromJson(jsonBody, MoviesPojo.class);
      // Ici, implémentez la validation du film si nécessaire
      MoviesPojo createdMovie = Movie.createMovie(movieEntity);
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_CREATED, gson.toJson(createdMovie));
    } catch (JsonSyntaxException e) {
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId = ServletUtils.extraireEtValiderId(request.getPathInfo(), response, true);
    if (movieId == null || movieId < 0) {
      return; // La validation de l'ID a échoué
    }

    String jsonBody = ServletUtils.lireCorpsRequete(request);
    try {
      MoviesPojo movieToUpdate = gson.fromJson(jsonBody, MoviesPojo.class);
      MoviesPojo updatedMovie = Movie.updateMovie(movieId, movieToUpdate);
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_OK, gson.toJson(updatedMovie));
    } catch (JsonSyntaxException e) {
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId = ServletUtils.extraireEtValiderId(request.getPathInfo(), response, true);
    if (movieId == null || movieId < 0) {
      return; // La validation de l'ID a échoué.
    }

    try {
      boolean deleted = Movie.deleteMovie(movieId);
      if (deleted) {
        ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_NO_CONTENT, "");
      } else {
        ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"Film non trouvé.\"}");
      }
    } catch (Exception e) {
      ServletUtils.envoyerReponseJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Une erreur s'est produite.\"}");
    }
  }
}
