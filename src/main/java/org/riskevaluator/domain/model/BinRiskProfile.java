package org.riskevaluator.domain.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinRiskProfile {

    private String binNum;
    private String country;
    private Boolean localUse;
    private String productCode;
    private Boolean nonReloadableIndicator;
    private String anonymousPrepaidIndicator;
    private String fundingSource;
    private String consumerType;
    private String credentialStatus;
    private Boolean gamblingBlockEnabled;
}