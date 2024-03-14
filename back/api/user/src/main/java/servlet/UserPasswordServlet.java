package servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "UserPasswordServlet", urlPatterns = "/user/password/*")
public class UserPasswordServlet extends HttpServlet {

  private final Gson gson = new Gson();
}
