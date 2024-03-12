package model;

import database.MariaDB;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import mariadbPojo.MoviesPojo;

import java.util.List;

public class Movie {
  public static MoviesPojo getMovieById(Integer id) {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.find(MoviesPojo.class, id);
    } finally {
      em.close();
    }

    return null;
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

  public static List<MoviesPojo> getListMovie() {
    EntityManager em = MariaDB.getEntityManager();
    try {
      return em.createQuery("SELECT m FROM MoviesPojo m", MoviesPojo.class).getResultList();
    } finally {
      em.close();
    }
  }
}
