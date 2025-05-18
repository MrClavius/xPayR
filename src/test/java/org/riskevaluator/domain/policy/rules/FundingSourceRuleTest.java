package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class FundingSourceRuleTest {
    FundingSourceRule rule = new FundingSourceRule();

    @ParameterizedTest
    @CsvSource({
            "CREDIT, 0, ''",
            "DEBIT, 10, 'Debit card: moderate fraud risk.'",
            "PREPAID, 20, 'Prepaid card: higher fraud risk.'",
            "credit, 0, ''",
            "debit, 10, 'Debit card: moderate fraud risk.'",
            "'', 0, ''",
            "null, 0, ''"
    })
    void returnsExpectedScoreAndReason(String fundingSource, int expectedScore, String expectedReason) {
        var tx = Transaction.builder().build();
        var bin = BinRiskProfile.builder()
                .fundingSource("null".equals(fundingSource) ? null : fundingSource)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("FundingSourceRule");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }
}