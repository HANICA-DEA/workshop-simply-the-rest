package nl.han.oose.dea.rest.resources;

import nl.han.oose.dea.rest.services.ItemService;
import nl.han.oose.dea.rest.services.dto.ItemDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/items")
public class ItemResource {

    private ItemService itemService;

    public ItemResource() {
        this.itemService = new ItemService();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTextItems() {
        return "bread, butter";
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getJsonItems() {
//        return "[\"bread\", \"butter\"]";
//    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<ItemDTO> getJsonItems() {
//        return itemService.getAll();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJsonItems() {
        return Response.ok().entity(itemService.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("id") int id) {
        return Response.ok().entity(itemService.getItem(id)).build();
    }
}
