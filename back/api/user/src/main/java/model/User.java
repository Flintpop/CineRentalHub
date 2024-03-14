package model;

import database.MariaDB;
import dto.UserDTO;
import dto.UserLowDTO;
import jakarta.persistence.*;
import mariadbPojo.UsersPojo;
import org.hibernate.exception.GenericJDBCException;

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

  public static List<UserDTO> getUser() {
    EntityManager em = MariaDB.getEntityManager();
    try {
      List<UsersPojo> userPojos = em.createQuery("SELECT u FROM UsersPojo u", UsersPojo.class).getResultList();
      return userPojos.stream().map(UserDTO::new).collect(Collectors.toList());
    } finally {
      em.close();
    }
  }

  public static UsersPojo createUser(UserDTO userDto) throws Exception {
    EntityManager em = MariaDB.getEntityManager();
    UsersPojo user = null;

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
      query.setParameter(4, userDto.getPassword()); // Assurez-vous que le mot de passe est haché si nécessaire
      query.setParameter(5, userDto.getRole());


      query.execute();

      em.getTransaction().commit();
      return user;
    } catch (PersistenceException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new Exception(e.getCause().getCause().getMessage());
    } finally {
      em.close();
    }
  }

  // Ajoutez ici d'autres méthodes pour créer, mettre à jour, supprimer des utilisateurs, etc.
}