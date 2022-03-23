package interview.vertx.rest_api.repository;

import interview.vertx.rest_api.model.User;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface UserRepository {
  Future<String> save(User user);
  Future<JsonObject> findUserByLogin(String login);
}
