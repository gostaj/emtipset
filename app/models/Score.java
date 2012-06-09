package models;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a game score, such as "1 - 4" for game id 5
 */
@Entity
@Table(name = "scores")
public class Score extends GenericModel {

    // Extending GenericModel so we can define the id field ourselves.
    @Id
    @Column(name = "game_id")
    public Long gameId;

    @Required
    public String score;

    public Score(Long gameId, String score) {
        this.gameId = gameId;
        this.score = score;
    }

    public static void updateScore(Long gameId, String scoreText) {
        Score score = find("gameId = ?", gameId).first();
        if (score == null) {
            score = new Score(gameId, scoreText);
        } else {
            score.score = scoreText;
        }
        score.save();
    }
}
