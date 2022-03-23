package interview.vertx.rest_api.handler;

import interview.vertx.rest_api.model.User;
import interview.vertx.rest_api.service.UserService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserHandler {
  private final UserService userService;
  private final AuthenticationService authService;

  public UserHandler(UserService userService, AuthenticationService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  public void register(RoutingContext context) {
    User user = Json.decodeValue(context.getBodyAsString(), User.class);

    userService.findUserByLogin(user.getLogin())
      .compose(res -> {
        if (res == null) {
          context.response()
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(Json.encodePrettily(user));
          return userService.save(user);
        } else {
          context.response().setStatusCode(404).end();
          return null;
        }
      });
  }

  public void login(RoutingContext context) {
    User user = Json.decodeValue(context.getBodyAsString(), User.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    userService.findUserByLogin(user.getLogin())
      .onSuccess(res -> {
        if (res == null) {
          context.response().setStatusCode(404).end();
          return;
        }

        if (!passwordEncoder.matches(user.getPassword(), res.getString("password"))) {
          context.response().setStatusCode(404).end();
          return;
        }

        String authToken = authService.createAuthorizationToken(res);

        context.response().setStatusCode(200).end(Json.encode(new JsonObject().put("token", authToken)));
      })
      .onFailure(context::fail);
  }
}
