package mariadbPojo;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
@Table(name = "users", schema = "cine_rental_hub")
public class UsersPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "last_name", nullable = false, length = 255)
  private String lastName;
  @Basic
  @Column(name = "first_name", nullable = false, length = 255)
  private String firstName;
  @Basic
  @Column(name = "email", nullable = false, length = 255)
  private String email;
  @Basic
  @Column(name = "password", nullable = false, length = 255)
  private String password;
  @Basic
  @Column(name = "role", nullable = false)
  private Object role;
  @OneToMany(mappedBy = "usersByUserId")
  private Collection<CommentsPojo> commentsById;
  @OneToMany(mappedBy = "usersByUserId")
  private Collection<PurchasesPojo> purchasesById;
  @OneToMany(mappedBy = "usersByUserId")
  private Collection<RentalsPojo> rentalsById;
  @OneToMany(mappedBy = "usersByUserId")
  private Collection<ShoppingCartPojo> shoppingCartsById;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Object getRole() {
    return role;
  }

  public void setRole(Object role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UsersPojo usersPojo = (UsersPojo) o;

    if (id != usersPojo.id) return false;
    if (lastName != null ? !lastName.equals(usersPojo.lastName) : usersPojo.lastName != null) return false;
    if (firstName != null ? !firstName.equals(usersPojo.firstName) : usersPojo.firstName != null) return false;
    if (email != null ? !email.equals(usersPojo.email) : usersPojo.email != null) return false;
    if (password != null ? !password.equals(usersPojo.password) : usersPojo.password != null) return false;
    if (role != null ? !role.equals(usersPojo.role) : usersPojo.role != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (password != null ? password.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
    return result;
  }

  public Collection<CommentsPojo> getCommentsById() {
    return commentsById;
  }

  public void setCommentsById(Collection<CommentsPojo> commentsById) {
    this.commentsById = commentsById;
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
}
