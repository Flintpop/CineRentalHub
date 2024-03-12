package model;

import database.MariaDB;
import dto.MovieDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
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

  public static MoviesPojo updateMovie(Integer movieId, MoviesPojo movieToUpdate) {
    return null;
  }

  public static MoviesPojo createMovie(MoviesPojo movieEntity) {
    return null;
  }

  public static boolean deleteMovie(Integer movieId) {
    return false;
  }
}
