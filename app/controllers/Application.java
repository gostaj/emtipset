package controllers;

import controllers.securesocial.SecureSocial;
import mocks.MockFactory;
import models.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

import java.util.Calendar;
import java.util.TimeZone;

@With( SecureSocial.class )
public class Application extends Controller {

    public static void index(boolean disableAutoLogin) {
        if (disableAutoLogin) {
            render();
        } else {
            SocialUser user = getSocialUser();
            User emUser = User.getUser(user);
            emUser.updateUserFromSocialUser(user);

            boolean hasTournamentStarted = hasTournamentStarted();

            int userPlaceInGroup = User.getUserPlaceInGroup(emUser);
            int usersInGroup = User.getUserCountInUserGroup(emUser.group);

            long secondsUntilTournamentStart = getSecondsUntilTournamentStart();

            render(user, emUser, hasTournamentStarted, userPlaceInGroup, usersInGroup, secondsUntilTournamentStart);
        }
    }

    static SocialUser getSocialUser() {
        if (Play.mode.isProd()) {
            return SecureSocial.getCurrentUser();
        } else {
            return MockFactory.getMockedSocialUser();
        }
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

    // The knock out phase starts the 21th of June 20:45 CET
    private static Calendar getKnockOutPhaseStartCal() {
        Calendar tournamentStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        tournamentStart.set(2012, Calendar.JUNE, 21, 20, 45);
        return tournamentStart;
    }

    private static long getSecondsUntilTournamentStart() {
        Calendar tournamentStart = getTournamentStartCal();
        return (tournamentStart.getTimeInMillis() -
                Calendar.getInstance().getTimeInMillis())/1000;
    }


}
