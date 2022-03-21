package interview.vertx.rest_api.model;

import java.util.UUID;

public class User {
  private final UUID id;
  private String login;
  private String password;

  public User(String login, String password) {
    this.id = UUID.randomUUID();
    this.login = login;
    this.password = password;
  }
}
