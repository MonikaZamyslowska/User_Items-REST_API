package interview.vertx.rest_api.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

public class AuthenticationService {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_TOKEN = "Bearer ";
  private final JWTAuth provider;

  public AuthenticationService(JWTAuth provider) {
    this.provider = provider;
  }

  public String getAuthorizedUserToken(RoutingContext context) {
    String header = context.request().getHeader(AUTHORIZATION_HEADER);
    if (header != null || header.startsWith(BEARER_TOKEN)) {
      return header.substring(BEARER_TOKEN.length());
    } else {
      return null;
    }
  }

  public String createAuthorizationToken(RoutingContext context, JsonObject user) {
    String token;
    return provider.generateToken(new JsonObject().put("sub", user.getString("id")), new JWTOptions()
      .setAlgorithm("HS256")
      .setExpiresInMinutes(15));
  }
}
