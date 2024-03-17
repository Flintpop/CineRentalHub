package model;

import database.MariaDB;
import dto.UserLowDTO;
import dto.UserPasswordDTO;
import dto.UserPostDTO;
import dto.UserPutDTO;
import jakarta.persistence.*;
import mariadbPojo.UsersPojo;

import java.util.List;
import java.util.stream.Collectors;

public class User {
  public static List<UserLowDTO> getAllUsers() {
    EntityManager em = MariaDB.getEntityManager();
    try {
      List<UsersPojo> userPojos = em.createQuery("SELECT u FROM UsersPojo u", UsersPojo.class).getResultList();
      return userPojos.stream().map(UserLowDTO::new).collect(Collectors.toList());
    } finally {
      em.close();
    }
  }

  public static UserLowDTO getUser(Integer userId) {
    EntityManager em = MariaDB.getEntityManager();
    try {
      UsersPojo usersPojo = em.find(UsersPojo.class, userId);
      if (usersPojo == null) {
        throw new EntityNotFoundException("L'utilisateur avec l'ID " + userId + " n'existe pas.");
      }
      return new UserLowDTO(usersPojo);
    } finally {
      em.close();
    }
  }

  public static UserPostDTO createUser(UserPostDTO userDto) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("create_user");
      query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
      query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
      query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
      query.registerStoredProcedureParameter(4, String.class, ParameterMode.IN);
      query.registerStoredProcedureParameter(5, String.class, ParameterMode.IN);

      query.setParameter(1, userDto.getLast_name());
      query.setParameter(2, userDto.getFirst_name());
      query.setParameter(3, userDto.getEmail());
      query.setParameter(4, userDto.getPassword());
      query.setParameter(5, userDto.getRole());


      query.execute();

      em.getTransaction().commit();
      return userDto;
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

  public static UserPutDTO updateUser(Integer userId, UserPutDTO userDto) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();

      /*
      Équivalent json pour modifier un utilisateur :

      {
        "last_name": "Doe",
        "first_name": "John",
        "email": "hey@jdskl",
        "activated": 1,
        "role": "admin"
      }
       */

      StoredProcedureQuery query = em.createStoredProcedureQuery("update_user")
              .registerStoredProcedureParameter("user_id", java.math.BigDecimal.class, ParameterMode.IN)
              .registerStoredProcedureParameter("new_last_name", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("new_first_name", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("new_email", String.class, ParameterMode.IN)
              .registerStoredProcedureParameter("new_role", String.class, ParameterMode.IN)
              .setParameter("user_id", userId)
              .setParameter("new_last_name", userDto.getLast_name())
              .setParameter("new_first_name", userDto.getFirst_name())
              .setParameter("new_email", userDto.getEmail())
              .setParameter("new_role", userDto.getRole());

      query.execute();

      em.getTransaction().commit();
      return userDto;
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

  // Disable user
  public static void deleteUser(Integer userId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("delete_user")
              .registerStoredProcedureParameter("user_id", java.math.BigDecimal.class, ParameterMode.IN)
              .setParameter("user_id", userId);

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

  // Update user password
  public static void updateUserPassword(Integer userId, String newPassword) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      em.getTransaction().begin();

      StoredProcedureQuery query = em.createStoredProcedureQuery("update_user_password")
              .registerStoredProcedureParameter("user_id", java.math.BigDecimal.class, ParameterMode.IN)
              .registerStoredProcedureParameter("new_password", String.class, ParameterMode.IN)
              .setParameter("user_id", userId)
              .setParameter("new_password", newPassword);

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

  public static UserPasswordDTO getUserPassword(Integer userId) throws Exception {
    EntityManager em = MariaDB.getEntityManager();

    try {
      UsersPojo user = em.find(UsersPojo.class, userId);
      if (user == null) {
        throw new EntityNotFoundException("L'utilisateur avec l'ID " + userId + " n'existe pas.");
      }

      return new UserPasswordDTO(user);
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