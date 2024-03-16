package servlet;

import com.google.gson.Gson;
import dto.StatsPostDTO;
import exceptions.IdMissingException;
import exceptions.IdValidationException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Stats;
import dto.StatsDTO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "StatsServlet", value = "/stats/*")
public class StatsServlet extends HttpServlet {
  Gson gson = new Gson();
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      List<StatsDTO> stats = Stats.getAllStats();
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, gson.toJson(stats));
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      StatsPostDTO statsPostDTO = ServletUtils.readRequestBodyAndGetObject(request, StatsPostDTO.class);
      Stats.addStats(statsPostDTO);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_CREATED, "{\"message\":\"Stat créée avec succès\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Integer statsId;
    try {
      statsId = ServletUtils.extractAndValidateId(request.getPathInfo(), response, true);
    } catch (NumberFormatException | IdMissingException | IdValidationException e) {
      return;
    }

    try {
      Stats.deleteStats(statsId);
      ServletUtils.sendJsonResponse(response, HttpServletResponse.SC_OK, "{\"message\":\"Stat supprimée avec succès\"}");
    } catch (Exception e) {
      ServletUtils.sendErrorJsonResponseWithTraceback(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
    }
  }
}
