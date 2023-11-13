CREATE TYPE comment_topic_status AS ENUM (
    'CREATED',
    'APPROVED'
);

CREATE TABLE IF NOT EXISTS comment (
    id SERIAL,
    article_id INTEGER NOT NULL,
    created_date DATE NOT NULL,
    content VARCHAR(255) NOT NULL,
    status comment_topic_status NOT NULL,
    username VARCHAR(50),
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
);