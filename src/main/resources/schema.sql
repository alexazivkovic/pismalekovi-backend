CREATE TABLE IF NOT EXISTS drug (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    external_id VARCHAR(128),
    name VARCHAR(512) NOT NULL,
    atc VARCHAR(64),
    manufacturer VARCHAR(256),
    UNIQUE KEY uk_drug_name (name)
    );

CREATE TABLE IF NOT EXISTS letter (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(512) NOT NULL,
    pdf_path VARCHAR(1024) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL
    );

CREATE TABLE IF NOT EXISTS letter_drug (
    letter_id BIGINT NOT NULL,
    drug_id BIGINT NOT NULL,
    PRIMARY KEY (letter_id, drug_id),
    CONSTRAINT fk_ld_letter FOREIGN KEY (letter_id) REFERENCES letter(id),
    CONSTRAINT fk_ld_drug FOREIGN KEY (drug_id) REFERENCES drug(id)
    );

CREATE TABLE IF NOT EXISTS subscriber_email (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(320) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
