CREATE TYPE article_status AS ENUM (
    'CREATED',
    'SUBMITTED',
    'APPROVED',
    'PUBLISHED'
);

CREATE TABLE IF NOT EXISTS article (
    id SERIAL,
    title VARCHAR(50) UNIQUE NOT NULL,
    status article_status NOT NULL,
    content TEXT NOT NULL,
    created_date DATE NOT NULL,
    published_date DATE,
    CONSTRAINT pk_article PRIMARY KEY (id),
    CONSTRAINT unique_article_title UNIQUE (title)
);