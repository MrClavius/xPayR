package org.riskevaluator.domain.policy.rules;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;

import static org.assertj.core.api.Assertions.assertThat;

class CountryMismatchRuleTest {

    CountryMismatchRule rule = new CountryMismatchRule();

    @ParameterizedTest
    @CsvSource({
            "PL, DE, 30, 'Transaction country differs from card issuer.'",
            "de, DE, 0, ''",
            "PL, PL, 0, ''",
            "PL, null, 0, ''",
            "null, DE, 0, ''",
            "null, null, 0, ''"
    })
    void returnsExpectedScoreAndReason(String txLocation, String binCountry, int expectedScore, String expectedReason) {
        var tx = Transaction.builder()
                .location("null".equals(txLocation) ? null : txLocation)
                .build();
        var bin = BinRiskProfile.builder()
                .country("null".equals(binCountry) ? null : binCountry)
                .build();

        var match = rule.evaluate(tx, bin);

        assertThat(match.score()).isEqualTo(expectedScore);
        assertThat(match.rule()).isEqualTo("CountryMismatch");
        assertThat(match.reason()).isEqualTo(expectedReason);
    }
}