
# --- !Ups

CREATE TABLE users (
  id                        serial,
  name                      varchar(100),
  email                     varchar(100),
  social_user_id            varchar(100),
  social_provider           varchar(20),
  avatar_url                varchar(300),
  user_group                varchar(20) default 'brothas',
  points                    int default 0,
  created                   timestamp,
  constraint pk_user primary key (id),
  constraint unique_social_user unique (social_user_id, social_provider)
);

create table game_bets (
  id                        serial,
  game_id                   bigint not null,
  user_id                   bigint not null,
  result                    varchar(10) not null,
  updated                   timestamp,
  constraint pk_gamebet primary key (id),
  constraint unique_gamebet_per_user unique (game_id, user_id),
  foreign key (user_id) references users (id) on delete restrict on update restrict
);

insert into users (id, name, email, social_user_id, social_provider, user_group)
 values (0, 'EM-tipset Admin', 'emtipset_adm@yahoo.com',
 'https://me.yahoo.com/a/qWA1G3JrqtvZ9E9VMcYm2mGKZsYxfG1tpg--', 'yahoo', 'admins');

# --- !Downs

drop table if exists game_bets;
drop table if exists users;