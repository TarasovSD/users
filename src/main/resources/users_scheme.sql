CREATE SEQUENCE cities_id_seq
    start with 1
    increment by 1
    cache 1;

CREATE TABLE IF NOT EXISTS cities
(
    id   integer           NOT NULL DEFAULT nextval('cities_id_seq'),
    name character varying NOT NULL,
    CONSTRAINT cities_pk PRIMARY KEY (id)
);

CREATE SEQUENCE hard_skills_id_seq
    start with 1
    increment by 1
    cache 1;

CREATE TABLE IF NOT EXISTS hard_skills
(
    id   integer           NOT NULL DEFAULT nextval('hard_skills_id_seq'),
    name character varying NOT NULL,
    CONSTRAINT hard_skills_pk PRIMARY KEY (id)
);

CREATE SEQUENCE users_id_seq
    start with 1
    increment by 1
    cache 1;

CREATE TABLE IF NOT EXISTS users
(
    id          integer           NOT NULL DEFAULT nextval('users_id_seq'),
    first_name  character varying NOT NULL,
    last_name   character varying NOT NULL,
    patronymic  character varying,
    gender      character varying NOT NULL,
    birthday    character varying NOT NULL,
    city_id     integer           NOT NULL,
    link        character varying NOT NULL,
    description character varying,
    nickname    character varying,
    email       character varying NOT NULL,
    phone       character varying NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_on_cities FOREIGN KEY (city_id) REFERENCES cities (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users_hard_skills
(
    user_id       integer NOT NULL,
    hard_skill_id integer NOT NULL,
    CONSTRAINT users_hard_skills_pk PRIMARY KEY (user_id, hard_skill_id),
    CONSTRAINT user_id_on_users_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT hard_skill_id_on_hard_skills_fk FOREIGN KEY (hard_skill_id) REFERENCES hard_skills (id) ON DELETE CASCADE
);

CREATE SEQUENCE subscriptions_id_seq
    start with 1
    increment by 1
    cache 1;

CREATE TABLE IF NOT EXISTS subscriptions
(
    id            integer NOT NULL DEFAULT nextval('subscriptions_id_seq'),
    subscriber_id integer NOT NULL,
    respondent_id integer NOT NULL,
    CONSTRAINT subscriptions_pk PRIMARY KEY (id),
    CONSTRAINT subscriber_on_user_fk FOREIGN KEY (subscriber_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT respondent_on_user_fk FOREIGN KEY (respondent_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX i_user_gender
    ON users (gender);

CREATE INDEX i_user_city
    ON users (city_id);

CREATE INDEX i_user_gender_and_city
    ON users (gender, city_id);

CREATE INDEX i_users_hard_skills_user_id
    ON users_hard_skills (user_id);

CREATE INDEX i_users_hard_skills_hard_skill_id
    ON users_hard_skills (hard_skill_id);

CREATE INDEX i_subscriptions_subscriber_id
    ON subscriptions (subscriber_id);

CREATE INDEX i_subscriptions_respondent_id
    ON subscriptions (respondent_id);