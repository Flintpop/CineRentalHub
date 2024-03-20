package servlet;

import com.google.gson.Gson;
import dto.CommentPutDTO;
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer commentId;
    try {
      commentId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdValidationException | NumberFormatException | IdMissingException e) {
      return; // Erreur déjà envoyée
    }

    try {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(Comment.getCommentById(commentId)));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

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
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
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
      CommentPutDTO commentEntity = ServletUtils.readRequestBodyAndGetObject(request, CommentPutDTO.class);
      CommentPutDTO updateComment = Comment.updateCommentById(commentId, commentEntity);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(updateComment));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);

    }
  }
}
