CREATE TABLE rental.car
(
    car_id bigint NOT NULL,
    active boolean NOT NULL,
    availability boolean NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    diesel character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    photo oid,
    price double precision,
    type character varying(255) COLLATE pg_catalog."default",
    category bigint,
    plate character varying(255) COLLATE pg_catalog."default",
    year integer,
    CONSTRAINT car_pkey PRIMARY KEY (car_id),
    CONSTRAINT fk11hxueuqrs2dyc5glnyaftson FOREIGN KEY (category)
        REFERENCES rental.category (category_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);