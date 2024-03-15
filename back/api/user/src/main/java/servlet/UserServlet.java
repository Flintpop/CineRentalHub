package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dto.UserLowDTO;
import dto.UserPostDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dto.UserDTO;
import model.User;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer userId;
    try {
      userId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    try {
      UserDTO user = User.getUser(userId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(user));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String jsonBody = ServletUtils.readRequestBody(request);

    try {
      UserPostDTO userDto = gson.fromJson(jsonBody, UserPostDTO.class);
      userDto.hashPassword();
      UserPostDTO userCreated = User.createUser(userDto);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(userCreated));
    } catch (JsonSyntaxException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "" +
              "\ntraceback:" + Arrays.toString(e.getStackTrace()) + "\"}");
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer userId;
    try {
      userId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    String jsonBody = ServletUtils.readRequestBody(request);

    try {
      UserLowDTO userDto = gson.fromJson(jsonBody, UserLowDTO.class);
      UserLowDTO userUpdated = User.updateUser(userId, userDto);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(userUpdated));
    } catch (JsonSyntaxException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
              "{\"error\":\"" + e.getMessage() +
              "\n\"traceback:\" \"" + Arrays.toString(e.getStackTrace()) + "\"\"}");
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer userId;
    try {
      userId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (IdMissingException | IdValidationException | NumberFormatException e) {
      return; // L'erreur a déjà été envoyée
    }

    try {
      User.disableUser(userId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"message\":\"Utilisateur désactivé.\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"" + e.getMessage() + "\"}");
    }
  }
}
