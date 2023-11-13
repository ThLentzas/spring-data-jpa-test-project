CREATE TABLE IF NOT EXISTS topic (
    id SERIAL,
    name VARCHAR(40) UNIQUE NOT NULL,
    created_date DATE NOT NULL,
    status comment_topic_status NOT NULL,
    CONSTRAINT pk_topic PRIMARY KEY (id),
    CONSTRAINT unique_topic_name UNIQUE (name)
);

CREATE TABLE if not exists article_topic (
    article_id INTEGER NOT NULL,
    topic_id INTEGER NOT NULL,
    CONSTRAINT pk_article_topic PRIMARY KEY (article_id, topic_id),
    CONSTRAINT fk_article_topic_topic FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE,
    CONSTRAINT fk_article_topic_article FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE
);
