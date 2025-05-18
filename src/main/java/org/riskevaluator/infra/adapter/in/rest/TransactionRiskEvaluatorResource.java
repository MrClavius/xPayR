package org.riskevaluator.infra.adapter.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.experimental.FieldDefaults;
import org.riskevaluator.app.port.in.TransactionRiskUseCase;
import org.riskevaluator.domain.model.Transaction;
import jakarta.annotation.security.RolesAllowed;
import org.riskevaluator.domain.policy.RiskResult;

@Path("/transactions")
@RolesAllowed("risk-analyzer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TransactionRiskEvaluatorResource {

    @Inject
    TransactionRiskUseCase useCase;

    @POST
    @Path("/risk")
    public RiskResult getRisk(Transaction transaction) {
        return useCase.calculateRisk(transaction);
    }
}