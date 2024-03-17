package dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImagePostDTO {
  private String image_url;

  public ImagePostDTO(String image_url) {
    this.image_url = image_url;
  }
}
