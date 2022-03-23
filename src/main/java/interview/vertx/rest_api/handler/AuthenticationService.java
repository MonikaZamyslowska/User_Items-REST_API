package interview.vertx.rest_api.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

public class AuthenticationService {
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_TOKEN = "Bearer ";
  private final JWTAuth provider;
  private final RoutingContext context;

  public AuthenticationService(JWTAuth provider, RoutingContext context) {
    this.provider = provider;
    this.context = context;
  }

  public String getAuthorizedUserToken() {
    String header = context.request().getHeader(AUTHORIZATION_HEADER);
    if (header != null || header.startsWith(BEARER_TOKEN)) {
      return header.substring(BEARER_TOKEN.length());
    } else {
      return null;
    }
  }

  public String createAuthorizationToken(JsonObject user) {
    return provider.generateToken(new JsonObject().put("sub", user.getString("id")), new JWTOptions()
      .setAlgorithm("HS256")
      .setExpiresInMinutes(15));
  }
}
