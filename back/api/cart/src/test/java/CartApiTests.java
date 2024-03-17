import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CartApiTests {

    private final OkHttpClient client = new OkHttpClient();
    private final String baseUrl = "http://localhost:8080/cart"; // Ajustez l'URL en fonction de votre environnement
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Test
    public void testCreateCartSuccess() throws IOException {
        // Suppression du panier s'il existe déjà existant

        String json = "{\"user_id\": 1, \"movie_id\": 1, \"cart_type\": \"rental\", \"rental_duration\": 3}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            if (response.code() == 409) {
                // Le panier existe déjà
                throw new IllegalStateException("Le panier existe déjà");
            }
            Assertions.assertEquals(201, response.code());
        }
    }

    @Test
    public void testCreateCartInvalidUserId() throws IOException {
        String json = "{\"user_id\": \"sdfa\", \"movie_id\": 1, \"cart_type\": \"rental\", \"rental_duration\": 3}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }

    @Test
    public void testCreateCartMissingField() throws IOException {
        String json = "{\"user_id\": 1, \"cart_type\": \"rental\", \"rental_duration\": 3}"; // movie_id manquant
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }

    @Test
    public void testGetCartByInvalidUserId() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/-1")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }
    @Test
    public void testDeleteEntireCartSuccess() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/delete/1") // Adaptez selon l'endpoint correct
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertEquals(200, response.code());
        }
    }

    @Test
    public void testDeleteCartByInvalidUserId() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/delete/-1")
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }

    @Test
    public void testGetCartByUserIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/2")
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertEquals(200, response.code());
        }
    }

    @Test
    public void testGetCartByUserIdThatDoesNotExist() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/999") // Supposons que l'utilisateur 999 n'existe pas
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }

    @Test
    public void testValidateCartByUserIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/validate/3")
                .patch(RequestBody.create("", JSON)) // Adaptez si nécessaire
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertEquals(200, response.code());
        }
    }

    @Test
    public void testValidateCartByInvalidUserId() throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl + "/validate/-1")
                .post(RequestBody.create("", JSON))
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }

    @Test
    public void testCreateCartEmptyField() throws IOException {
        String json = "{\"user_id\": 1, \"movie_id\": 1, \"cart_type\": \"\", \"rental_duration\": 3}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response: " + response.body().string());
            Assertions.assertNotEquals(200, response.code());
        }
    }
}
