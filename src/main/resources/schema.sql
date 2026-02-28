drop table if exists message;
drop table if exists conversation;
drop table if exists persona;
drop table if exists follow;
drop table if exists member;

create table member
(
    id            bigint auto_increment primary key,
    email         varchar(255) not null unique,
    nickname      varchar(100),
    password      varchar(100),
    created_at    datetime default current_timestamp
);


create table persona
(
    id                   bigint auto_increment primary key,
    member_id            bigint       not null,
    name                 varchar(100) not null,
    age                  int,
    job                  varchar(100),
    mbti                 varchar(10),
    relationship_type    varchar(50),
    personality_keywords text,
    speech_style         varchar(100),
    intimacy_score       int      default 0,
    trust_score          int      default 0,
    created_at           datetime default current_timestamp,

    constraint fk_persona_member foreign key (member_id) references member (id) on delete cascade
);


create table conversation
(
    id         bigint auto_increment primary key,
    persona_id bigint not null,
    created_at datetime default current_timestamp,

    constraint fk_conversation_persona foreign key (persona_id) references persona (id) on delete cascade
);

create table message
(
    id              bigint auto_increment primary key,
    conversation_id bigint      not null,
    sender_type     varchar(20) not null,
    content         text        not null,
    created_at      datetime default current_timestamp,

    constraint fk_message_conversation foreign key (conversation_id) references conversation (id) on delete cascade
);


create table follow
(
    id           bigint auto_increment primary key,
    follower_id  bigint not null,
    following_id bigint not null,
    created_at   datetime default current_timestamp,

    constraint fk_follow_follower foreign key (follower_id) references member (id) on delete cascade,
    constraint fk_follow_following foreign key (following_id) references member (id) on delete cascade
);