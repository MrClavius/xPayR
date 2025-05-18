package org.riskevaluator.infra.adapter.in.rest;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResource;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResourceCountry;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import org.riskevaluator.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class TransactionRiskEvaluatorResourceTest {

    @InjectMock
    @RestClient
    BinLookupApi binLookupApi;

    @Test
    @TestSecurity(user = "risk-user", roles = "risk-analyzer")
    void shouldReturnRiskResultForValidTransaction() {
        BinResource binResource = binResource();

        Mockito.when(binLookupApi.searchByAccountRangeResources(Mockito.any(SearchByAccountRange.class)))
                .thenReturn(List.of(binResource));

        Transaction tx = Transaction.builder()
                .binNum(new BigDecimal("123456789"))
                .amount(new BigDecimal("10000"))
                .location("PL")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(tx)
        .when()
                .post("/transactions/risk")
        .then()
                .statusCode(200)
                .body("totalScore", is(23.0F))
                .body("matches", hasSize(9))
                .body("matches.rule", hasItems(
                        "AmountTransferRule",
                        "CountryMismatch",
                        "NonReloadable",
                        "AnonymousPrepaidIndicatorRule",
                        "ConsumerTypeRoleRule",
                        "CredentialStatusRule",
                        "FundingSourceRule",
                        "GamblingBlockRule",
                        "LocalUseRule"
                ));
    }

    private static BinResource binResource(){
        BinResource bin = new BinResource();
        bin.setBinNum("123456");
        bin.setCountry(new BinResourceCountry());
        bin.setLocalUse(Boolean.TRUE);
        bin.setProductCode("123");
        bin.setNonReloadableIndicator(Boolean.TRUE);
        bin.setAnonymousPrepaidIndicator("A");
        bin.setFundingSource("A");
        bin.setConsumerType("CORPORATE");
        bin.setCredentialStatus("A");
        bin.setGamblingBlockEnabled(Boolean.FALSE);
        return bin;
    }

}