package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.CommentsPojo;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentGetDTO {

  private Integer id;
  private Integer movie_id;
  private Integer user_id;
  private String comment_text;
  private Timestamp comment_date;
  private String image_text;

  public CommentGetDTO(Integer id, Integer movie_id, Integer user_id, String comment_text, Timestamp comment_date, String image_text) {
    this.id = id;
    this.movie_id = movie_id;
    this.user_id = user_id;
    this.comment_text = comment_text;
    this.comment_date = comment_date;
    this.image_text = image_text;
  }

}
