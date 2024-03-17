package dto;

import lombok.Getter;
import lombok.Setter;

/*
JSON pour les stats post dans MongoDB :
{
  "user_id": 1,
  "movie_id": 1,
 */

@Getter
@Setter
public class StatsPostDTO {
  private Integer user_id;
  private Integer movie_id;

  public StatsPostDTO(Integer user_id, Integer movie_id) {
    this.user_id = user_id;
    this.movie_id = movie_id;
  }

  public StatsPostDTO() {
  }
}
