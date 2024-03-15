package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartPostDTO {
  private int user_id;
  private int movie_id;
  private String cart_type;
  private int rental_duration;

  public CartPostDTO(Integer user_id, Integer movie_id, String cart_type, Integer rental_duration) {
    this.user_id = user_id;
    this.movie_id = movie_id;
    this.cart_type = cart_type;
    this.rental_duration = rental_duration;
  }
}
