package model;

import database.MariaDB;
import dto.CartDTO;
import dto.CartPostDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;

import java.util.List;

public class Cart {

  public static List<CartDTO> getCartByUserId(Integer userId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();
      // Utiliser la procédure
      StoredProcedureQuery query = em.createStoredProcedureQuery("get_user_cart")
              .registerStoredProcedureParameter("user_id", Integer.class, jakarta.persistence.ParameterMode.IN)
              .setParameter("user_id", userId);


      query.execute();

      @SuppressWarnings("unchecked")
      List<Object[]> results = query.getResultList();
      if (!results.isEmpty()) {
        em.getTransaction().commit();
        return results.stream().map((record) -> {
          Integer id = (Integer) record[0];
          String cartType = (String) record[1];
          Integer movieId = (Integer) record[2];
          Integer rentalDuration = (Integer) record[3];
          return new CartDTO(id, cartType, userId, movieId, rentalDuration);
        }).toList();
      } else {
        em.getTransaction().commit();
        return null;
      }
    } catch (PersistenceException e) {
      ModelUtils.generateException(e);
      return null;
    } finally {
      em.close();
    }
  }

  public static void createCart(CartPostDTO cart) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {

      /*
      JSon to test post the procedure :

      {
        "user_id": 1,
        "movie_id": 1,
        "cart_type": "rental",
        "rental_duration": 3
      }
       */
      em.getTransaction().begin();
      // Utiliser la procédure
      StoredProcedureQuery query = em.createStoredProcedureQuery("add_to_cart")
              .registerStoredProcedureParameter("user_id", Integer.class, jakarta.persistence.ParameterMode.IN)
              .registerStoredProcedureParameter("movie_id", Integer.class, jakarta.persistence.ParameterMode.IN)
              .registerStoredProcedureParameter("cart_type", String.class, jakarta.persistence.ParameterMode.IN)
              .registerStoredProcedureParameter("rental_duration", Integer.class, jakarta.persistence.ParameterMode.IN)
              .setParameter("user_id", cart.getUser_id())
              .setParameter("movie_id", cart.getMovie_id())
              .setParameter("cart_type", cart.getCart_type())
              .setParameter("rental_duration", cart.getRental_duration());

      query.execute();
    } catch (PersistenceException e) {
      ModelUtils.generateException(e);
    } finally {
      em.close();
    }
  }

  public static void deleteEntireCart(Integer userId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();
      // Utiliser la procédure
      StoredProcedureQuery query = em.createStoredProcedureQuery("delete_user_cart")
              .registerStoredProcedureParameter("user_id", Integer.class, jakarta.persistence.ParameterMode.IN)
              .setParameter("user_id", userId);

      query.execute();
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      ModelUtils.generateException(e);
    } finally {
      em.close();
    }
  }

  public static void validateCartByUseId(Integer userId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();
      // Utiliser la procédure
      StoredProcedureQuery query = em.createStoredProcedureQuery("validate_user_cart")
              .registerStoredProcedureParameter("user_id", Integer.class, jakarta.persistence.ParameterMode.IN)
              .setParameter("user_id", userId);

      query.execute();
      em.getTransaction().commit();
    } catch (PersistenceException e) {
      ModelUtils.generateException(e);
    } finally {
      em.close();
    }
  }
}