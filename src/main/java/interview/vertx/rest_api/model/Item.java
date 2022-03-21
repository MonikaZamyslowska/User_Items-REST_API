package interview.vertx.rest_api.model;

import java.util.UUID;

public class Item {
  private final UUID id;
  private User owner;
  private String name;

  public Item(User owner, String name) {
    this.id = UUID.randomUUID();
    this.owner = owner;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
