package interview.vertx.rest_api.handler;

import interview.vertx.rest_api.model.Item;
import interview.vertx.rest_api.service.ItemService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemHandler {
  private final ItemService itemService;
  private final AuthenticationService authService;
  private final JWTAuth provider;

  public ItemHandler(ItemService itemService, JWTAuth provider, AuthenticationService authService) {
    this.itemService = itemService;
    this.provider = provider;
    this.authService = authService;
  }

  public void createItem(RoutingContext context) {
    final String token = authService.getAuthorizedUserToken();

    if (token == null) {
      context.response().setStatusCode(404).end();
    }

    provider.authenticate(new JsonObject().put("token", token))
      .compose(user -> {
        Item item = Json.decodeValue(context.getBodyAsString(), Item.class);
        context.response().setStatusCode(200).end();
        return itemService.save(item, user.get("sub"));
      })
      .onFailure(context::fail);
  }

  public void getItemsByUser(RoutingContext context) {
    final String token = authService.getAuthorizedUserToken();

    if (token == null) {
      context.response().setStatusCode(404).end();
    }

    provider.authenticate(new JsonObject().put("token", token))
      .compose(user -> itemService.findItemsByUser(user.get("sub")))
      .onSuccess(res -> {
        List<Item> items = res.stream()
          .map(jsonObj -> new Item(UUID.fromString(jsonObj.getString("id")),
            UUID.fromString(jsonObj.getString("owner")),
            jsonObj.getString("name")))
          .collect(Collectors.toList());

        context.response().end(Json.encodePrettily(items));
      })
      .onFailure(context::fail);
  }


}
