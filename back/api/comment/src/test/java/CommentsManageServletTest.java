import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CommentsManageServletTest {

  private final OkHttpClient client = new OkHttpClient();
  private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private final String manageBaseUrl = "http://localhost:8080/comments/manage"; // Ajustez selon votre configuration

  @Test
  public void testDeleteComment() throws IOException {
    Request request = new Request.Builder()
            .url(manageBaseUrl + "/1") // Supposons que 1 est un ID de commentaire valide
            .delete()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_CREATED, response.code());
    }
  }

  @Test
  public void testUpdateComment() throws IOException {
    String json = "{\"comment_text\": \"Lol\"}";
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
            .url(manageBaseUrl + "/2") // Supposons que 1 est un ID de commentaire valide
            .put(body)
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_CREATED, response.code());
    }
  }
}
