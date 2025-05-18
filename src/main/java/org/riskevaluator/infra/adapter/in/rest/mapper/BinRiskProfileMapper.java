package org.riskevaluator.infra.adapter.in.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.quarkus.BIN_Table_Resource_OpenAPI3_yaml.model.BinResource;
import org.riskevaluator.domain.model.BinRiskProfile;

@Mapper(componentModel = "cdi")
public interface BinRiskProfileMapper {

    @Mapping(source = "country.alpha3", target = "country")
    BinRiskProfile dtoToDomain(BinResource bin);
}