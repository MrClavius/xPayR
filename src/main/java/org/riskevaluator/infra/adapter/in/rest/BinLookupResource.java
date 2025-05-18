package org.riskevaluator.infra.adapter.in.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.experimental.FieldDefaults;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import org.riskevaluator.app.port.in.BinLookupUseCase;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/bin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BinLookupResource {

    private static final Logger logger = LoggerFactory.getLogger(BinLookupResource.class);

    @Inject
    BinLookupUseCase lookupUseCase;

    @POST
    @Path("/account-range")
    @RolesAllowed("bin-reader")
    public BinRiskProfile searchByAccountRange(SearchByAccountRange request) {
        return lookupUseCase.getBin(request);
    }
}
