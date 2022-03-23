package interview.vertx.rest_api.handler;

import interview.vertx.rest_api.model.Item;
import interview.vertx.rest_api.repository.ItemRepository;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemHandler {
  private final ItemRepository itemRepository;
  private final AuthenticationService authService;

  public ItemHandler(ItemRepository itemRepository, AuthenticationService authService) {
    this.itemRepository = itemRepository;
    this.authService = authService;
  }

  public void createItem(RoutingContext context) {
    final String token = authService.getAuthorizedUserToken(context);

    if (token == null) {
      context.response().setStatusCode(404).end();
    }

    authService.getAuthenticateUser(context)
      .compose(user -> {
        Item item = Json.decodeValue(context.getBodyAsString(), Item.class);
        return itemRepository.save(item, user.get("sub"));
      })
      .onSuccess(res -> context.response().setStatusCode(201).end())
      .onFailure(context::fail);
  }

  public void getItemsByUser(RoutingContext context) {
    authService.getAuthenticateUser(context)
      .compose(user -> itemRepository.findItemsByUser(user.get("sub")))
      .onSuccess(res -> {
        List<Item> items = res.stream()
          .map(jsonObj -> new Item(
            UUID.fromString(jsonObj.getString("id")),
            UUID.fromString(jsonObj.getString("owner")),
            jsonObj.getString("name")))
          .collect(Collectors.toList());

        context.response().end(Json.encodePrettily(items));
      })
      .onFailure(context::fail);
  }
}
