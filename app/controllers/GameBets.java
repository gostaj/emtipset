package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.EmtipsetException;
import models.GameBet;
import models.User;
import play.Logger;
import play.mvc.Controller;
import securesocial.provider.SocialUser;

import java.util.List;

/**
 *  http://www.playframework.org/documentation/1.2.4/jpa
 */
//@With( SecureSocial.class )
public class GameBets extends Controller {

    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static void list() throws EmtipsetException {
        User user = getLoggedInUser();

        List<GameBet> gameBets = GameBet.find("byUser", user).fetch();

        // If you need more control over the JSON builder when passing an Object to the renderJSON(…) method, you can also pass in GSON serialisers and Type objects to customise the output.
        // https://sites.google.com/site/gson/gson-user-guide#TOC-Object-Examples  Gson.
        // http://stackoverflow.com/questions/9040060/playframework-json-template-list-ojects-with-foreach

        if(gameBets != null) { //  && !games.isEmpty()
            renderText(gson.toJson(gameBets));
            //renderJSON(gameBets);
        } else {
            notFound("Couldn't find any game bets.");
        }
    }

    public static void create(Long gameId, Character result) throws EmtipsetException {
        User user = getLoggedInUser();
        GameBet.validateResult(result);
        GameBet.placeGameBet(gameId, user, result);
        Logger.info("Placing game bet for user " + user.id + " on game " + gameId + ": " + result);

    }

    private static User getLoggedInUser() throws EmtipsetException {
        SocialUser socialUser = Application.getSocialUser();
        return User.getUser(socialUser);
    }
}
