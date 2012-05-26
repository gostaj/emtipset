function setGameBet(gameId, result) {

var request = $.ajax({
  url: "/gamebet",
  type: "POST",
  data: { gameId: gameId, result: result },
  dataType: "html",
  cache: false
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern, prova att tippa igen eller logga ut och in igen. \nFel: " + textStatus);
});

}



function getGameBets() {

var request = $.getJSON('/gamebets', function(data) {
  $.each(data, function(key, gameBet) {
    $("#game_" + gameBet.gameId + "_" + gameBet.result).attr("checked", "checked");
  });
});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern, prova att ladda om sidan eller logga ut och in igen. \nFel: " + textStatus);
});

}