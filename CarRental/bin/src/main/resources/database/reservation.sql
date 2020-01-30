CREATE TABLE rental.reservation
(
    id bigint NOT NULL,
    active boolean NOT NULL,
    end_date date,
    start_date date,
    car_id bigint,
    user_id bigint,
    CONSTRAINT reservation_pkey PRIMARY KEY (id),
    CONSTRAINT fkgkmbspv7rljixxoxo1af80lpp FOREIGN KEY (car_id)
        REFERENCES rental.car (car_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fkm4oimk0l1757o9pwavorj6ljg FOREIGN KEY (user_id)
        REFERENCES rental."user" (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);