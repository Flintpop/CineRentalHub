package servlet;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "UserActivateServlet", urlPatterns = "/user/activate/*")
public class UserActivateServlet extends HttpServlet {

  private final Gson gson = new Gson();
}
