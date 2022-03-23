package interview.vertx.rest_api.repository;

import interview.vertx.rest_api.model.User;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.Future;

public interface UserRepository {
  Future<String> save(User user);
  Future<JsonObject> findUserByLogin(String login);
}
