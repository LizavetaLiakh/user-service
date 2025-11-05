--liquibase formatted sql

--changeset LizavetaLiakh:usr1_tables
CREATE TABLE users(
    id BIGINT PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    email VARCHAR(254) NOT NULL
);

CREATE TABLE card_info(
    id BIGINT PRIMARY KEY NOT NULL,
    user_id BIGINT NOT NULL,
    number VARCHAR(16) NOT NULL,
    holder VARCHAR(101) NOT NULL,
    expiration_date DATE NOT NULL,
    CONSTRAINT fk_card_info_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);