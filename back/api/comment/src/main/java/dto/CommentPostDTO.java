package dto;


import lombok.Getter;
import lombok.Setter;
import mariadbPojo.CommentsPojo;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentPostDTO {

  private Integer movie_id;
  private Integer user_id;
  private String comment_text;

  public CommentPostDTO(CommentsPojo comment) {
    this.movie_id = comment.getMoviesByMovieId().getId();
    this.user_id = comment.getUsersByUserId().getId();
    this.comment_text = comment.getCommentText();
  }

  public CommentPostDTO(Integer movieId, Integer userId, String commentText) {
    this.movie_id = movieId;
    this.user_id = userId;
    this.comment_text = commentText;
  }
}
