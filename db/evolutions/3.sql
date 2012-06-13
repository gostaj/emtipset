
# --- !Ups

ALTER TABLE users ADD COLUMN weighted_points int default 0;

# --- !Downs

ALTER TABLE users DROP COLUMN weighted_points;
