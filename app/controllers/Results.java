package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.EmtipsetException;
import models.GameBet;
import models.User;
import play.mvc.Controller;

import java.util.List;

/**
 *
 */

// TODO: Denna klass ska faktiskt använda SecureSocial
//@With( SecureSocial.class )
public class Results extends Controller {

    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static void list() throws EmtipsetException {
        User user = User.getResultUser();

        List<GameBet> gameBets = GameBet.getByUser(user);

        if(gameBets != null) {
            renderText(gson.toJson(gameBets));
        } else {
            notFound("Couldn't find any game bets. (gameBets == null) !!!");
        }
    }
}
