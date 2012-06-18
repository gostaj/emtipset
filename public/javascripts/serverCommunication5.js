// Team ids of the teams that the user thinks will go to the semi finals.
var semiFinalists = new Array(4);
// Team ids of the teams that the user thinks will go to the final.
var finalists = new Array(2);

function setGameBet(gameId, result) {

var request = $.ajax({
  url: "/gamebet",
  type: "POST",
  data: { gameId: gameId, result: result },
  dataType: "html",
  cache: false
}).done(function(data) {

  // If the server succeeds with placing bet it returns "SUCCESS"
  if (data == 'SUCCESS') {
    // Update the betPlaced class
    $("[id^=game_" + gameId + "_]").closest("td").removeClass("betPlaced");
    $("#game_" + gameId + "_" + result).closest("td").addClass("betPlaced");

    // Update the number of games the user has placed bets on
    var userBets = 24 - $(':radio:checked').length;
    $("#games_to_bet").text(userBets);
  } else {
    $("#game_" + gameId + "_" + result).removeAttr("checked");
    alert( "Problem att kontakta servern (för att tippa), prova att tippa igen eller logga ut och in igen.");
  }

});

request.fail(function(jqXHR, textStatus) {
    $("#game_" + gameId + "_" + result).removeAttr("checked");
    alert( "Problem att kontakta servern (för att tippa), prova att tippa igen eller logga ut och in igen. \nFel: " + textStatus);
});
}


function setKnockOutGameBet(gameId, winningTeamId, winningTeamInArray, nextGameElement) {

var request = $.ajax({
  url: "/gamebet",
  type: "POST",
  data: { gameId: gameId, result: winningTeamId },
  dataType: "html",
  cache: false
}).done(function(data) {
    markKnockOutGameBet(gameId, winningTeamInArray, nextGameElement)
});

request.fail(function(jqXHR, textStatus) {
    alert( "Problem att kontakta servern (för att tippa), prova att tippa igen eller logga ut och in igen. \nFel: " + textStatus);
});
}


function toggleFinalGameBetting() {
    // Enable semi final betting
    $("#semifinals").find("a").removeClass("disabled");
    for (var i = 0; i < semiFinalists.length; i++) {
        if (!semiFinalists[i]) {
            // Disable semi final betting if any semi finalist has not been set
            $("#semifinals").find("a").addClass("disabled");
            break;
        }
    }
    // Enable final game betting
    $("#final").find("a").removeClass("disabled");
    for (var i = 0; i < finalists.length; i++) {
        if (!finalists[i]) {
            // Disable final betting if any finalist has not been set
            $("#final").find("a").addClass("disabled");
            break;
        }
    }
}

function setQuarterFinalGameBet(gameId, winningTeamId, nextGameElement) {
    semiFinalists[gameId-25] = winningTeamId;
    setKnockOutGameBet(gameId, winningTeamId, winningTeamId, nextGameElement);
}

function setSemiFinalGameBet(gameId, winningTeamInArray, nextGameElement) {
    winningTeamId = semiFinalists[winningTeamInArray];
    finalists[gameId-29] = winningTeamId;
    setKnockOutGameBet(gameId, winningTeamId, winningTeamInArray, nextGameElement);
}

function setFinalGameBet(gameId, winningTeamInArray) {
    winningTeamId = finalists[winningTeamInArray];
    setKnockOutGameBet(gameId, winningTeamId, winningTeamInArray, "winner_final");
}

function markKnockOutGameBet(gameId, winningTeamId, nextGameElement) {
    $("#game_" + gameId + "_" + winningTeamId).closest("table").find("a").removeClass("selected");
    $("#game_" + gameId + "_" + winningTeamId).addClass("selected");
    var teamName = $("#game_" + gameId + "_" + winningTeamId).text();
    $("#" + nextGameElement).text(teamName);
    toggleFinalGameBetting();
}

