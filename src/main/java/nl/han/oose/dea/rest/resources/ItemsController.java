package nl.han.oose.dea.rest.resources;

import nl.han.oose.dea.rest.resources.dto.Item;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/items")
public class ItemsController {
    private List<Item> items = new ArrayList<Item>();

    public ItemsController()
    {
        items.add(new Item("Bread", "Breakfast", "Delicious!"));
        items.add(new Item("Butter", "Breakfast", "Use it with bread"));
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTextItems()
    {
        return "bread, butter";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getJsonItems()  {
        return items;
    }

    @GET
    @Path("/{sku}")
    @Produces(MediaType.APPLICATION_JSON)
    public Item getJsonItem(@PathParam("sku") final String sku) {
        return items.stream().filter((item) -> item.getSku().equals(sku)).findAny().get();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addItem(Item item)
    {
        items.add(item);
    }

    @DELETE
    @Path("/{sku}")
    public void deleteItem(@PathParam("sku") final String sku)
    {
        items.removeIf(item -> item.getSku().equals(sku));
    }
}
