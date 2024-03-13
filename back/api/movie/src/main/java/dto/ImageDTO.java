package dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageDTO {
  private int id;
  private int movie_id;
  private String imageUrl;
  private Boolean mainImage;

  public ImageDTO(int id, int movie_id, String imageUrl, Boolean mainImage) {
    this.id = id;
    this.movie_id = movie_id;
    this.imageUrl = imageUrl;
    this.mainImage = mainImage;
  }

}
