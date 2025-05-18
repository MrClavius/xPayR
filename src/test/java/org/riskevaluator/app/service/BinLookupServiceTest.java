package org.riskevaluator.app.service;

import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.api.BinLookupApi;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResource;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.infra.adapter.in.rest.mapper.BinRiskProfileMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BinLookupServiceTest {

    @InjectMocks
    BinLookupService binLookupService;

    @Mock
    BinLookupApi binLookupApi;

    @Mock
    BinRiskProfileMapper binRiskProfileMapper;

    @Nested
    @DisplayName("When BIN found")
    class WhenBinFound {

        private SearchByAccountRange search;
        private BinResource binResource;
        private BinRiskProfile expectedProfile;

        @BeforeEach
        void init() {
            search = new SearchByAccountRange().accountRange(new BigDecimal("123456"));
            binResource = new BinResource().binNum("123456");
            expectedProfile = BinRiskProfile.builder().binNum("123456").build();

            when(binLookupApi.searchByAccountRangeResources(search)).thenReturn(List.of(binResource));
            when(binRiskProfileMapper.dtoToDomain(binResource)).thenReturn(expectedProfile);
        }

        @Test
        void shouldReturnBinRiskProfile() {
            BinRiskProfile result = binLookupService.getBin(search);

            assertThat(result).isNotNull();
            assertThat(result.getBinNum()).isEqualTo("123456");

            verify(binLookupApi).searchByAccountRangeResources(search);
            verify(binRiskProfileMapper).dtoToDomain(binResource);
        }
    }

    @Nested
    @DisplayName("When BIN not found")
    class WhenBinNotFound {

        private SearchByAccountRange search;

        @BeforeEach
        void init() {
            search = new SearchByAccountRange().accountRange(new BigDecimal("999999"));
            when(binLookupApi.searchByAccountRangeResources(search)).thenReturn(List.of());
        }

        @Test
        void shouldThrowNotFoundException() {
            assertThatThrownBy(() -> binLookupService.getBin(search))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("No BIN found for account range");

            verify(binLookupApi).searchByAccountRangeResources(search);
            verifyNoInteractions(binRiskProfileMapper);
        }
    }
}