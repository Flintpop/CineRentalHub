package mariadbPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "shopping_cart", schema = "cine_rental_hub")
public class ShoppingCartPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "cart_type", nullable = false)
  private Object cartType;
  @Basic
  @Column(name = "rental_duration", nullable = false)
  private int rentalDuration;
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

    ShoppingCartPojo that = (ShoppingCartPojo) o;

    if (id != that.id) return false;
    if (rentalDuration != that.rentalDuration) return false;
    if (cartType != null ? !cartType.equals(that.cartType) : that.cartType != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (cartType != null ? cartType.hashCode() : 0);
    result = 31 * result + rentalDuration;
    return result;
  }

}
