package model;

import database.MariaDB;
import dto.ImageDTO;
import dto.MoviePostPutDTO;
import jakarta.persistence.*;
import mariadbPojo.MoviesPojo;

import java.util.List;
import java.util.stream.Collectors;

public class Movie {
  public static dto.MovieDTO getMovieById(Integer id) {
    EntityManager em = MariaDB.getEntityManager();
    try {
      MoviesPojo movie = em.find(MoviesPojo.class, id);
      if (movie != null) {
        return new dto.MovieDTO(movie);
      }
    } finally {
      em.close();
    }
    return null;
  }

  // Les autres méthodes peuvent être adaptées de manière similaire pour retourner ou accepter des DTO au lieu de MoviesPojo
  public static List<dto.MovieDTO> getListMovie() {
    EntityManager em = MariaDB.getEntityManager();
    try {
      List<MoviesPojo> movies = em.createQuery("SELECT m FROM MoviesPojo m", MoviesPojo.class).getResultList();
      return movies.stream().map(dto.MovieDTO::new).collect(Collectors.toList());
    } finally {
      em.close();
    }
  }

  /**
   * Ajoute un film à la base de données.
   *
   * @param movieDTO Le film à ajouter.
   * @return Le film ajouté.
   * @throws Exception Si une erreur survient lors de l'ajout du film.
   */
  public static MoviePostPutDTO addMovie(MoviePostPutDTO movieDTO) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      /*
      JSON de la requête HTTP POST:

      {
        "title": "Le titre du film",
        "release_date": "2021-01-01",
        "description": "La description du film",
        "daily_rental_price": 10.99,
        "purchase_price": 19.99,
        "link": "https://www.youtube.com/watch?v=123456"
      }


       */
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
      ModelUtils.generateException(e);
      return null;
    } finally {
      em.close();
    }
  }

  public static dto.MovieDTO updateMovie(Integer movieId, MoviePostPutDTO movieDTO) throws Exception {
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
      return new dto.MovieDTO(em.find(MoviesPojo.class, movieId));
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      ModelUtils.generateException(e);
      return null;
    } finally {
      em.close();
    }
  }

  public static ImageDTO getMainImage(Integer movieId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("get_main_image_by_movie_id")
              .registerStoredProcedureParameter("id_movie", Integer.class, ParameterMode.IN)
              .setParameter("id_movie", movieId);

      @SuppressWarnings("unchecked")
      List<Object[]> result = query.getResultList();
      if (!result.isEmpty()) {
        Object[] row = result.get(0);
        // Hack car bug, pour le get de main image, le 4e paramètre est un Byte et non un boolean
        Boolean isMain = (Byte) row[3] == 1;
        ImageDTO image = new ImageDTO((Integer) row[0], (Integer) row[1], (String) row[2], isMain);

        em.getTransaction().commit(); // Ajout du commit ici
        return image;
      } else {
        em.getTransaction().rollback(); // Rollback en cas de non résultat
        throw new Exception("No main image found for movie with id " + movieId);
      }
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      ModelUtils.generateException(e);
      return null;
    } finally {
      em.close();
    }
  }

  public static List<ImageDTO> getImagesByMovieId(Integer movieId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("get_images_by_movie_id")
              .registerStoredProcedureParameter("id_movie", Integer.class, ParameterMode.IN)
              .setParameter("id_movie", movieId);

      @SuppressWarnings("unchecked")
      List<Object[]> result = query.getResultList();
      em.getTransaction().commit();

      return result.stream().map(row -> new ImageDTO((Integer) row[0], (Integer) row[1], (String) row[2], (Boolean) row[3])).collect(Collectors.toList());
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

  public static void activateMovie(Integer movieId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("enable_movie")
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

  public static void addImage(Integer movieId, String link) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("add_image_to_movie")
              .registerStoredProcedureParameter("_movie_id", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("_image_url", String.class, ParameterMode.IN)
              .setParameter("_movie_id", movieId)
              .setParameter("_image_url", link);

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

  public static void deleteImage(Integer imageId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("delete_image_by_id")
              .registerStoredProcedureParameter("_image_id", Integer.class, ParameterMode.IN)
              .setParameter("_image_id", imageId);

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

  public static void setMainImage(Integer id_movie, Integer id_image) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("set_main_image")
              .registerStoredProcedureParameter("id_movie", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("id_image", Integer.class, ParameterMode.IN)
              .setParameter("id_movie", id_movie)
              .setParameter("id_image", id_image);

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
