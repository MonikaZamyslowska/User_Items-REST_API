package interview.vertx.rest_api;

import interview.vertx.rest_api.model.Item;
import interview.vertx.rest_api.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainVerticle extends AbstractVerticle {
  private Map<UUID, User> users = new HashMap<>();
  private Map<UUID, Item> items = new HashMap<>();

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

    router.get("/api/users").handler(this::getAllUsers);
    router.get("/api/items").handler(this::getAllItems);

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

  public void createSomeData() {
    User user = new User("test", "password");
    User user1 = new User("test1", "password");

    Item item = new Item(user, "test");
    Item item1 = new Item(user1, "test1");

    users.put(user.getId(), user);
    users.put(user1.getId(), user1);

    items.put(item.getId(), item);
    items.put(item1.getId(), item1);
  }
}
