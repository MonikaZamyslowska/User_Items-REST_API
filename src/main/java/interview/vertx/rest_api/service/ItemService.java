package interview.vertx.rest_api.service;

import interview.vertx.rest_api.model.Item;
import interview.vertx.rest_api.repository.ItemRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.UUID;

public class ItemService implements ItemRepository {
  private static final String COLLECTION_NAME = "items";
  private final MongoClient mongoClient;

  public ItemService(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Override
  public Future<String> save(Item item, String userId) {
    return mongoClient.save(COLLECTION_NAME,
      new JsonObject()
        .put("_id", UUID.randomUUID().toString())
        .put("owner", userId)
        .put("name", item.getName())
    );
  }

  @Override
  public Future<List<JsonObject>> findItemsByUser(String userId) {
    return mongoClient.find(COLLECTION_NAME,
      new JsonObject()
        .put("owner", userId));
  }
}
