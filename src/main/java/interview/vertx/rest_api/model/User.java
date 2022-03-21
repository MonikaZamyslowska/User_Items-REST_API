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

  public UUID getId() {
    return id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
