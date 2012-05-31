function setGameBet(gameId, result) {

var request = $.ajax({
  url: "/gamebet",
  type: "POST",
  data: { gameId: gameId, result: result },
  dataType: "html",
  cache: false
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern (tippa), prova att tippa igen eller logga ut och in igen. \nFel: " + textStatus);
});

}


function getGameBets() {

var request = $.getJSON('/gamebets', function(data) {
  $.each(data, function(key, gameBet) {
    $("#game_" + gameBet.gameId + "_" + gameBet.result).attr("checked", "checked");
  });
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
  $(':radio').attr('disabled',true);
}