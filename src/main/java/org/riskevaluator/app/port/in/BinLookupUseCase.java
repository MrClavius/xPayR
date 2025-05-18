package org.riskevaluator.app.port.in;

import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.SearchByAccountRange;
import org.riskevaluator.domain.model.BinRiskProfile;

public interface BinLookupUseCase {
    BinRiskProfile getBin(SearchByAccountRange searchByAccountRange);
}