import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ServletApiTests {

  private final OkHttpClient client = new OkHttpClient();
  private final String baseUrl = "http://localhost:8081"; // Adjust your API base URL here

  @Test
  public void testMovieDeactivated() throws IOException {
    String url = baseUrl + "/movies/deactivated/1"; // Example ID
    Request request = new Request.Builder()
            .url(url)
            .patch(RequestBody.create("", MediaType.parse("application/json")))
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testMovieDeactivated Response: " + response.body().string());
    }
  }

  @Test
  public void testMovieActivated() throws IOException {
    String url = baseUrl + "/movies/activated/1"; // Example ID
    Request request = new Request.Builder()
            .url(url)
            .patch(RequestBody.create("", MediaType.parse("application/json")))
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testMovieActivated Response: " + response.body().string());
    }
  }

  @Test
  public void testGetMainImage() throws IOException {
    String url = baseUrl + "/movies/main_image/1"; // Example ID
    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testGetMainImage Response: " + response.body().string());
    }
  }

  @Test
  public void testPutMainImage() throws IOException {
    String url = baseUrl + "/movies/main_image/1"; // Example ID
    String json = "{\"image_id\": \"2\"}"; // Example image ID to set as main
    Request request = new Request.Builder()
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testPutMainImage Response: " + response.body().string());
    }
  }

  @Test
  public void testGetImagesByMovieId() throws IOException {
    String url = baseUrl + "/movies/images/1"; // Example ID
    Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testGetImagesByMovieId Response: " + response.body().string());
    }
  }

  @Test
  public void testPostImage() throws IOException {
    String url = baseUrl + "/movies/images/1"; // Example ID
    String json = "{\"image_url\": \"https://example.com/image.jpg\"}"; // Example image URL to add
    Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testPostImage Response: " + response.body().string());
    }
  }

  @Test
  public void testDeleteImage() throws IOException {
    String url = baseUrl + "/movies/images/1"; // Example image ID to delete
    Request request = new Request.Builder()
            .url(url)
            .delete()
            .build();

    try (Response response = client.newCall(request).execute()) {
      System.out.println("testDeleteImage Response: " + response.body().string());
    }
  }
}
