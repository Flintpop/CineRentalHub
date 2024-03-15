package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
  private int id;
  private String cart_type;
  private int user_id;
  private int movie_id;
  private int rental_duration;

  public CartDTO(int id, String cart_type, int user_id, int movie_id, int rental_duration) {
    this.id = id;
    this.cart_type = cart_type;
    this.user_id = user_id;
    this.movie_id = movie_id;
    this.rental_duration = rental_duration;
  }
}
