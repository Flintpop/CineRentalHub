package servlet;

import com.google.gson.Gson;
import dto.CommentDTO;
import dto.CommentPostDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Comment;

import java.io.IOException;

@WebServlet(name = "CommentsManageServlet", urlPatterns = "/comments/manage/*")
public class CommentsManageServlet extends HttpServlet {
  private final Gson gson = new Gson();

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer commentId;
    try {
      commentId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdValidationException | NumberFormatException | IdMissingException e) {
      return; // Erreur déjà envoyée
    }

    try {
      Comment.deleteCommentById(commentId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, "{\"message\":\"Commentaire supprimé.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer commentId;
    try {
      commentId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdValidationException | NumberFormatException | IdMissingException e) {
      return; // Erreur déjà envoyée
    }

    try {
      CommentDTO commentEntity = ServletUtils.readRequestBodyAndGetObject(request, CommentDTO.class);
      CommentPostDTO updateComment = Comment.updateCommentById(commentId, commentEntity);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(updateComment));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);

    }
  }
}
