package model;

import com.mongodb.client.MongoCollection;
import database.MongoDB;
import dto.StatsDTO;
import dto.StatsPostDTO;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static database.DatabaseUtils.convertToList;


public class Stats {

  public static List<StatsDTO> getAllStats() {
    MongoCollection<StatsDTO> collection = MongoDB.getCollection();
    return convertToList(collection);
  }

  public static void addStats(StatsPostDTO statsPostDTO) {
    MongoCollection<StatsDTO> collection = MongoDB.getCollection();
    StatsDTO statsDTOToInsert = new StatsDTO(statsPostDTO);
    statsDTOToInsert.setId(getNextStatsId());
    collection.insertOne(statsDTOToInsert);
  }

  private static Integer getNextStatsId() {
    List<StatsDTO> listStats = getAllStats();
    Integer maxId = 0;
    for (StatsDTO stats : listStats) {
      if (stats.getId() > maxId) {
        maxId = stats.getId();
      }
    }
    return maxId + 1;
  }

  public static void deleteStats(Integer statsId) throws IllegalArgumentException {
    MongoCollection<StatsDTO> collection = MongoDB.getCollection();

    StatsDTO statsDTO = getCommentaireById(statsId);
    if (statsDTO == null) {
      throw new IllegalArgumentException("Stat non trouvée.");
    }
    collection.deleteOne(new org.bson.Document("_id", statsId));
  }

  public static StatsDTO getCommentaireById(Integer id) {
    MongoCollection<StatsDTO> collection = MongoDB.getCollection();
    Bson filter = eq("_id", id);
    return collection.find(filter).first();
  }
}