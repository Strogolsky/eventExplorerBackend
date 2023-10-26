-- Remove conflicting tables
DROP TABLE IF EXISTS event CASCADE;
DROP TABLE IF EXISTS ticket CASCADE;
DROP TABLE IF EXISTS user CASCADE;
DROP TABLE IF EXISTS user_event CASCADE;
-- End of removing

CREATE TABLE event (
                       id SERIAL NOT NULL,
                       nickname INTEGER NOT NULL,
                       title VARCHAR(256) NOT NULL,
                       date_and_time VARCHAR(256) NOT NULL,
                       ticket_price VARCHAR(256) NOT NULL,
                       location VARCHAR(256) NOT NULL,
                       capacity VARCHAR(256),
                       description VARCHAR(256),
                       topic VARCHAR(256),
                       age_restriction VARCHAR(256)
);
ALTER TABLE event ADD CONSTRAINT pk_event PRIMARY KEY (id, nickname);

CREATE TABLE ticket (
                        id SERIAL NOT NULL,
                        event_id INTEGER NOT NULL,
                        nickname INTEGER NOT NULL,
                        user_nickname INTEGER NOT NULL,
                        seat VARCHAR(256),
                        details VARCHAR(256)
);
ALTER TABLE ticket ADD CONSTRAINT pk_ticket PRIMARY KEY (id, event_id, nickname);

CREATE TABLE user (
                      nickname SERIAL NOT NULL,
                      email VARCHAR(256) NOT NULL,
                      age VARCHAR(256) NOT NULL,
                      first_name VARCHAR(256),
                      last_name VARCHAR(256),
                      description VARCHAR(256)
);
ALTER TABLE user ADD CONSTRAINT pk_user PRIMARY KEY (nickname);

CREATE TABLE user_event (
                            user_nickname INTEGER NOT NULL,
                            id INTEGER NOT NULL,
                            event_nickname INTEGER NOT NULL
);
ALTER TABLE user_event ADD CONSTRAINT pk_user_event PRIMARY KEY (user_nickname, id, event_nickname);

ALTER TABLE event ADD CONSTRAINT fk_event_user FOREIGN KEY (nickname) REFERENCES user (nickname) ON DELETE CASCADE;

ALTER TABLE ticket ADD CONSTRAINT fk_ticket_event FOREIGN KEY (event_id, nickname) REFERENCES event (id, nickname) ON DELETE CASCADE;
ALTER TABLE ticket ADD CONSTRAINT fk_ticket_user FOREIGN KEY (user_nickname) REFERENCES user (nickname) ON DELETE CASCADE;

ALTER TABLE user_event ADD CONSTRAINT fk_user_event_user FOREIGN KEY (user_nickname) REFERENCES user (nickname) ON DELETE CASCADE;
ALTER TABLE user_event ADD CONSTRAINT fk_user_event_event FOREIGN KEY (id, event_nickname) REFERENCES event (id, nickname) ON DELETE CASCADE;
