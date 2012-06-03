package controllers;

import controllers.securesocial.SecureSocial;
import mocks.MockFactory;
import models.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

//@With( SecureSocial.class )
public class Application extends Controller {

    public static void index(boolean disableAutoLogin) {
        if (disableAutoLogin) {
            render();
        } else {
            SocialUser user = getSocialUser();
            User emUser = User.getUser(user);
            boolean hasTournamentStarted = hasTournamentStarted();
            List<User> groupUsers = User.getUserGroupPointSorted(emUser.group);
            int userPlaceInGroup = groupUsers.indexOf(emUser) + 1; // +1 since index is zero-based
            int usersInGroup = groupUsers.size();
            long secondsUntilTournamentStart = getSecondsUntilTournamentStart();
            render(user, emUser, hasTournamentStarted, userPlaceInGroup, usersInGroup, secondsUntilTournamentStart);
        }
    }

    static SocialUser getSocialUser() {
        if (Play.mode.isProd()) {
            SocialUser socialUser = SecureSocial.getCurrentUser();
            socialUser.avatarUrl = enforceHttpsUrl(socialUser.avatarUrl);
            return socialUser;
        } else {
            return MockFactory.getMockedSocialUser();
        }
    }

    private static String enforceHttpsUrl(String url) {
        if (url.toLowerCase().startsWith("http:")) {
            return "https" + url.substring(4);
        }
        return url;
    }

    static boolean hasTournamentStarted() {
        Calendar tournamentStart = getTournamentStartCal();
        Calendar now = Calendar.getInstance();
        return now.after(tournamentStart);
    }

    // The tournament starts the 8th of June 18:00 CET
    private static Calendar getTournamentStartCal() {
        Calendar tournamentStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        tournamentStart.set(2012, Calendar.JUNE, 8, 18, 00);
        return tournamentStart;
    }

    private static long getSecondsUntilTournamentStart() {
        Calendar tournamentStart = getTournamentStartCal();
        return (tournamentStart.getTimeInMillis() -
                Calendar.getInstance().getTimeInMillis())/1000;
    }


}
