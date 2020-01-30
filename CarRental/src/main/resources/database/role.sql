CREATE TABLE rental.role
(
    id bigint NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT role_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);