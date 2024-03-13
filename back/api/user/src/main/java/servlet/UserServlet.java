package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dto.UserDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mariadbPojo.UsersPojo;
import model.User;

import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = "/user/*")
public class UserServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String jsonBody = ServletUtils.readRequestBody(request);

    try {
      UserDTO userDto = gson.fromJson(jsonBody, UserDTO.class);
      // Validez ici les données de userDto si nécessaire.

      UsersPojo userCreated = User.createUser(userDto);

      if (userCreated != null) {
        ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, gson.toJson(new UserDTO(userCreated)));
      } else {
        ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\":\"Erreur lors de la création de l'utilisateur.\"}");
      }
    } catch (JsonSyntaxException e) {
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, "{\"error\":\"Format de données incorrect.\"}");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
