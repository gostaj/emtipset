package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.securesocial.SecureSocial;
import exceptions.EmtipsetException;
import models.GameBet;
import models.GameBetPercentages;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  http://www.playframework.org/documentation/1.2.4/jpa
 */
@With( SecureSocial.class )
public class GameBets extends Controller {

    public static Map<String, List<GameBetPercentages>> groupBetsCache = new HashMap<String, List<GameBetPercentages>>();

    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private static final char TOTAL_CHAR = 'T';

    public static void listUserBets() throws EmtipsetException {
        User user = getLoggedInUser();

        List<GameBet> gameBets = GameBet.getByUser(user);

        // If you need more control over the JSON builder when passing an Object to the renderJSON(…) method, you can also pass in GSON serialisers and Type objects to customise the output.
        // https://sites.google.com/site/gson/gson-user-guide#TOC-Object-Examples  Gson.
        // http://stackoverflow.com/questions/9040060/playframework-json-template-list-ojects-with-foreach

        if(gameBets != null) { //  && !games.isEmpty()
            renderText(gson.toJson(gameBets));
            //renderJSON(gameBets);
        } else {
            notFound("Couldn't find any game bets.");
        }
    }

    public static void listGroupBets() throws EmtipsetException {
        User user = getLoggedInUser();

        List<GameBetPercentages> gameBetPercentagesList;

        if (groupBetsCache.containsKey(user.group)) {
            gameBetPercentagesList = groupBetsCache.get(user.group);
        } else {
            gameBetPercentagesList = generateBetPercentagesList(user.group);
            groupBetsCache.put(user.group, gameBetPercentagesList);
        }

        renderText(gson.toJson(gameBetPercentagesList));
    }

    private static List<GameBetPercentages> generateBetPercentagesList(String group) {
        List<GameBet> groupGameBets = GameBet.getGroupBets(group);

        HashMap<Long, HashMap<Character, Integer>> groupBetsMap = new HashMap<Long, HashMap<Character, Integer>>();
        for (GameBet gameBet : groupGameBets) {
            if (!groupBetsMap.containsKey(gameBet.gameId)) {
                groupBetsMap.put(gameBet.gameId, new HashMap<Character, Integer>());
            }

            if (groupBetsMap.get(gameBet.gameId).containsKey(gameBet.result)) {
                groupBetsMap.get(gameBet.gameId).put(gameBet.result, groupBetsMap.get(gameBet.gameId).get(gameBet.result) + 1);
            } else {
                groupBetsMap.get(gameBet.gameId).put(gameBet.result, 1);
            }

            if (groupBetsMap.get(gameBet.gameId).containsKey(TOTAL_CHAR)) {
                groupBetsMap.get(gameBet.gameId).put(TOTAL_CHAR, groupBetsMap.get(gameBet.gameId).get(TOTAL_CHAR) + 1);
            } else {
                groupBetsMap.get(gameBet.gameId).put(TOTAL_CHAR, 1);
            }
        }

        List<GameBetPercentages> gameBetPercentagesList = new ArrayList<GameBetPercentages>();
        int totalBets;
        HashMap<Character, Integer> gameBets;
        GameBetPercentages gameBetPercentages;
        for (Long gameId : groupBetsMap.keySet()) {
            gameBetPercentages = new GameBetPercentages(gameId);
            totalBets = groupBetsMap.get(gameId).get(TOTAL_CHAR);
            gameBets = groupBetsMap.get(gameId);
            for (Character result : gameBets.keySet()) {
                switch(result) {
                    case '1':
                        gameBetPercentages.result1 = (gameBets.get(result)*100/totalBets);
                        break;
                    case 'X':
                        gameBetPercentages.resultX = (gameBets.get(result)*100/totalBets);
                        break;
                    case '2':
                        gameBetPercentages.result2 = (gameBets.get(result)*100/totalBets);
                        break;
                }
            }
            gameBetPercentagesList.add(gameBetPercentages);
        }

        return  gameBetPercentagesList;
    }

    public static void create(Long gameId, Character result) throws EmtipsetException {
        User user = getLoggedInUser();
        if (Application.hasTournamentStarted() && !user.isResultUser()) {
            error("Tournament has started, no bets can be made.");
        }
        GameBet.validateResult(result);
        GameBet.placeGameBet(gameId, user, result);
        Logger.info("Placed game bet for user " + user.id + " on game " + gameId + ": " + result);
        renderText("SUCCESS");
    }

    private static User getLoggedInUser() throws EmtipsetException {
        SocialUser socialUser = Application.getSocialUser();
        return User.getUser(socialUser);
    }
}
