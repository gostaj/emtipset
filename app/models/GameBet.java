package models;

import com.google.gson.annotations.Expose;
import exceptions.EmtipsetException;
import play.data.validation.Required;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game_bets")
public class GameBet extends GenericModel {

    // Extending GenericModel so we can define the id field ourselves and use the DB sequence.
    @Id
    @SequenceGenerator(name="game_bets_seq", sequenceName="game_bets_id_seq", allocationSize=0)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="game_bets_seq")
    public Long id;

    @Required
    @Column(name = "game_id")
    @Expose // In JSON
    public Long gameId;

    @Required
    @ManyToOne
    @JoinColumn(name="user_id") //removed the id reference, let JPA manage it
    public User user;

    @Required
    @Expose // In JSON
    public char result;

    public Date updated = new Date();

    public GameBet(Long gameId, User user, char result) {
        validateResult(gameId, result);
        this.gameId = gameId;
        this.result = result;
        this.user = user;
    }

    public static void validateResult(Long gameId, char result) {
        if (gameId <= 24 && (result != '1' && result != 'X' && result != '2')) {
            throw new EmtipsetException("Result " + result + " is not any of 1, X or 2!");
        }
    }

    /**
     * Create a GameBet
     */
    public static GameBet create(Long gameId, User user, char result) {
        GameBet gameBet = new GameBet(gameId, user, result);
        gameBet.save();
        return gameBet;
    }


    // Creates or updates the bet for the game for this user.
    public static void placeGameBet(Long gameId, User user, char result) {
        GameBet gameBet = getGameBet(gameId, user);
        if (gameBet == null) {
            create(gameId, user, result);
        } else {
            update(gameBet, result);
        }
    }

    private static void update(GameBet gameBet, char gameResult) {
        validateResult(gameBet.gameId, gameResult);
        gameBet.result = gameResult;
        gameBet.updated = new Date();
        gameBet.save();
    }

    private static GameBet getGameBet(Long gameId, User user) {
        return find("select g from GameBet g " +
                "where g.gameId = ? " +
                "and g.user = ? ", gameId, user).first();
    }

    public static List<GameBet> getByUser(User user) {
        return find("select g from GameBet g " +
                    "where g.user = ? order by g.gameId", user).fetch();
    }

    public static int deleteResults() {
        return delete("user = ?", User.getResultUser());
    }

    public static int getPointsForUser(User user, Long startGameId, Long endGameId) {
        return new Long(count("select count(*) from GameBet r, GameBet g " +
                "where r.user = ? " +
                "and g.user = ? " +
                "and r.gameId >= ? " +
                "and r.gameId <= ? " +
                "and r.result = g.result " +
                "and r.gameId = g.gameId", User.getResultUser(), user, startGameId, endGameId)).intValue();
    }

    public static List<GameBet> getCorrectGameBetsForUser(User user) {
        return find("select r from GameBet r, GameBet g " +
                "where r.user = ? " +
                "and g.user = ? " +
                "and r.result = g.result " +
                "and r.gameId = g.gameId", User.getResultUser(), user).fetch();
    }

    public static int getNumberOfBetsPlaced(User user) {
        return new Long(GameBet.count("user = ?", user)).intValue();
    }

    public static List<GameBet> getGroupBets(String group) {
        return find("select gb from GameBet gb, User u " +
                "where u.group = ? " +
                "and gb.user = u", group).fetch();
    }
}
