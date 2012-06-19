package controllers;

import controllers.securesocial.SecureSocial;
import mocks.MockFactory;
import models.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@With( SecureSocial.class )
public class Application extends Controller {

    private static final String REFERENCE_GROUP_NAME = "referens";

    public static void index(boolean disableAutoLogin) {
        if (disableAutoLogin) {
            render();
        } else {
            SocialUser user = getSocialUser();
            User emUser = User.getUser(user);
            emUser.updateUserFromSocialUser(user);

            boolean hasTournamentStarted = hasTournamentStarted();
            boolean haveGroupGamesEnded = haveGroupGamesEnded();
            boolean hasKnockOutPhaseStarted = hasKnockOutPhaseStarted();

            int userPlaceInGroup = User.getUserPlaceInGroup(emUser);
            int usersInGroup = User.getUserCountInUserGroup(emUser.group);

            long secondsUntilTournamentStart = getSecondsUntilTournamentStart();
            long secondsUntilKnockOutPhaseStart = getSecondsUntilKnockOutPhaseStart();

            List<User> topList = getTopList(emUser.group);
            List<User> refTopList = getRefTopList();

            int maxPoints = User.getResultUser().points;

            render(user, emUser, hasTournamentStarted, userPlaceInGroup, usersInGroup, secondsUntilTournamentStart,
                    topList, refTopList, maxPoints, haveGroupGamesEnded, hasKnockOutPhaseStarted,
                    secondsUntilKnockOutPhaseStart);
        }
    }

    private static List<User> getRefTopList() {
        return getTopList(REFERENCE_GROUP_NAME);
    }

    private static List<User> getTopList(String group) {
        if (haveGroupGamesEnded() || Play.mode.isDev()) {
            return User.getUserGroupWeightedPointsSorted(group);
        } else {
            return new ArrayList<User>();
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

    static boolean haveGroupGamesEnded() {
        Calendar tournamentStart = getGroupGamesEndCal();
        Calendar now = Calendar.getInstance();
        return now.after(tournamentStart);
    }

    static boolean hasKnockOutPhaseStarted() {
        Calendar knockOutPhaseStart = getKnockOutPhaseStartCal();
        Calendar now = Calendar.getInstance();
        return now.after(knockOutPhaseStart);
    }

    // The tournament starts the 8th of June 18:00 CET
    private static Calendar getTournamentStartCal() {
        Calendar tournamentStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        tournamentStart.set(2012, Calendar.JUNE, 8, 18, 00);
        return tournamentStart;
    }

    // The group games ends the 19th of June 23:00 CET
    private static Calendar getGroupGamesEndCal() {
        Calendar groupGamesEnd = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        groupGamesEnd.set(2012, Calendar.JUNE, 19, 23, 00);
        return groupGamesEnd;
    }

    // The knock out phase starts the 21th of June 20:45 CET
    private static Calendar getKnockOutPhaseStartCal() {
        Calendar knockOutPhaseStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        knockOutPhaseStart.set(2012, Calendar.JUNE, 21, 20, 45);
        return knockOutPhaseStart;
    }

    private static long getSecondsUntilTournamentStart() {
        Calendar tournamentStart = getTournamentStartCal();
        return (tournamentStart.getTimeInMillis() -
                Calendar.getInstance().getTimeInMillis())/1000;
    }

    private static long getSecondsUntilKnockOutPhaseStart() {
        Calendar knockOutStart = getKnockOutPhaseStartCal();
        return (knockOutStart.getTimeInMillis() -
                Calendar.getInstance().getTimeInMillis())/1000;
    }



}
