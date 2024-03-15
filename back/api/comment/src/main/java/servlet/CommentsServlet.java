package servlet;

import com.google.gson.Gson;
import dto.CommentPostDTO;
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
    Integer movie_id;
    try {
      movie_id = ServletUtils.extractAndValidateId(request.getPathInfo(), response, false);
    } catch (IdValidationException | NumberFormatException | IdMissingException e) {
      return; // Erreur déjà envoyée
    }

    try {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(Comment.getCommentsByMovieId(movie_id)));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer movieId;
    try {
      movieId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, false);
    } catch (IdValidationException | NumberFormatException | IdMissingException e) {
      return; // Erreur déjà envoyée
    }
    try {
      CommentPostDTO commentEntity  = ServletUtils.readRequestBodyAndGetObject(request, CommentPostDTO.class);
      // Ici, implémentez la validation du commentaire si nécessaire
      CommentPostDTO createdComment = Comment.addComment(commentEntity);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(createdComment));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
