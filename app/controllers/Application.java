package controllers;

import controllers.securesocial.SecureSocial;
import mocks.MockFactory;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import securesocial.provider.SocialUser;

//@With( SecureSocial.class )
public class Application extends Controller {

    public static void index(boolean disableAutoLogin) {
        if (disableAutoLogin) {
            Logger.info("Disabling auto login");
            SecuredController.makeUserUserIsLoggedIn();
            render();
        } else {
            SocialUser user = getSocialUser();
            render(user);
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

}