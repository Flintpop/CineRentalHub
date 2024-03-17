package dto;


import lombok.Getter;
import lombok.Setter;
import mariadbPojo.CommentsPojo;

@Getter
@Setter
public class CommentPutDTO {

  private String comment_text;

  public CommentPutDTO(CommentsPojo comment) {
    this.comment_text = comment.getCommentText();
  }

  public CommentPutDTO(String commentText) {
    this.comment_text = commentText;
  }
}
