package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.CommentsPojo;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentDTO {

  private Integer id;
  private Integer movie_id;
  private Integer user_id;
  private String comment_text;
  private Timestamp comment_date;

  public CommentDTO(CommentsPojo comment) {
    this.id = comment.getId();
    this.movie_id = comment.getMoviesByMovieId().getId();
    this.user_id = comment.getUsersByUserId().getId();
    this.comment_text = comment.getCommentText();
    this.comment_date = comment.getCommentDate();
  }

  public CommentDTO(Integer id, Integer movie_id, Integer user_id, String comment_text, Timestamp comment_date) {
    this.id = id;
    this.movie_id = movie_id;
    this.user_id = user_id;
    this.comment_text = comment_text;
    this.comment_date = comment_date;
  }

}
