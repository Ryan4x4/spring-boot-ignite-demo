-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE credit_card (
    cc_number character varying(255) PRIMARY KEY,
    city character varying(255),
    country_code character varying(255),
    line_1 character varying(255),
    line_2 character varying(255),
    phone character varying(255),
    postal_code character varying(255),
    state character varying(255),
    status character varying(255),
    cvv2 integer,
    expire_month integer,
    expire_year integer,
    external_customer_id character varying(255),
    first_name character varying(255),
    funding_state character varying(255),
    last_name character varying(255),
    payer_id character varying(255),
    cc_type character varying(255),
    valid_until character varying(255)
);