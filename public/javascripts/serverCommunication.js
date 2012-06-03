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


function getGameBets() {

var request = $.getJSON('/gamebets', function(data) {
  var userBets = 24; // Number of group games
  $.each(data, function(key, gameBet) {
    $("#game_" + gameBet.gameId + "_" + gameBet.result).attr("checked", "checked");
    $("#game_" + gameBet.gameId + "_" + gameBet.result).closest("td").addClass("betPlaced");
    userBets--;
  });
  $("#games_to_bet").text(userBets);
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern (hämta tippningar), prova att ladda om sidan eller logga ut och in igen. \nFel: " + textStatus);
});

}

function getResults() {

var request = $.getJSON('/results', function(data) {
  var userPoints = 0;
  var maxPoints = 0;
  $.each(data, function(key, gameBet) {
    var resultRadio = $("#game_" + gameBet.gameId + "_" + gameBet.result);
    if (resultRadio.attr("checked") == "checked") {
        resultRadio.closest("td").addClass("right");
        userPoints++;
    } else {
        resultRadio.closest("td").addClass("wrong");
    }
    maxPoints++;
  });

  // Print out userPoints and maxPoints
  $("#userPoints").text(userPoints);
  $("#maxPoints").text(maxPoints);

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
  $("#points").show();
  $("#place").show();

  // Disable the betting radio buttons
  $(':radio').attr('disabled',true);
}