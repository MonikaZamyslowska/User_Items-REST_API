package interview.vertx.rest_api.service;

import interview.vertx.rest_api.model.User;
import interview.vertx.rest_api.repository.UserRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserService implements UserRepository {
  private static final String COLLECTION_NAME = "users";
  private final MongoClient mongoClient;

  public UserService(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @Override
  public Future<String> save(User user) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    return mongoClient.save(COLLECTION_NAME,
      new JsonObject()
        .put("id", UUID.randomUUID().toString())
        .put("login", user.getLogin())
        .put("password", passwordEncoder.encode(user.getPassword())));
  }

  @Override
  public Future<JsonObject> findUserByLogin(String login) {
    return mongoClient.findOne(COLLECTION_NAME,
      new JsonObject().put("login", login),
      null);
  }
}
