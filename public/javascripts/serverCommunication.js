function setGameBet(gameId, result) {

var request = $.ajax({
  url: "/gamebet",
  type: "POST",
  data: { gameId: gameId, result: result },
  dataType: "html",
  cache: false
});

request.done(function(msg) {

});

request.fail(function(jqXHR, textStatus) {
  alert( "Problem att kontakta servern, prova att tippa igen eller logga ut och in igen. \nFel: " + textStatus);
});

}