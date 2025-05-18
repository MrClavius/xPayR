package org.riskevaluator.domain.policy;

import org.junit.jupiter.api.Test;
import org.riskevaluator.domain.model.BinRiskProfile;
import org.riskevaluator.domain.model.Transaction;
import org.riskevaluator.domain.policy.rules.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RiskScorerTest {

    private static final int EXPECTED_SCORE = 175;
    private static final int MAX_SCORE = 100;

    @Test
    void shouldCalculateRiskWhenAllRulesAreTriggered() {
        // Given
        var tx = Transaction.builder()
                .binNum(new BigDecimal("12345678"))
                .amount(new BigDecimal("600000"))
                .location("PL")
                .build();

        var bin = BinRiskProfile.builder()
                .country("US")
                .anonymousPrepaidIndicator("A")
                .fundingSource("PREPAID")
                .consumerType("CONSUMER")
                .credentialStatus("B")
                .gamblingBlockEnabled(true)
                .localUse(false)
                .nonReloadableIndicator(true)
                .build();

        var rules = List.of(
                new AmountTransferRule(),
                new CountryMismatchRule(),
                new AnonymousPrepaidIndicatorRule(),
                new FundingSourceRule(),
                new ConsumerTypeRoleRule(),
                new CredentialStatusRule(),
                new GamblingBlockRule(),
                new LocalUseRule(),
                new NonReloadableRule()
        );

        var scorer = new RiskScorer(rules);


        // When
        var result = scorer.evaluate(tx, bin);
        var actualSumScore = result.matches().stream().mapToDouble(RuleMatch::score).sum();

        // Then
        assertThat(result).isNotNull();
        assertThat(actualSumScore).isEqualTo(EXPECTED_SCORE);
        assertThat(result.totalScore())
                .isEqualTo(MAX_SCORE);

        assertThat(result.matches())
                .hasSize(rules.size())
                .allSatisfy(match -> assertThat(match.score()).isGreaterThan(0));
        assertThat(result.matches()).anySatisfy(m -> {
            if (m.rule().equals("AmountTransferRule")) {
                assertThat(m.score()).isEqualTo(20);
                assertThat(m.reason()).contains("above 500000");
            }
        });
        assertThat(result.matches()).anySatisfy(m -> {
            if (m.rule().equals("CountryMismatch")) {
                assertThat(m.score()).isEqualTo(30);
                assertThat(m.reason()).containsIgnoringCase("country differs");
            }
        });
        assertThat(result.message().toLowerCase())
                .isNotNull()
                .contains("very high")
                .contains("risk");
    }
}