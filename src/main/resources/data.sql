DROP TABLE IF EXISTS questions;
CREATE TABLE questions (
    id INT NOT NULL,
    is_answered BOOLEAN NOT NULL,
    view_count INT NOT NULL,
    answer_count INT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS tags;
CREATE TABLE tags (
    id INT AUTO_INCREMENT,
    tag VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX ux_tag_text ON tags(tag);

DROP TABLE IF EXISTS question_tags;
CREATE TABLE question_tags (
    question_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (question_id, tag_id)
);

ALTER TABLE question_tags ADD FOREIGN KEY (question_id) REFERENCES questions(id);
ALTER TABLE question_tags ADD FOREIGN KEY (tag_id) REFERENCES tags(id);

DROP TABLE IF EXISTS users;
CREATE TABLE users (
    user_id INT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    display_name VARCHAR(1000) NOT NULL,
    PRIMARY KEY (user_id)
);
