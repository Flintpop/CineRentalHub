package model;

import database.MariaDB;
import dto.CommentDTO;
import dto.CommentImagePostDTO;
import dto.CommentPostDTO;
import dto.CommentPutDTO;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class Comment {


  /**
   * Retourne la liste des commentaires d'un film
   *
   * @param movieId ID du film
   * @return Liste des commentaires
   */
  public static List<CommentDTO> getCommentsByMovieId(Integer movieId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("get_comments_by_movie_id")
              .registerStoredProcedureParameter("id_movie", Integer.class, ParameterMode.IN)
              .setParameter("id_movie", movieId);

      @SuppressWarnings("unchecked")
      List<Object[]> result = query.getResultList();
      em.getTransaction().commit();
      // retour:
      // id, comment_text, comment_date, user_id, last_name, first_name
      return result.stream().map(
              row -> new CommentDTO((Integer) row[0], movieId, (Integer) row[2], (String) row[3], (Timestamp) row[4])).collect(
              Collectors.toList());
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

  /**
   * Ajoute un commentaire
   *
   * @param commentEntity Commentaire à ajouter
   * @return Commentaire ajouté
   */

  // utilise la procédure stockée add_comment
  // CREATE PROCEDURE add_comment (IN movie_id INT, IN user_id INT, IN comment_text TEXT, IN comment_date DATETIME)
  public static CommentPostDTO addComment(CommentPostDTO commentEntity) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("add_comment")
              .registerStoredProcedureParameter("movie_id", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("user_id", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("comment_text", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("comment_date", java.sql.Timestamp.class, ParameterMode.IN)
              .setParameter("movie_id", commentEntity.getMovie_id())
              .setParameter("user_id", commentEntity.getUser_id())
              .setParameter("comment_text", commentEntity.getComment_text())
              .setParameter("comment_date", Timestamp.valueOf(java.time.LocalDateTime.now()));

      query.execute();
      em.getTransaction().commit();
      return new CommentPostDTO(commentEntity.getMovie_id(), commentEntity.getUser_id(), commentEntity.getComment_text());
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

  /**
   * Supprime un commentaire par ID
   *
   * @param commentId ID du commentaire
   */

  // utilise la procédure stockée delete_comment
  public static void deleteCommentById(Integer commentId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("delete_comment")
              .registerStoredProcedureParameter("comment_id", Integer.class, ParameterMode.IN)
              .setParameter("comment_id", commentId);

      query.execute();
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      ModelUtils.generateException(e);
    } finally {
      em.close();
    }

  }

  /**
   * Met à jour un commentaire par ID
   *
   * @param commentId  ID du commentaire
   * @param commentDTO Commentaire à mettre à jour
   * @return Commentaire mis à jour
   */
  public static CommentPutDTO updateCommentById(Integer commentId, CommentPutDTO commentDTO) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("update_comment_by_id")
              .registerStoredProcedureParameter("comment_id", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("comment_text", String.class, ParameterMode.IN)
              .setParameter("comment_id", commentId)
              .setParameter("comment_text", commentDTO.getComment_text());

      query.execute();
      em.getTransaction().commit();
      return new CommentPutDTO(commentDTO.getComment_text());
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

  public static void addImage(CommentImagePostDTO imagePostDTO) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("add_image_to_comment")
              .registerStoredProcedureParameter("comment_id", Integer.class, ParameterMode.IN)
              .registerStoredProcedureParameter("image_base64", String.class, ParameterMode.IN)
              .setParameter("comment_id", imagePostDTO.getComment_id())
              .setParameter("image_base64", imagePostDTO.getImage_base64());

      query.execute();
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      ModelUtils.generateException(e);
    } finally {
      em.close();
    }
  }

  public static void deleteImageFromComment(Integer commentId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("delete_image_from_comment")
              .registerStoredProcedureParameter("comment_id", Integer.class, ParameterMode.IN)
              .setParameter("comment_id", commentId);

      query.execute();
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      ModelUtils.generateException(e);
    } finally {
      em.close();
    }
  }

  public static Object getCommentById(Integer commentId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("get_comment_by_id")
              .registerStoredProcedureParameter("id_comment", Integer.class, ParameterMode.IN)
              .setParameter("id_comment", commentId);

      @SuppressWarnings("unchecked")
      List<Object[]> result = query.getResultList();
      em.getTransaction().commit();
      // retour:
      // id, comment_text, comment_date, user_id, last_name, first_name
      return result.stream().map(
              row -> new CommentDTO((Integer) row[0], (Integer) row[1], (Integer) row[2], (String) row[3], (Timestamp) row[4])).collect(
              Collectors.toList());
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
}