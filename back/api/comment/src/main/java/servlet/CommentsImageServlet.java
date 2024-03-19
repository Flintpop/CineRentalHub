package servlet;

import com.google.gson.Gson;
import dto.CommentImagePostDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Comment;

import java.io.IOException;

@WebServlet(name = "CommentsImageServlet", urlPatterns = "/comments/image/*")
public class CommentsImageServlet extends HttpServlet {

  Gson gson = new Gson();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      CommentImagePostDTO imagePostDTO = ServletUtils.readRequestBodyAndGetObject(request, CommentImagePostDTO.class);

      Comment.addImage(imagePostDTO);

      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson("{ \"message\": \"Image ajouté.\"}"));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      Integer commentId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
      Comment.deleteImageFromComment(commentId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson("{ \"message\": \"Image supprimée.\"}"));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
