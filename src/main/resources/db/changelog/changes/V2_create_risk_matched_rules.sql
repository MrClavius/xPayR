CREATE TABLE risk_matched_rules (
    log_entry_id BIGINT NOT NULL,
    rule_name VARCHAR(255),
    CONSTRAINT fk_risk_matched_rules_log_entry FOREIGN KEY (log_entry_id) REFERENCES risk_evaluation_log(id)
);