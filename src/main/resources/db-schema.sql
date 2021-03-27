-- drop tables
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `member`;

create table comment
(
    comment_id    bigint not null primary key,
    created_date  timestamp,
    modified_date timestamp,
    content       text,
    member_id     bigint
);

create table member
(
    member_id         bigint not null primary key,
    created_date      timestamp,
    modified_date     timestamp,
    authority         varchar(255),
    display_id        varchar(255),
    email             varchar(255),
    nickname          varchar(255),
    password          varchar(255),
    profile_image_url varchar(255)
);
