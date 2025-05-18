package org.riskevaluator.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Transaction {

    private BigDecimal binNum;
    private BigDecimal amount;
    private String location;
}