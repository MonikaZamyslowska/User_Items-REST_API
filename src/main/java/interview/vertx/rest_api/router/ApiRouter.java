package interview.vertx.rest_api.router;

import interview.vertx.rest_api.handler.ItemHandler;
import interview.vertx.rest_api.handler.UserHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ApiRouter {
  private static final String REGISTER_ENDPOINT = "/register";
  private static final String LOGIN_ENDPOINT = "/login";
  private static final String ITEMS_ENDPOINT = "/items";
  private static final String ERROR_MESSAGE = "Something went wrong. Please, try later.";
  private static final String JSON_FORMAT = "application/json";
  private final Vertx vertx;
  private final ItemHandler itemHandler;
  private final UserHandler userHandler;

  public ApiRouter(Vertx vertx, ItemHandler itemHandler, UserHandler userHandler) {
    this.vertx = vertx;
    this.itemHandler = itemHandler;
    this.userHandler = userHandler;
  }

  public Router getRouter() {
    Router router = Router.router(vertx);

    //User
    router.post(REGISTER_ENDPOINT)
      .handler(BodyHandler.create())
      .consumes(JSON_FORMAT)
      .produces(JSON_FORMAT)
      .handler(userHandler::register)
      .failureHandler(frc -> frc.response().setStatusCode(500).setStatusMessage(ERROR_MESSAGE).end());

    router.post(LOGIN_ENDPOINT)
      .consumes(JSON_FORMAT)
      .produces(JSON_FORMAT)
      .handler(BodyHandler.create())
      .handler(userHandler::login)
      .failureHandler(frc -> frc.response().setStatusCode(500).setStatusMessage(ERROR_MESSAGE).end());

    //Item
    router.post(ITEMS_ENDPOINT)
      .consumes(JSON_FORMAT)
      .produces(JSON_FORMAT)
      .handler(BodyHandler.create())
      .handler(itemHandler::createItem)
      .failureHandler(frc -> frc.response().setStatusCode(500).setStatusMessage(ERROR_MESSAGE).end());

    router.get(ITEMS_ENDPOINT)
      .produces(JSON_FORMAT)
      .handler(itemHandler::getItemsByUser)
      .failureHandler(frc -> frc.response().setStatusCode(500).setStatusMessage(ERROR_MESSAGE).end());

    return router;
  }
}
