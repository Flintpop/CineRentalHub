
package database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Singleton pour la connexion à MariaDB
 */
public class MariaDB {
  private static final String PERSISTENCE_UNIT_NAME = "default";
  private static volatile EntityManagerFactory entityManagerFactory = null;

  private MariaDB() {
    // Constructeur privé pour éviter l'instanciation
  }

  public static EntityManager getEntityManager() {
    if (entityManagerFactory == null) {
      synchronized (MariaDB.class) {
        if (entityManagerFactory == null) {
          entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
          System.out.println("Connexion à MariaDB établie.");
        }
      }
    }
    return entityManagerFactory.createEntityManager();
  }
}
