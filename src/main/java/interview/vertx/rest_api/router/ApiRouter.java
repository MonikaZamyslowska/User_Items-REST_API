package interview.vertx.rest_api.router;

import interview.vertx.rest_api.handler.ItemHandler;
import interview.vertx.rest_api.handler.UserHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ApiRouter {
  private static final String REGISTER_ENDPOINT = "/register";
  private static final String LOGIN_ENDPOINT = "/login";
  private static final String ITEMS_ENDPOINT = "/items";
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
    router.post(REGISTER_ENDPOINT).handler(userHandler::register);
    router.post(LOGIN_ENDPOINT).handler(userHandler::login);

    //Item
    router.post(ITEMS_ENDPOINT).handler(itemHandler::createItem);
    router.get(ITEMS_ENDPOINT).handler(itemHandler::getItemsByUser);

    return router;
  }
}
