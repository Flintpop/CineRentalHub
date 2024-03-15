package dto;

import lombok.Getter;
import lombok.Setter;
import mariadbPojo.CommentsPojo;

@Getter
@Setter
public class CommentDTO {

  private int id;
  private int movie_id;
  private int user_id;
  private String comment_text;
  private String comment_date;

  public CommentDTO(CommentsPojo comment) {
    this.id = comment.getId();
    this.movie_id = comment.getMoviesByMovieId().getId();
    this.user_id = comment.getUsersByUserId().getId();
    this.comment_text = comment.getCommentText();
    this.comment_date = comment.getCommentDate().toString();
  }

  public CommentDTO(int id, int movie_id, int user_id, String comment_text, String comment_date) {
    this.id = id;
    this.movie_id = movie_id;
    this.user_id = user_id;
    this.comment_text = comment_text;
    this.comment_date = comment_date;
  }

}
