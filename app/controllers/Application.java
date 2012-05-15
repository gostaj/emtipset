package controllers;

import controllers.securesocial.SecureSocial;
import play.mvc.*;

import java.util.*;

import models.*;

@With( SecureSocial.class )
public class Application extends Controller {

    public static void index() {
        render();
    }

}