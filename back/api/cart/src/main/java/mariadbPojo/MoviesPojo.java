package mariadbPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "movies", schema = "cine_rental_hub")
public class MoviesPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "available", nullable = false)
  private byte available;
  @Basic
  @Column(name = "title", nullable = false, length = 255)
  private String title;
  @Basic
  @Column(name = "release_date", nullable = true)
  private LocalDate releaseDate;
  @Basic
  @Column(name = "daily_rental_price", nullable = false, precision = 2)
  private BigDecimal dailyRentalPrice;
  @Basic
  @Column(name = "purchase_price", nullable = false, precision = 2)
  private BigDecimal purchasePrice;
  @Basic
  @Column(name = "description", nullable = false, length = -1)
  private String description;
  @Basic
  @Column(name = "link", nullable = false, length = 2000)
  private String link;
  @OneToMany(mappedBy = "moviesByMovieId")
  private Collection<CommentsPojo> commentsById;
  @OneToMany(mappedBy = "moviesByMovieId")
  private Collection<ImagesPojo> imagesById;
  @OneToMany(mappedBy = "moviesByMovieId")
  private Collection<PurchasesPojo> purchasesById;
  @OneToMany(mappedBy = "moviesByMovieId")
  private Collection<RentalsPojo> rentalsById;
  @OneToMany(mappedBy = "moviesByMovieId")
  private Collection<ShoppingCartPojo> shoppingCartsById;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MoviesPojo that = (MoviesPojo) o;

    if (id != that.id) return false;
    if (available != that.available) return false;
    if (title != null ? !title.equals(that.title) : that.title != null) return false;
    if (releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null) return false;
    if (dailyRentalPrice != null ? !dailyRentalPrice.equals(that.dailyRentalPrice) : that.dailyRentalPrice != null)
      return false;
    if (purchasePrice != null ? !purchasePrice.equals(that.purchasePrice) : that.purchasePrice != null) return false;
    if (description != null ? !description.equals(that.description) : that.description != null) return false;
    if (link != null ? !link.equals(that.link) : that.link != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (int) available;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
    result = 31 * result + (dailyRentalPrice != null ? dailyRentalPrice.hashCode() : 0);
    result = 31 * result + (purchasePrice != null ? purchasePrice.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (link != null ? link.hashCode() : 0);
    return result;
  }

}
