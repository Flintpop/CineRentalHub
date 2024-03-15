package dto;


import lombok.Getter;
import lombok.Setter;
import mariadbPojo.CommentsPojo;

@Getter
@Setter
public class CommentPostDTO {

  private int movie_id;
  private int user_id;
  private String comment_text;
  private String comment_date;

  public CommentPostDTO(CommentsPojo comment) {
    this.movie_id = comment.getMoviesByMovieId().getId();
    this.user_id = comment.getUsersByUserId().getId();
    this.comment_text = comment.getCommentText();
    this.comment_date = comment.getCommentDate().toString();
  }

  public CommentPostDTO(int movieId, int userId, String commentText, String commentDate) {
    this.movie_id = movieId;
    this.user_id = userId;
    this.comment_text = commentText;
    this.comment_date = commentDate;
  }
}
