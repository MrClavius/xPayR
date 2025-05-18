CREATE TABLE risk_evaluation_log (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(255),
    timestamp TIMESTAMP,
    bin_num NUMERIC(19,2),
    amount NUMERIC(19,2),
    merchant_location VARCHAR(255),
    card_country VARCHAR(255),
    is_local_use BOOLEAN,
    is_reloadable BOOLEAN,
    risk_score DOUBLE PRECISION,
    final_decision VARCHAR(255)
);