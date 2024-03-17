import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class UserPasswordServletTest {

  private final OkHttpClient client = new OkHttpClient();
  private final String baseUrl = "http://localhost:8080/user/password"; // Ajustez selon votre configuration
  private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  @Test
  public void testGetUserPassword() throws IOException {
    Request request = new Request.Builder()
            .url(baseUrl + "/1") // Supposons que 1 est un ID d'utilisateur valide
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
    }
  }

  @Test
  public void testUpdateUserPassword() throws IOException {
    String json = "{\"password\": \"newPassword123\"}";
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
            .url(baseUrl + "/2") // Supposons que 1 est un ID d'utilisateur valide pour lequel modifier le mot de passe
            .put(body)
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
    }
  }

  @Test
  public void testUpdateUserPasswordFailEmptyField() throws IOException {
    String json = "{\"password\": \"\"}";
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
            .url(baseUrl + "/2") // Supposons que 1 est un ID d'utilisateur valide pour lequel modifier le mot de passe
            .put(body)
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.code());
    }
  }
}
