CREATE TABLE rental.category
(
    category_id bigint NOT NULL,
    active boolean,
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    photo oid,
    CONSTRAINT category_pkey PRIMARY KEY (category_id)
)
WITH (
    OIDS = FALSE
);