package interview.vertx.rest_api;

import interview.vertx.rest_api.handler.AuthenticationService;
import interview.vertx.rest_api.handler.ItemHandler;
import interview.vertx.rest_api.handler.UserHandler;
import interview.vertx.rest_api.repository.ItemRepository;
import interview.vertx.rest_api.repository.UserRepository;
import interview.vertx.rest_api.router.ApiRouter;
import interview.vertx.rest_api.service.ItemService;
import interview.vertx.rest_api.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;


public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    final MongoClient mongoClient = createMongoClient(vertx);
    final JWTAuth jwtAuth = createJwtAuth();
    final AuthenticationService authService = new AuthenticationService(jwtAuth);

    //User
    UserRepository userRepository = new UserService(mongoClient);
    UserHandler userHandler = new UserHandler(userRepository, authService);

    //Item
    ItemRepository itemRepository = new ItemService(mongoClient);
    ItemHandler itemHandler = new ItemHandler(itemRepository, authService);

    ApiRouter apiRouter = new ApiRouter(vertx, itemHandler, userHandler);
    Router router = apiRouter.geRouter();

    server
      .requestHandler(router)
      .listen(8080)
      .onSuccess(http -> {
        System.out.println("HTTP server started on port:" + http.actualPort());
        startPromise.complete();
      })
      .onFailure(http -> startPromise.fail(http.getCause()));
  }

  private JWTAuth createJwtAuth() {
    return JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm("HS256")
        .setBuffer("keyboard cat")));
  }

  private MongoClient createMongoClient(Vertx vertx) {
    final JsonObject config = new JsonObject()
      .put("db_name", "demo_db");

    return MongoClient.createShared(vertx, config);
  }
}
