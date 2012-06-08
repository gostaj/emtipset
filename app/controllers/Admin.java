package controllers;

import controllers.securesocial.SecureSocial;
import models.GameBet;
import models.User;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

import java.util.ArrayList;
import java.util.List;

@With( SecureSocial.class )
public class Admin extends Controller {

    public static void adminPanel() {
        makeSureUserIsAdmin();

        boolean hasTournamentStarted = Application.hasTournamentStarted();
        render(hasTournamentStarted);
    }

    public static void users() {
        makeSureUserIsAdmin();

        List<User> users = User.getAllIdSorted();
        for (User user : users) {
            user.betsPlaced = GameBet.getNumberOfBetsPlaced(user);
        }
        render(users);
    }

    public static void topList() {
        makeSureUserIsAdmin();

        List<User> users = User.getAllPointSorted();
        render(users);
    }


    public static void changeUser(Long userId, String group, String name, String avatarUrl) {
        makeSureUserIsAdmin();
        User user = User.findById(userId);
        if (user != null) {
            updateUserIfNotEmptyInfo(user, group, name, avatarUrl);
            users();
        } else {
            error("User with given id not found!");
        }
    }

    private static void updateUserIfNotEmptyInfo(User user, String group, String name, String avatarUrl) {
        if (group != null && !group.isEmpty()) {
            user.group = group;
        }
        if (name != null && !name.isEmpty()) {
            user.name = name;
        }
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            user.avatarUrl = avatarUrl;
        }
        user.save();
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
}