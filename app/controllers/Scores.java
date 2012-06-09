package controllers;

import controllers.securesocial.SecureSocial;
import exceptions.EmtipsetException;
import models.Score;
import play.mvc.Controller;
import play.mvc.With;

import java.util.List;

/**
 *  Shows the game scores as JSON.
 */
@With( SecureSocial.class )
public class Scores extends Controller {

    public static void list() throws EmtipsetException {
        List<Score> scores = Score.findAll();

        if(scores != null) {
            renderJSON(scores);
        } else {
            notFound("Couldn't find any scores. (scores == null) !!!");
        }
    }

}
