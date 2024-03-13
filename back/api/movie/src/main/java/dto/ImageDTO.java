package dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImageDTO {
  private int id;
  private int movie_id;
  private String image_url;
  private Boolean main_image;

  public ImageDTO(int id, int movie_id, String image_url, Boolean main_image) {
    this.id = id;
    this.movie_id = movie_id;
    this.image_url = image_url;
    this.main_image = main_image;
  }
}
