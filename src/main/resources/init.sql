-- Remove conflicting tables
DROP TABLE IF EXISTS event CASCADE;
DROP TABLE IF EXISTS ticket CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_event CASCADE;
-- End of removing

CREATE TABLE event (
                       id_event SERIAL NOT NULL,
                       organizer_nickname INTEGER NOT NULL,
                       title VARCHAR(256) NOT NULL,
                       date_and_time VARCHAR(256) NOT NULL,
                       ticket_price VARCHAR(256) NOT NULL, -- todo int
                       location VARCHAR(256) NOT NULL,
                       capacity VARCHAR(256), -- todo int
                       description VARCHAR(256),
                       topic VARCHAR(256),
                       age_restriction VARCHAR(256)  -- todo bool
);
ALTER TABLE event ADD CONSTRAINT pk_event PRIMARY KEY (id_event, organizer_nickname);

CREATE TABLE ticket (
                        id_ticket SERIAL NOT NULL,
                        event_id INTEGER NOT NULL,
                        organizer_nickname INTEGER NOT NULL,
                        customer_nickname INTEGER NOT NULL,
                        seat VARCHAR(256), -- todo int
                        details VARCHAR(256)
);
ALTER TABLE ticket ADD CONSTRAINT pk_ticket PRIMARY KEY (id_ticket, event_id, organizer_nickname);

CREATE TABLE users (
                      nickname SERIAL NOT NULL,
                      email VARCHAR(256) NOT NULL,
                      age VARCHAR(256) NOT NULL, -- todo int
                      first_name VARCHAR(256),
                      last_name VARCHAR(256),
                      description VARCHAR(256)
);
ALTER TABLE users ADD CONSTRAINT pk_user PRIMARY KEY (nickname);

CREATE TABLE user_event (
                            user_nickname INTEGER NOT NULL,
                            id INTEGER NOT NULL,
                            event_nickname INTEGER NOT NULL
);
ALTER TABLE user_event ADD CONSTRAINT pk_user_event PRIMARY KEY (user_nickname, id, event_nickname);

ALTER TABLE event ADD CONSTRAINT fk_event_user FOREIGN KEY (organizer_nickname) REFERENCES users (nickname) ON DELETE CASCADE;

ALTER TABLE ticket ADD CONSTRAINT fk_ticket_event FOREIGN KEY (event_id, organizer_nickname) REFERENCES event (id_event, organizer_nickname) ON DELETE CASCADE;
ALTER TABLE ticket ADD CONSTRAINT fk_ticket_user FOREIGN KEY (customer_nickname) REFERENCES users (nickname) ON DELETE CASCADE;

ALTER TABLE user_event ADD CONSTRAINT fk_user_event_user FOREIGN KEY (user_nickname) REFERENCES users (nickname) ON DELETE CASCADE;
ALTER TABLE user_event ADD CONSTRAINT fk_user_event_event FOREIGN KEY (id, event_nickname) REFERENCES event (id_event, organizer_nickname) ON DELETE CASCADE;
