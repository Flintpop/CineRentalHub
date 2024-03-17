package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPostDTO {
  private String cart_type;
  private Integer user_id;
  private Integer movie_id;
  private Integer rental_duration;

  public CartPostDTO(String cart_type, Integer user_id, Integer movie_id, Integer rental_duration) {
    this.cart_type = cart_type;
    this.user_id = user_id;
    this.movie_id = movie_id;
    this.rental_duration = rental_duration;
  }
}
