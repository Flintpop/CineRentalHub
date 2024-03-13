package model;

import database.MariaDB;
import dto.MovieDTO;
import dto.MoviePostPutDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;
import mariadbPojo.MoviesPojo;

import java.util.List;
import java.util.stream.Collectors;

public class Movie {
  public static MovieDTO getMovieById(Integer id) {
    EntityManager em = MariaDB.getEntityManager();
    try {
      MoviesPojo movie = em.find(MoviesPojo.class, id);
      if (movie != null) {
        return new MovieDTO(movie);
      }
    } finally {
      em.close();
    }
    return null;
  }

  // Les autres méthodes peuvent être adaptées de manière similaire pour retourner ou accepter des DTO au lieu de MoviesPojo
  public static List<MovieDTO> getListMovie() {
    EntityManager em = MariaDB.getEntityManager();
    try {
      List<MoviesPojo> movies = em.createQuery("SELECT m FROM MoviesPojo m", MoviesPojo.class).getResultList();
      return movies.stream().map(MovieDTO::new).collect(Collectors.toList());
    } finally {
      em.close();
    }
  }
  public static MoviePostPutDTO addMovie(MoviePostPutDTO movieDTO) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("add_movie")
              .registerStoredProcedureParameter("title", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("release_date", java.sql.Date.class, ParameterMode.IN)
              .registerStoredProcedureParameter("description", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("daily_rental_price", java.math.BigDecimal.class, ParameterMode.IN)
              .registerStoredProcedureParameter("purchase_price", java.math.BigDecimal.class, ParameterMode.IN)
              .registerStoredProcedureParameter("link", String.class, ParameterMode.IN)
              .setParameter("title", movieDTO.getTitle())
              .setParameter("release_date", movieDTO.getRelease_date())
              .setParameter("description", movieDTO.getDescription())
              .setParameter("daily_rental_price", movieDTO.getDaily_rental_price())
              .setParameter("purchase_price", movieDTO.getPurchase_price())
              .setParameter("link", movieDTO.getLink());

      query.execute();
      em.getTransaction().commit();
      return movieDTO;
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new Exception(e.getCause().getCause().getMessage());
    } finally {
      em.close();
    }
  }

  public static MovieDTO updateMovie(Integer movieId, MoviePostPutDTO movieDTO) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("update_movie")
              // Register parameters and set values here as in addMovie
              .registerStoredProcedureParameter("movie_id", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("title", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("release_date", java.sql.Date.class, ParameterMode.IN)
              .registerStoredProcedureParameter("description", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("daily_rental_price", java.math.BigDecimal.class, ParameterMode.IN)
              .registerStoredProcedureParameter("purchase_price", java.math.BigDecimal.class, ParameterMode.IN)
              .registerStoredProcedureParameter("link", String.class, ParameterMode.IN)
              .setParameter("movie_id", movieId)
              .setParameter("title", movieDTO.getTitle())
              .setParameter("release_date", movieDTO.getRelease_date())
              .setParameter("description", movieDTO.getDescription())
              .setParameter("daily_rental_price", movieDTO.getDaily_rental_price())
              .setParameter("purchase_price", movieDTO.getPurchase_price())
              .setParameter("link", movieDTO.getLink());

      query.execute();
      em.getTransaction().commit();
      return new MovieDTO(em.find(MoviesPojo.class, movieId));
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new Exception(e.getCause().getCause().getMessage());
    } finally {
      em.close();
    }
  }

  public static void deactivateMovie(Integer movieId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("disable_movie")
              .registerStoredProcedureParameter("_movie_id", Integer.class, ParameterMode.IN)
              .setParameter("_movie_id", movieId);

      query.execute();
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new Exception(e.getCause().getCause().getMessage());
    } finally {
      em.close();
    }
  }
}
