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
  private static final String AUTH_ALGORITHM = "HS256";
  private static final String AUTH_BUFFER = "keyboard cat";
  private static final String DB_NAME = "demo_db";
  private static final String SERVER_STARTED_INFO = "HTTP server started on port: ";
  private static final int PORT_NUMBER = 8080;

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
    Router router = apiRouter.getRouter();

    server
      .requestHandler(router)
      .listen(PORT_NUMBER)
      .onSuccess(http -> {
        System.out.println(SERVER_STARTED_INFO + http.actualPort());
        startPromise.complete();
      })
      .onFailure(http -> startPromise.fail(http.getCause()));
  }

  private JWTAuth createJwtAuth() {
    return JWTAuth.create(vertx, new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions()
        .setAlgorithm(AUTH_ALGORITHM)
        .setBuffer(AUTH_BUFFER)));
  }

  private MongoClient createMongoClient(Vertx vertx) {
    final JsonObject config = new JsonObject()
      .put("db_name", DB_NAME);

    return MongoClient.createShared(vertx, config);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
