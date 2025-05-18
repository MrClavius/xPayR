package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class ConsumerTypeRoleRuleTest {

    ConsumerTypeRoleRule rule = new ConsumerTypeRoleRule();

    @ParameterizedTest
    @CsvSource({
            "CONSUMER, 20, 'Consumer card: higher fraud risk.'",
            "consumer, 20, 'Consumer card: higher fraud risk.'",
            "BUSINESS, 0, ''",
            "'', 0, ''",
            "null, 0, ''"
    })
    void returnsExpectedScoreAndReason(String consumerType, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .consumerType("null".equals(consumerType) ? null : consumerType)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("ConsumerTypeRoleRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }

}