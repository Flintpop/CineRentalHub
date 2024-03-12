package mariadbPojo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;

@Entity
@Table(name = "movies", schema = "cine_rental_hub", catalog = "")
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
  private Date releaseDate;
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
  @OneToMany(mappedBy = "moviesByMovieId")
  private Collection<ViewsPojo> viewsById;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public byte getAvailable() {
    return available;
  }

  public void setAvailable(byte available) {
    this.available = available;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public BigDecimal getDailyRentalPrice() {
    return dailyRentalPrice;
  }

  public void setDailyRentalPrice(BigDecimal dailyRentalPrice) {
    this.dailyRentalPrice = dailyRentalPrice;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(BigDecimal purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

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

  public Collection<CommentsPojo> getCommentsById() {
    return commentsById;
  }

  public void setCommentsById(Collection<CommentsPojo> commentsById) {
    this.commentsById = commentsById;
  }

  public Collection<ImagesPojo> getImagesById() {
    return imagesById;
  }

  public void setImagesById(Collection<ImagesPojo> imagesById) {
    this.imagesById = imagesById;
  }

  public Collection<PurchasesPojo> getPurchasesById() {
    return purchasesById;
  }

  public void setPurchasesById(Collection<PurchasesPojo> purchasesById) {
    this.purchasesById = purchasesById;
  }

  public Collection<RentalsPojo> getRentalsById() {
    return rentalsById;
  }

  public void setRentalsById(Collection<RentalsPojo> rentalsById) {
    this.rentalsById = rentalsById;
  }

  public Collection<ShoppingCartPojo> getShoppingCartsById() {
    return shoppingCartsById;
  }

  public void setShoppingCartsById(Collection<ShoppingCartPojo> shoppingCartsById) {
    this.shoppingCartsById = shoppingCartsById;
  }

  public Collection<ViewsPojo> getViewsById() {
    return viewsById;
  }

  public void setViewsById(Collection<ViewsPojo> viewsById) {
    this.viewsById = viewsById;
  }
}
