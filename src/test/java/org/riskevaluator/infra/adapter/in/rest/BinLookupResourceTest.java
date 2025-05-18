package org.riskevaluator.infra.adapter.in.rest;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResource;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import io.quarkus.test.security.TestSecurity;
import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class BinLookupResourceTest {

    @InjectMock
    @RestClient
    BinLookupApi binLookupApi;

    @Nested
    class GetBinEndpointTests {

        @Test
        @TestSecurity(user = "test-user", roles = {"bin-reader"})
        void shouldReturnBinNumWhenPostBinAccountRangeIsValid() {
            var binResource = new BinResource().binNum("123456");
            Mockito.when(binLookupApi.searchByAccountRangeResources(Mockito.any())).thenReturn(List.of(binResource));

            var input = new SearchByAccountRange().accountRange(new BigDecimal("123456"));

            given()
                    .contentType(ContentType.JSON)
                    .body(input)
                    .when()
                    .post("/bin/account-range")
                    .then()
                    .statusCode(200)
                    .body("binNum", equalTo("123456"));
        }
    }
}