function getGameBets() {

var request = $.getJSON('/gamebets', function(data) {
  $.each(data, function(key, gameBet) {
    if (gameBet.gameId < 25) {
        $("#game_" + gameBet.gameId + "_" + gameBet.result).attr("checked", "checked");
        $("#game_" + gameBet.gameId + "_" + gameBet.result).closest("td").addClass("betPlaced");
    } else if (gameBet.gameId < 29) {
        $("#game_" + gameBet.gameId + "_" + gameBet.result).click();
    } else if (gameBet.gameId == 29) {
        if (gameBet.result < 3) {
           $("#game_" + gameBet.gameId + "_0").click();
        } else {
           $("#game_" + gameBet.gameId + "_2").click();
        }
    } else if (gameBet.gameId == 30) {
        if (gameBet.result < 5) {
           $("#game_" + gameBet.gameId + "_1").click();
        } else {
           $("#game_" + gameBet.gameId + "_3").click();
        }
    } else if (gameBet.gameId == 31) {
        if (gameBet.result < 3 || gameBet.result == 5 || gameBet.result == 6) {
           $("#game_" + gameBet.gameId + "_0").click();
        } else {
           $("#game_" + gameBet.gameId + "_1").click();
        }
    }
    toggleFinalGameBetting()
  });

  // Mark bets as right or wrong and sum the points
  getResults();
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern (hämta tippningar), prova att ladda om sidan eller logga ut och in igen. \nFel: " + textStatus);
});

}

function getResults() {

var request = $.getJSON('/results', function(data) {
  $.each(data, function(key, gameBet) {
    if (gameBet.gameId < 25) {
        var selectedRadio = $("input[id^=game_" + gameBet.gameId + "_]:radio:checked");
        var resultRadio = $("input[id^=game_" + gameBet.gameId + "_" + gameBet.result + "]:radio");

        if (selectedRadio.attr('id') == resultRadio.attr('id')) {
            selectedRadio.closest("td").addClass("right");
        } else {
            selectedRadio.closest("td").addClass("wrong");
        }
    } else if (gameBet.gameId < 29) {
        // TODO: Mark knock out bets as right or wrong
        var selectedTeam = $("#game_" + gameBet.gameId + "_" + gameBet.result);
        if (selectedTeam.hasClass("selected")) {
            selectedTeam.closest("td").addClass("right");
        } else {
            selectedTeam.closest("td").addClass("wrong");
        }
    }
  });

  });

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern (hämta resultat), prova att ladda om sidan eller logga ut och in igen. \nFel: " + textStatus);
});

}

function disableBetting() {

  // Do not show the game bet counter or the countdown
  $("#to_bet").hide();
  $("#countdown").hide();

  // Show the points counter and the place number status
  $("#standings").show();
  $("#points").show();
  $("#place").show();

  // Disable the betting radio buttons
  $(':radio').attr('disabled',true);
}

function getGroupGameBets() {

var request = $.getJSON('/groupGamebets', function(data) {
  $.each(data, function(key, gameBet) {
    var totalPercent = 0;

    $("#game_" + gameBet.gameId + "_bets > .part_1").width(gameBet.result1 + "%").css('left', totalPercent + "%").attr("title", gameBet.result1 + "% tror på 1:a");
    totalPercent += gameBet.result1;

    $("#game_" + gameBet.gameId + "_bets > .part_X").width(gameBet.resultX + "%").css('left', totalPercent + "%").attr("title", gameBet.resultX + "% tror på X");
    totalPercent += gameBet.resultX;

    $("#game_" + gameBet.gameId + "_bets > .part_2").width(gameBet.result2 + "%").css('left', totalPercent + "%").attr("title", gameBet.result2 + "% tror på 2:a");
  });

  $("div.userbets").css('display', 'block');
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern (hämta tippstatistik), prova att ladda om sidan eller logga ut och in igen. \nFel: " + textStatus);
});

}


// Get the scores and put them on the page
function getScores() {

var request = $.getJSON('/scores', function(data) {
  $.each(data, function(key, score) {
    $("#game_" + score.gameId).attr("title", score.score);
  });
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern (hämta matchresultat), prova att ladda om sidan eller logga ut och in igen. \nFel: " + textStatus);
});

}

