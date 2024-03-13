package mariadbPojo;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "purchases", schema = "cine_rental_hub")
public class PurchasesPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "purchase_date", nullable = false)
  private Date purchaseDate;
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

  public Date getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(Date purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PurchasesPojo that = (PurchasesPojo) o;

    if (id != that.id) return false;
    if (purchaseDate != null ? !purchaseDate.equals(that.purchaseDate) : that.purchaseDate != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
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
