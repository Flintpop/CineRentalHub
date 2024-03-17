import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MovieServletTest {

  private final OkHttpClient client = new OkHttpClient();
  private final String baseUrl = "http://localhost:8081/movies"; // Ajustez selon votre configuration
  private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  @Test
  public void testGetMovieById() throws IOException {
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
  public void testGetAllMovies() throws IOException {
    Request request = new Request.Builder()
            .url(baseUrl) // Pas d'ID spécifié pour récupérer tous les films
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
    }
  }

  @Test
  public void testAddMovie() throws IOException {
    String json = """
    {
      "available": 1,
      "title": "New Moviee",
      "release_date": "Jan 21, 2023",
      "daily_rental_price": 5.99,
      "purchase_price": 19.99,
      "description": "A new movie description",
      "link": "https://www.example.com"
    }
    """;
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

  @Test
  public void testUpdateMovie() throws IOException {
    String json = """
    {
      "available": 1,
      "title": "Updated Movie",
      "release_date": "Jan 21, 2021",
      "daily_rental_price": 5.99,
      "purchase_price": 19.99,
      "description": "An updated movie description",
      "link": "https://www.example.com"
    }
    """;
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
            .url(baseUrl + "/1") // Supposons que 1 est un ID de film valide à mettre à jour
            .put(body)
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println(response.body().string());
      Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
    }
  }
}
