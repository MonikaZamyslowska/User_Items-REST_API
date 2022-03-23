package interview.vertx.rest_api.router;

import interview.vertx.rest_api.handler.ItemHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ItemRouter {
  private final Vertx vertx;
  private final ItemHandler itemHandler;

  public ItemRouter(Vertx vertx, ItemHandler itemHandler) {
    this.vertx = vertx;
    this.itemHandler = itemHandler;
  }

  public Router getItemRouter() {
    final Router itemRouter = Router.router(vertx);

    itemRouter.post("/items").handler(itemHandler::createItem);
    itemRouter.get("/items").handler(itemHandler::getItemsByUser);

    return itemRouter;
  }
}
