package interview.vertx.rest_api.router;

import interview.vertx.rest_api.handler.ItemHandler;
import interview.vertx.rest_api.handler.UserHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ApiRouter {
  private final Vertx vertx;
  private final ItemHandler itemHandler;
  private final UserHandler userHandler;

  public ApiRouter(Vertx vertx, ItemHandler itemHandler, UserHandler userHandler) {
    this.vertx = vertx;
    this.itemHandler = itemHandler;
    this.userHandler = userHandler;
  }

  public Router geRouter() {
    Router router = Router.router(vertx);

    //User
    router.post("/register").handler(userHandler::register);
    router.post("/login").handler(userHandler::login);

    //Item
    router.post("/items").handler(itemHandler::createItem);
    router.get("/items").handler(itemHandler::getItemsByUser);

    return router;
  }
}
