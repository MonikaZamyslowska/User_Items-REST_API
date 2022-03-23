package interview.vertx.rest_api.repository;

import interview.vertx.rest_api.model.Item;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.concurrent.Future;

public interface ItemRepository {
  Future<String> save(Item item, String userId);
  Future<List<JsonObject>> findAllUserItems(String userId);
}
