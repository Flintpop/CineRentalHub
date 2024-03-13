package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.MoviesPojo;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class MoviePostPutDTO {
  private String title;
  private Date release_date;
  private BigDecimal daily_rental_price;
  private BigDecimal purchase_price;
  private String description;
  private String link;

  // Constructeur qui accepte un MoviesPojo
  public MoviePostPutDTO(MoviesPojo movie) {
    this.title = movie.getTitle();
    this.release_date = movie.getReleaseDate();
    this.daily_rental_price = movie.getDailyRentalPrice();
    this.purchase_price = movie.getPurchasePrice();
    this.description = movie.getDescription();
    this.link = movie.getLink();
  }

  // Getters et Setters
}
