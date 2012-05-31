package controllers;

import controllers.securesocial.SecureSocial;
import mocks.MockFactory;
import models.User;
import play.Play;
import play.mvc.Controller;
import securesocial.provider.SocialUser;

import java.util.Calendar;
import java.util.TimeZone;

//@With( SecureSocial.class )
public class Application extends Controller {

    public static void index(boolean disableAutoLogin) {
        if (disableAutoLogin) {
            SecuredController.makeUserUserIsLoggedIn();
            render();
        } else {
            SocialUser user = getSocialUser();
            User emUser = User.getUser(user);
            boolean hasTournamentStarted = hasTournamentStarted();
            render(user, emUser, hasTournamentStarted);
        }
    }

    static SocialUser getSocialUser() {
        if (Play.mode.isProd()) {
            SecuredController.makeUserUserIsLoggedIn();
            return SecureSocial.getCurrentUser();
        } else {
            return MockFactory.getMockedSocialUser();
        }
    }


    // The tournament starts the 8th of June 18:00 CET
    static boolean hasTournamentStarted() {
        Calendar tournamentStart = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        tournamentStart.set(2012, Calendar.JUNE, 8, 17, 59);
        Calendar now = Calendar.getInstance();
        return now.after(tournamentStart);
    }

}