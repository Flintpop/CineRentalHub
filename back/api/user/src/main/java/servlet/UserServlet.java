package servlet;

import com.google.gson.Gson;
import dto.UserLowDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<UserLowDTO> users = User.getAllUsers();
    ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(users));
  }

  // Ajoutez ici des méthodes doPost, doPut, doDelete pour la création, mise à jour, suppression des utilisateurs
}
