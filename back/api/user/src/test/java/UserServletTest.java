import jakarta.servlet.http.HttpServletResponse;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class UserServletTest {

    private final OkHttpClient client = new OkHttpClient();
    private final String baseUrl = "http://localhost:8080/user"; // Ajustez selon votre configuration
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Test
    public void testGetUser() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/2") // Supposons que 1 est un ID d'utilisateur valide
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
        }
    }

    @Test
    public void testCreateUser() throws IOException {
        String json = "{\"last_name\": \"Doe\", \"first_name\": \"John\", \"email\": \"john.doe@example.com\", \"password\": \"password123\", \"role\": \"USER\"}";
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
    public void testUpdateUser() throws IOException {
        String json = "{\"last_name\": \"Doe\", \"first_name\": \"Jane\", \"email\": \"jane.doe@example.com\", \"role\": \"admin\"}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseUrl + "/2") // Supposons que 1 est un ID d'utilisateur valide
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
        }
    }

    @Test
    public void testDeleteUser() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/2") // Supposons que 1 est un ID d'utilisateur valide
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            Assertions.assertEquals(HttpServletResponse.SC_OK, response.code());
        }
    }

    @Test
    public void testDeleteUserFailProtectedUser() {
        Request request = new Request.Builder()
                .url(baseUrl + "/1")
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            Assertions.assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
