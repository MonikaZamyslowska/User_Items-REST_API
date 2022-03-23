package interview.vertx.rest_api.router;

import interview.vertx.rest_api.handler.UserHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class UserRouter {
  private final Vertx vertx;
  private final UserHandler userHandler;

  public UserRouter(Vertx vertx, UserHandler userHandler) {
    this.vertx = vertx;
    this.userHandler = userHandler;
  }

  public Router getUserRouter() {
    Router userRouter = Router.router(vertx);

    userRouter.post("/register").handler(userHandler::register);
    userRouter.post("/login").handler(userHandler::login);

    return userRouter;
  }
}
