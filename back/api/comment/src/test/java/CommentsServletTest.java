import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CommentsServletTest {

  private final OkHttpClient client = new OkHttpClient();
  private final String baseUrl = "http://localhost:8080/comments"; // Ajustez selon votre configuration
  private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  @Test
  public void testGetCommentsByMovieId() throws IOException {
    Request request = new Request.Builder()
            .url(baseUrl + "/1") // Supposons que 1 est un ID de film valide
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
    }
  }

  @Test
  public void testPostComment() throws IOException {
    String json = "{\"movie_id\": 1, \"user_id\": 1, \"comment_text\": \"Great movie!\"}";
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
            .url(baseUrl)
            .post(body)
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_CREATED, response.code());
    }
  }
}
