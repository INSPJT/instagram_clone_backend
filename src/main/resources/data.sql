INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (1, 'woody', 'woody@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'woody', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (2, 'alice', 'alice@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'alice', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (3, 'bob', 'bob@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'bob', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (4, 'cat', 'cat@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'cat', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (5, 'diamond', 'diamond@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'diamond', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (6, 'east', 'east@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'east', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (7, 'fuck', 'fuck@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'fuck', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (8, 'gg', 'gg@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'gg', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (9, 'hello', 'hello@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'hello', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date) VALUES (10, 'ice', 'ice@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'ice', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');


INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (1, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 2);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (2, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 3);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (3, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 2, 1);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (4, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 3, 2);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (5, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 4);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (6, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 5);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (7, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 6);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (8, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 7);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (9, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 8);
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (10, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 9);


INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (1, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (2, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (3, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (5, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (8, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (11, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (12, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (14, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (17, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (18, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (24, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (32, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (65, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (66, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (70, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (89, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (100, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 3, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (150, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 1, 0);


INSERT INTO comment (created_date, modified_date, content, member_id, post_id) VALUES ('2021-03-15 00:01:23.278192','2021-03-15 00:01:23.278192','testComment',1,1);