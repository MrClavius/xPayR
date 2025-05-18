package org.riskevaluator.infra.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "risk_evaluation_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiskEvaluationLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    private Instant timestamp;

    @Column(name = "bin_num")
    private BigDecimal binNum;

    private BigDecimal amount;

    @Column(name = "merchant_location")
    private String merchantLocation;

    @Column(name = "card_country")
    private String cardCountry;

    @Column(name = "is_local_use")
    private Boolean isLocalUse;

    @Column(name = "is_reloadable")
    private Boolean isReloadable;

    @Column(name = "risk_score")
    private Double riskScore;

    @Column(name = "final_decision")
    private String finalDecision;

    @ElementCollection
    @CollectionTable(name = "risk_matched_rules", joinColumns = @JoinColumn(name = "log_entry_id"))
    @Column(name = "rule_name")
    private List<String> matchedRules;
}