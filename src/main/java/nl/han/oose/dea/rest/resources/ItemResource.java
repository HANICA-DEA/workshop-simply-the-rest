package nl.han.oose.dea.rest.resources;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.rest.services.ItemService;
import nl.han.oose.dea.rest.services.dto.ItemDTO;

import java.net.URI;

import static jakarta.ws.rs.core.Response.created;
import static jakarta.ws.rs.core.Response.ok;

@Path("/items")
@ApplicationScoped
public class ItemResource {
    private ItemService itemService = new ItemService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {
        return ok(itemService.getAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getItem(@PathParam("id") int id) {
        return ok(itemService.getItem(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(ItemDTO item) {
        itemService.addItem(item);
        return created(URI.create("/items/"+item.getId())).entity(item).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") int id) {
        itemService.deleteItem(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
