package interview.vertx.rest_api.handler;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

public class AuthenticationService {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_TOKEN = "Bearer ";
  private static final String AUTH_ALGORITHM = "HS256";
  private final JWTAuth provider;

  public AuthenticationService(JWTAuth provider) {
    this.provider = provider;
  }

  public String getAuthorizedUserToken(RoutingContext context) {
    String header = context.request().getHeader(AUTHORIZATION_HEADER);
    if (header != null) {
      return header.substring(BEARER_TOKEN.length());
    } else {
      return null;
    }
  }

  public String createAuthorizationToken(JsonObject user) {
    return provider.generateToken(new JsonObject().put("sub", user.getString("_id")), new JWTOptions()
      .setAlgorithm(AUTH_ALGORITHM)
      .setExpiresInMinutes(15));
  }

  public Future<User> getAuthenticateUser(RoutingContext context) {
    String token = getAuthorizedUserToken(context);
    return provider.authenticate(new JsonObject().put("token", token));
  }
}
