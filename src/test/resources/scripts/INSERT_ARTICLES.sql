INSERT INTO topic (name, created_date, status) VALUES ('Technology', CURRENT_DATE, 'CREATED');
INSERT INTO topic (name, created_date, status) VALUES ('Science', CURRENT_DATE, 'CREATED');
INSERT INTO topic (name, created_date, status) VALUES ('Health', CURRENT_DATE, 'CREATED');
INSERT INTO topic (name, created_date, status) VALUES ('Cooking', CURRENT_DATE, 'CREATED');
INSERT INTO topic (name, created_date, status) VALUES ('Education', CURRENT_DATE, 'CREATED');

INSERT INTO article (title, status, content, created_date)
VALUES ('The Future of AI', 'PUBLISHED', 'Content about AI and its future', CURRENT_DATE);

INSERT INTO article (title, status, content, created_date)
VALUES ('Space Exploration', 'APPROVED', 'Content about space missions', CURRENT_DATE);

INSERT INTO article (title, status, content, created_date)
VALUES ('Cooking', 'APPROVED', 'Content about cooking', '2023-02-18');

INSERT INTO article (title, status, content, created_date)
VALUES ('Education', 'SUBMITTED', 'Content about education', CURRENT_DATE);

INSERT INTO article (title, status, content, created_date)
VALUES ('Healthy Living', 'SUBMITTED', 'Content about health and wellness', '2023-04-01');

INSERT INTO article_topic (article_id, topic_id) VALUES (1, 1);
INSERT INTO article_topic (article_id, topic_id) VALUES (2, 2);
INSERT INTO article_topic (article_id, topic_id) VALUES (3, 4);
INSERT INTO article_topic (article_id, topic_id) VALUES (4, 5);
INSERT INTO article_topic (article_id, topic_id) VALUES (5, 3);

