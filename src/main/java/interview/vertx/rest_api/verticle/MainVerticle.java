package interview.vertx.rest_api.verticle;

import interview.vertx.rest_api.model.Item;
import interview.vertx.rest_api.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainVerticle extends AbstractVerticle {
  private final Map<UUID, User> users = new HashMap<>();
  private final Map<UUID, Item> items = new HashMap<>();
  private String id;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    createSomeData();

    Router router = Router.router(vertx);

    router.route("/").handler(routingContext -> {
      HttpServerResponse response = routingContext.response();
      response
        .putHeader("content-type", "text/html")
        .end("<h1>Hello from my first Vert.x 3 application</h1>");
    });

    router.route("/api/users*").handler(BodyHandler.create());
    router.route("/api/items*").handler(BodyHandler.create());

//    get all
    router.get("/api/users").handler(this::getAllUsers);
    router.get("/api/items").handler(this::getAllItems);

//    get one
    router.get("/api/users/:id").handler(this::getOneUser);
    router.get("/api/items/:id").handler(this::getOneItem);

//    create
    router.post("/api/users").handler(this::addOneUser);
    router.post("/api/items").handler(this::addOneItem);

//    update
    router.put("/api/users/:id").handler(this::updateOneUser);
    router.put("/api/items/:id").handler(this::updateOneItem);

//    delete
    router.delete("/api/users/:id").handler(this::deleteUser);
    router.delete("/api/items/:id").handler(this::deleteItem);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 8080");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private void updateOneItem(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    JsonObject json = routingContext.getBodyAsJson();
    if (id == null || json == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      final UUID idAsUuid = UUID.fromString(id);
      Item item = items.get(idAsUuid);
      if (item == null) {
        routingContext.response().setStatusCode(404).end();
      } else {
        item.setName(json.getString("name"));
        routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(item));
      }
    }
  }

  private void updateOneUser(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    JsonObject json = routingContext.getBodyAsJson();
    if (id == null || json == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      final UUID idAsUuid = UUID.fromString(id);
      User user = users.get(idAsUuid);
      if (user == null) {
        routingContext.response().setStatusCode(404).end();
      } else {
        user.setLogin(json.getString("login"));
        user.setPassword(json.getString("password"));
        routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(user));
      }
    }
  }

  private void getOneItem(RoutingContext routingContext) {
    id = getIdAsString(routingContext);
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      final UUID idAsUuid = UUID.fromString(id);
      Item item = items.get(idAsUuid);
      if (item == null) {
        routingContext.response().setStatusCode(404).end();
      } else {
        routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(item));
      }
    }
  }

  private void getOneUser(RoutingContext routingContext) {
    id = getIdAsString(routingContext);
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      final UUID idAsUuid = UUID.fromString(id);
      User user = users.get(idAsUuid);
      if (user == null) {
        routingContext.response().setStatusCode(404).end();
      } else {
        routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(user));
      }
    }
  }

  private void deleteItem(RoutingContext routingContext) {
    id = getIdAsString(routingContext);
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      UUID idAsUuid = UUID.fromString(id);
      items.remove(idAsUuid);
    }
    routingContext.response().setStatusCode(204).end();
  }

  private void deleteUser(RoutingContext routingContext) {
    id = getIdAsString(routingContext);
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      UUID idAsUuid = UUID.fromString(id);
      users.remove(idAsUuid);
    }
    routingContext.response().setStatusCode(204).end();
  }

  private void addOneUser(RoutingContext routingContext) {
    final User user = Json.decodeValue(routingContext.getBodyAsString(), User.class);
    user.setId(UUID.randomUUID());
    users.put(user.getId(), user);
    routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(user));
  }


  private void addOneItem(RoutingContext routingContext) {
    final Item item = Json.decodeValue(routingContext.getBodyAsString(), Item.class);
    item.setId(UUID.randomUUID());
    items.put(item.getId(), item);
    routingContext.response()
      .setStatusCode(201)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(item));
  }

  private void getAllItems(RoutingContext routingContext) {
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(items.values()));
  }

  private void getAllUsers(RoutingContext routingContext) {
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(users.values()));
  }

  private String getIdAsString(RoutingContext routingContext) {
    return routingContext.request().getParam("id");
  }

//  private void deleteItemsByUser() {
//    items.entrySet().stream()
//      .map(entry -> entry.getValue())
//      .filter(item -> item.getOwner().getId().equals(id))
//      .forEach(item -> items.remove(item.getId()));
//  }

  private void createSomeData() {
    User user = new User(UUID.randomUUID(),"test", "password");
    User user1 = new User(UUID.randomUUID(),"test1", "password");

//    Item item = new Item(UUID.randomUUID(),  "test");
//    Item item1 = new Item(UUID.randomUUID(),  "test1");

    users.put(user.getId(), user);
    users.put(user1.getId(), user1);
//
//    items.put(item.getId(), item);
//    items.put(item1.getId(), item1);
  }
}
