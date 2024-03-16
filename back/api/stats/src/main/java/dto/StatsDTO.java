package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

// VOici le json pour les stats dans mongodb :


/*
{
  "id": 1,
  "user_id": 1,
  "movie_id": 1,
  "view_date": "2020-01-01 20:00:00"
}
*/
@Getter
@Setter
public class StatsDTO {
  private Integer id;
  private Integer user_id;
  private Integer movie_id;
  private Date view_date;

  public StatsDTO(Integer id, Integer user_id, Integer movie_id, Date view_date) {
    this.id = id;
    this.user_id = user_id;
    this.movie_id = movie_id;
    this.view_date = view_date;
  }

  public StatsDTO(StatsPostDTO statsPostDTO) {
    this.user_id = statsPostDTO.getUser_id();
    this.movie_id = statsPostDTO.getMovie_id();
    this.view_date = new Date();
  }

  public StatsDTO() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatsDTO statsDTO = (StatsDTO) o;
    return id == statsDTO.id && user_id == statsDTO.user_id && movie_id == statsDTO.movie_id && Objects.equals(view_date, statsDTO.view_date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, user_id, movie_id, view_date);
  }

}
