package nl.han.oose.dea.rest.resources.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.dea.rest.services.exceptions.IdAlreadyInUseException;

@Provider
public class IdAlreadyInUseExceptionMapper implements ExceptionMapper<IdAlreadyInUseException> {
    public Response toResponse(IdAlreadyInUseException ex) {
        return Response.status(409).
                entity(ex.getMessage()).
                type("text/plain").
                build();
    }
}
