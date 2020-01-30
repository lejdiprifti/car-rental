CREATE TABLE rental."user"
(
    user_id bigint NOT NULL,
    active boolean,
    address character varying(255) COLLATE pg_catalog."default",
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    username character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    contact_number character varying(255) COLLATE pg_catalog."default",
    role_id integer,
    birthdate date,
    CONSTRAINT user_pkey PRIMARY KEY (user_id),
    CONSTRAINT fkn82ha3ccdebhokx3a8fgdqeyy FOREIGN KEY (role_id)
        REFERENCES rental.role (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);