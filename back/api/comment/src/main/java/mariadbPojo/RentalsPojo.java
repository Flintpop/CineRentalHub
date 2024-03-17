package mariadbPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "rentals", schema = "cine_rental_hub")
public class RentalsPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "rental_date", nullable = false)
  private Timestamp rentalDate;
  @Basic
  @Column(name = "return_date", nullable = false)
  private Timestamp returnDate;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private UsersPojo usersByUserId;
  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
  private MoviesPojo moviesByMovieId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RentalsPojo that = (RentalsPojo) o;

    if (id != that.id) return false;
    if (rentalDate != null ? !rentalDate.equals(that.rentalDate) : that.rentalDate != null) return false;
    if (returnDate != null ? !returnDate.equals(that.returnDate) : that.returnDate != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (rentalDate != null ? rentalDate.hashCode() : 0);
    result = 31 * result + (returnDate != null ? returnDate.hashCode() : 0);
    return result;
  }

}
