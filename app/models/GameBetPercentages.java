package models;

import com.google.gson.annotations.Expose;

/**
 * Class that represents the percentages of placed bets on 1, X, and 2 for a game.
 */
public class GameBetPercentages {

    @Expose // In JSON
    public Long gameId;

    @Expose // In JSON
    public int result1 = 0;

    @Expose // In JSON
    public int resultX = 0;

    @Expose // In JSON
    public int result2 = 0;

    public GameBetPercentages(Long gameId) {
        this.gameId = gameId;
    }
}
