package mariadbPojo;

import jakarta.persistence.*;

@Entity
@Table(name = "images", schema = "cine_rental_hub", catalog = "")
public class ImagesPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "movie_id", nullable = false)
  private int movieId;
  @Basic
  @Column(name = "image_url", nullable = false, length = 2000)
  private String imageUrl;
  @Basic
  @Column(name = "main_image", nullable = false)
  private byte mainImage;
  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
  private MoviesPojo moviesByMovieId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getMovieId() {
    return movieId;
  }

  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public byte getMainImage() {
    return mainImage;
  }

  public void setMainImage(byte mainImage) {
    this.mainImage = mainImage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ImagesPojo that = (ImagesPojo) o;

    if (id != that.id) return false;
    if (movieId != that.movieId) return false;
    if (mainImage != that.mainImage) return false;
    if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + movieId;
    result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
    result = 31 * result + (int) mainImage;
    return result;
  }

  public MoviesPojo getMoviesByMovieId() {
    return moviesByMovieId;
  }

  public void setMoviesByMovieId(MoviesPojo moviesByMovieId) {
    this.moviesByMovieId = moviesByMovieId;
  }
}
