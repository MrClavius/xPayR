package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AmountTransferRuleTest {

    AmountTransferRule rule = new AmountTransferRule();

    @ParameterizedTest
    @CsvSource({

            "5000, 0, ''",
            "90000, 5, 'Transaction amount above 10000.'",
            "120000, 10, 'Transaction amount above 100000.'",
            "260000, 15, 'Transaction amount above 250000.'",
            "600000, 20, 'Transaction amount above 500000.'"
    })
    void returnsCorrectMatchForGivenAmount(String amount, int expectedScore, String expectedReason) {
        var tx = Transaction.builder()
                .amount(new BigDecimal(amount))
                .build();
        var bin = new BinRiskProfile();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("AmountTransferRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }
}