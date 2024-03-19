package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentImagePostDTO {
  private Integer comment_id;
  private String image_base64;

  // Constructeur par défaut nécessaire pour la désérialisation
  public CommentImagePostDTO() {
  }

  public CommentImagePostDTO(String image_base64) {
    this.image_base64 = image_base64;
  }
}
