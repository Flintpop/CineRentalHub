package dto;

import mariadbPojo.MoviesPojo;

import java.math.BigDecimal;
import java.sql.Date;

public class MovieDTO {
  private int id;
  private String title;
  private Date releaseDate;
  private BigDecimal dailyRentalPrice;
  private BigDecimal purchasePrice;
  private String description;
  private String link;

  // Constructeur qui accepte un MoviesPojo
  public MovieDTO(MoviesPojo movie) {
    this.id = movie.getId();
    this.title = movie.getTitle();
    this.releaseDate = movie.getReleaseDate();
    this.dailyRentalPrice = movie.getDailyRentalPrice();
    this.purchasePrice = movie.getPurchasePrice();
    this.description = movie.getDescription();
    this.link = movie.getLink();
  }

  // Getters et Setters
}
