
-- 지금 데이터들은 테스트 돌릴 때 사용되므로 추가되거나 수정되면 안됨 --
-- 테스트를 위해 다른 데이터 필요 시 다른 Member, Post, Follow 만들 것 --
-- 테스트용 ID 비밀번호 : 1q2w3e4r
INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_post_count, member_following_count, member_follower_count)
VALUES (1, 'woody', 'woody@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'woody', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1, 8, 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_post_count, member_following_count, member_follower_count)
VALUES (2, 'alice', 'alice@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'alice', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 15, 2, 2);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_post_count, member_following_count, member_follower_count)
VALUES (3, 'bob', 'bob@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'bob', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 8, 1, 2);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_follower_count)
VALUES (4, 'cat', 'cat@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'cat', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_follower_count)
VALUES (5, 'diamond', 'diamond@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'diamond', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_follower_count)
VALUES (6, 'east', 'east@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'east', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_follower_count)
VALUES (7, 'fuck', 'fuck@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'fuck', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_follower_count)
VALUES (8, 'gg', 'gg@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'gg', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date, member_follower_count)
VALUES (9, 'hello', 'hello@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'hello', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 1);

INSERT INTO MEMBER (member_id, display_id, email, password, nickname, profile_image_url, authority, created_date, modified_date)
VALUES (10, 'ice', 'ice@test.net', '$2a$10$1sRKcKzA7fK.P7Yjj8nva.ygpMN9lMyOGpS1.oy5pWOLj0ALdAA9O', 'ice', null, 'ROLE_USER', '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192');

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
INSERT INTO FOLLOW (follow_id, created_date, modified_date, from_member_id, to_member_id) VALUES (11, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 2, 3);

INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views, post_comment_count) VALUES (1, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0, 1);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views, post_like_count) VALUES (2, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0, 1);
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
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (160, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (190, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (200, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (221, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (223, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);
INSERT INTO POST (post_id, created_date, modified_date, content, member_id, views) VALUES (224, '2021-03-15 00:01:23.278192', '2021-03-15 00:01:23.278192', 'post content', 2, 0);

INSERT INTO comment (created_date, modified_date, content, member_id, post_id, comment_like_count) VALUES ('2021-03-15 00:01:23.278192','2021-03-15 00:01:23.278192','testComment',1,1,0);
INSERT INTO POST_LIKE (created_date, modified_date, post_like_id, member_id, post_id) VALUES ('2021-03-15 00:01:23.278192','2021-03-15 00:01:23.278192', 1, 2, 18);
