package dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PurchasesDTO {
  private Integer id;
  private Integer movie_id;
  private Byte available;
  private String title;
  private Date release_date;
  private String description;
  private BigDecimal daily_rental_price;
  private BigDecimal purchase_price;
  private String link;
  private Date purchase_date;


  public PurchasesDTO() {
  }

  public PurchasesDTO(Integer id, Integer movie_id, Byte available, String title,
                      Date release_date, String description, BigDecimal daily_rental_price,
                      BigDecimal purchase_price, String link, Date purchase_date) {
    this.id = id;
    this.movie_id = movie_id;
    this.available = available;
    this.title = title;
    this.release_date = release_date;
    this.description = description;
    this.daily_rental_price = daily_rental_price;
    this.purchase_price = purchase_price;
    this.link = link;
    this.purchase_date = purchase_date;
  }
}
