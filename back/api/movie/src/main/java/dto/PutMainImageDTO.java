package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutMainImageDTO {
  private int image_id;

  PutMainImageDTO() {
  }

  public PutMainImageDTO(int image_id) {
    this.image_id = image_id;
  }
}
