package mariadbPojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments", schema = "cine_rental_hub")
public class CommentsPojo {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private int id;
  @Basic
  @Column(name = "comment_text", nullable = false, length = -1)
  private String commentText;
  @Basic
  @Column(name = "comment_date", nullable = false)
  private LocalDateTime commentDate;
  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
  private MoviesPojo moviesByMovieId;
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private UsersPojo usersByUserId;
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CommentsPojo that = (CommentsPojo) o;

    if (id != that.id) return false;
    if (commentText != null ? !commentText.equals(that.commentText) : that.commentText != null) return false;
    if (commentDate != null ? !commentDate.equals(that.commentDate) : that.commentDate != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (commentText != null ? commentText.hashCode() : 0);
    result = 31 * result + (commentDate != null ? commentDate.hashCode() : 0);
    return result;
  }
}
