package mariadbPojo;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "views", schema = "cine_rental_hub", catalog = "")
public class ViewsPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "user_id", nullable = false)
  private int userId;
  @Basic
  @Column(name = "movie_id", nullable = false)
  private int movieId;
  @Basic
  @Column(name = "view_date", nullable = false)
  private Timestamp viewDate;
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

  public Timestamp getViewDate() {
    return viewDate;
  }

  public void setViewDate(Timestamp viewDate) {
    this.viewDate = viewDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ViewsPojo viewsPojo = (ViewsPojo) o;

    if (id != viewsPojo.id) return false;
    if (userId != viewsPojo.userId) return false;
    if (movieId != viewsPojo.movieId) return false;
    if (viewDate != null ? !viewDate.equals(viewsPojo.viewDate) : viewsPojo.viewDate != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + userId;
    result = 31 * result + movieId;
    result = 31 * result + (viewDate != null ? viewDate.hashCode() : 0);
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
