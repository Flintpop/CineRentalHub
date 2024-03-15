package servlet;

import com.google.gson.Gson;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Comment;

import java.io.IOException;

@WebServlet(name = "CommentsServlet", urlPatterns = "/comments/*")
public class CommentsServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer filmId;
    try {
      filmId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, false);
    } catch (IdValidationException | NumberFormatException | IdMissingException e) {
      return; // Erreur déjà envoyée
    }

    // Requête pour un commentaire spécifique par ID
    Comment commentEntity = Comment.getCommentsByFilmId(filmId);
    if (commentEntity == null) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_NOT_FOUND, "{\"error\":\"Commentaire non trouvé.\"}");
    } else {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(commentEntity));
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String jsonBody = ServletUtils.readRequestBody(request);
    try {
      Comment commentEntity = gson.fromJson(jsonBody, Comment.class);
      // Ici, implémentez la validation du commentaire si nécessaire
      Comment createdComment = Comment.addComment(commentEntity);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(createdComment));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
