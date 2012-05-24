
# --- !Ups

-- CREATE SEQUENCE users_id_seq;
-- CREATE SEQUENCE game_bets_id_seq;
-- int unique default nextval('users_id_seq')

CREATE TABLE users (
  id                        serial,
  name                      varchar(100),
  email                     varchar(100),
  social_user_id            varchar(100),
  social_provider           varchar(20),
  points                    int default 0,
  created                   timestamp,
  constraint pk_user primary key (id),
  constraint unique_social_user unique (social_user_id, social_provider)
);

--create table Team (
--  id                        bigint not null,
--  name                      varchar(100),
--  constraint pk_team primary key (id)
--);
--
--create table Game (
--  id                         bigint not null,
--  home_team_id               bigint not null,
--  away_team_id               bigint not null,
--  canEndAsTie                boolean,
--  result                     varchar(1),
--  constraint pk_game primary key (id),
--  foreign key (home_team_id) references Team (id) on delete restrict on update restrict,
--  foreign key (away_team_id) references Team (id) on delete restrict on update restrict
--);

create table game_bets (
  id                        serial,
  game_id                   bigint not null,
  user_id                   bigint not null,
  result                    varchar(10) not null,
  updated                   timestamp,
  constraint pk_gamebet primary key (id),
  constraint unique_gamebet_per_user unique (game_id, user_id),
--  foreign key (game_id) references Game (id) on delete restrict on update restrict,
  foreign key (user_id) references users (id) on delete restrict on update restrict
);

--create sequence User_seq start with 100;
--create sequence Team_seq start with 100;
--create sequence Game_seq start with 100;
--create sequence Game_Bet_seq start with 1000;

# --- !Downs

drop table if exists game_bets;
drop table if exists users;
--drop table if exists Team;
--drop table if exists Game;

--drop sequence if exists User_seq;
--drop sequence if exists Team_seq;
--drop sequence if exists Game_seq;
--drop sequence if exists Game_Bet_seq;


