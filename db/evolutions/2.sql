
# --- !Ups

CREATE TABLE scores (
  game_id                   bigint not null,
  score                     varchar(20),
  constraint pk_score primary key (game_id)
);

insert into scores (game_id, score)
 values (1, '1 - 1');
insert into scores (game_id, score)
 values (2, '4 - 1');

# --- !Downs

drop table if exists scores;
