package model;

import database.MariaDB;
import dto.UserLowDTO;
import jakarta.persistence.EntityManager;
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

  // Ajoutez ici d'autres méthodes pour créer, mettre à jour, supprimer des utilisateurs, etc.
}