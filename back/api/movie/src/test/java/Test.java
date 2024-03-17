import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dto.MovieDTO;
import dto.MoviePostPutDTO;
import lombok.Getter;
import lombok.Setter;
import mariadbPojo.MoviesPojo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Test {

  public static void main(String[] args) {
//    String json = "{\"date\":\"Jan 21, 2023\"}";
    String json = "{ \"title\": \"New Moviee\", \"release_date\": \"Jan 21, 2023\", \"daily_rental_price\": 5.99, \"purchase_price\": 19.99, \"description\": \"A new movie description\", \"link\": \"https://www.example.com\"}";
    GsonBuilder builder = new GsonBuilder();
    JsonDeserializer<Date> deserializer = (jsonDeserialize, typeOfT, context) -> {
      try {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(jsonDeserialize.getAsJsonPrimitive().getAsString());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    };
    Gson gson = builder.registerTypeAdapter(Date.class, deserializer).create();
    MoviePostPutDTO obj = gson.fromJson(json, MoviePostPutDTO.class);
    System.out.println(obj.getRelease_date());
  }

  @Setter
  @Getter
  static class MyClass {
    private String title;
    private Date release_date;
    private BigDecimal daily_rental_price;
    private BigDecimal purchase_price;
    private String description;
    private String link;

//    public MyClass() {
//    }
//
//    public MyClass(String title, Date release_date, BigDecimal daily_rental_price, BigDecimal purchase_price, String description, String link) {
//      this.title = title;
//      this.release_date = release_date;
//      this.daily_rental_price = daily_rental_price;
//      this.purchase_price = purchase_price;
//      this.description = description;
//      this.link = link;
//    }

    public MyClass(MoviesPojo movie) {
      this.title = movie.getTitle();
      this.release_date = movie.getReleaseDate();
      this.daily_rental_price = movie.getDailyRentalPrice();
      this.purchase_price = movie.getPurchasePrice();
      this.description = movie.getDescription();
      this.link = movie.getLink();
    }
  }
}
