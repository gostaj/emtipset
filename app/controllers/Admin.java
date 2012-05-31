package controllers;

import models.GameBet;
import models.User;
import play.Play;
import play.mvc.Controller;
import securesocial.provider.SocialUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

//@With( SecureSocial.class )
public class Admin extends Controller {

    public static void adminPanel() {
        makeSureUserIsAdmin();

        boolean hasTournamentStarted = Application.hasTournamentStarted();
        boolean hasTest1Started = hasTest1Started();
        boolean hasTest2Started = hasTest2Started();
        render(hasTournamentStarted, hasTest1Started, hasTest2Started);
    }

    public static void users() {
        makeSureUserIsAdmin();

        List<User> users = User.findAll();
        render(users);
    }

    public static void deleteResults() {
        makeSureUserIsAdmin();

        int deletedResults = GameBet.deleteResults();
        render(deletedResults);
    }

    public static void sumPoints() {
        makeSureUserIsAdmin();
        List<String> userPoints = new ArrayList<String>();
        List<User> users = User.findAll();
        for (User user : users) {
            user.points = GameBet.getPointsForUser(user);
            user.save();
            userPoints.add(String.format("UserId %d: %d points", user.id, user.points));
        }
        render(userPoints);
    }

    private static void makeSureUserIsAdmin() {
        if (Play.mode.isProd()) {
            SocialUser user = Application.getSocialUser();
            User emUser = User.getUser(user);
            if (!emUser.isResultUser()) {
                error("Sorry, you are not admin!");
            }
        }
    }



    static boolean hasTest1Started() {
        Calendar tournamentStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        tournamentStart.set(2012,Calendar.MAY,30,20,59);
        Calendar now = Calendar.getInstance();
        return now.after(tournamentStart);
    }

    static boolean hasTest2Started() {
        Calendar tournamentStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        tournamentStart.set(2012,Calendar.MAY,30,19,59);
        Calendar now = Calendar.getInstance();
        return now.after(tournamentStart);
    }

}