package mariadbPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "images", schema = "cine_rental_hub")
public class ImagesPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "image_url", nullable = false, length = 2000)
  private String imageUrl;
  @Basic
  @Column(name = "main_image", nullable = false)
  private byte mainImage;
  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
  private MoviesPojo moviesByMovieId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ImagesPojo that = (ImagesPojo) o;

    if (id != that.id) return false;
    if (mainImage != that.mainImage) return false;
    if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
    result = 31 * result + (int) mainImage;
    return result;
  }

}
