package mariadbPojo;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_cart", schema = "cine_rental_hub", catalog = "")
public class ShoppingCartPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "cart_type", nullable = false)
  private Object cartType;
  @Basic
  @Column(name = "user_id", nullable = false)
  private int userId;
  @Basic
  @Column(name = "movie_id", nullable = false)
  private int movieId;
  @Basic
  @Column(name = "rental_duration", nullable = false)
  private int rentalDuration;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private UsersPojo usersByUserId;
  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
  private MoviesPojo moviesByMovieId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Object getCartType() {
    return cartType;
  }

  public void setCartType(Object cartType) {
    this.cartType = cartType;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getMovieId() {
    return movieId;
  }

  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  public int getRentalDuration() {
    return rentalDuration;
  }

  public void setRentalDuration(int rentalDuration) {
    this.rentalDuration = rentalDuration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ShoppingCartPojo that = (ShoppingCartPojo) o;

    if (id != that.id) return false;
    if (userId != that.userId) return false;
    if (movieId != that.movieId) return false;
    if (rentalDuration != that.rentalDuration) return false;
    if (cartType != null ? !cartType.equals(that.cartType) : that.cartType != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (cartType != null ? cartType.hashCode() : 0);
    result = 31 * result + userId;
    result = 31 * result + movieId;
    result = 31 * result + rentalDuration;
    return result;
  }

  public UsersPojo getUsersByUserId() {
    return usersByUserId;
  }

  public void setUsersByUserId(UsersPojo usersByUserId) {
    this.usersByUserId = usersByUserId;
  }

  public MoviesPojo getMoviesByMovieId() {
    return moviesByMovieId;
  }

  public void setMoviesByMovieId(MoviesPojo moviesByMovieId) {
    this.moviesByMovieId = moviesByMovieId;
  }
}
