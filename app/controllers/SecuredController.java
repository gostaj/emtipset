package controllers;

import controllers.securesocial.SecureSocial;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Controller that is secured so that the other controllers can optionally
 * be secured if they need by calling the makeSureUserIsLoggedIn() method.
 *
 */
@With( SecureSocial.class )
public class SecuredController extends Controller {

    public static int makeSureUserIsLoggedIn() {
        return 0;
    }
}
