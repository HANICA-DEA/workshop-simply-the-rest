package nl.han.oose.dea.rest.resources;

import nl.han.oose.dea.rest.datasources.ConnectorFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Connection;

@Path("/health")
public class HealthResource {
    private Connection h2Connection;

    public HealthResource() {
        h2Connection = ConnectorFactory.createH2Connection();
    }

    @GET
    public Response healthy() {
        return Response.ok("System is up and running").build();
    }
}
