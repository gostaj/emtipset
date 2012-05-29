var xy = d3.geo.mercator(),
    path = d3.geo.path().projection(xy);

var translate = xy.translate();
translate[0] = 600; //500
translate[1] = 600; //780
 
xy.translate(translate);
xy.scale(2100); //3500

var countries = d3.select("div#map")
  .append("svg")
  .append("g")
    .attr("id", "countries");

d3.json("public/javascripts/countryinfo.json", function(data) {

d3.json("public/javascripts/europe.json", function(collection) {
  countries 
    .selectAll("path")
      .data(collection.features)
    .enter().append("path")
      .attr("id", function(d) { return d.properties.GU_A3; })
      .attr("class", function(d) {
          var tag = "non_uefa"
          if (data.q[d.properties.GU_A3]) 
              tag = "grp_" + data.q[d.properties.GU_A3].group
          else if (data.dnq.indexOf(d.properties.GU_A3) != -1) 
              tag = "dnq"
          return tag
       })
      .attr("d", path)
      .on("mouseover", function(d){d3.select(this).style("fill-opacity", "0.7"); getCountryInfo(d, data);})
      .on("mouseout", function(){d3.select(this).style("fill-opacity", "1");})
    .append("title")
      .text(function(d) {
            return d.properties.NAME;
      });
});
});

function getCountryInfo(d, data) {
    d3.select("div#countryinfo h2").text(d.properties.SE_NAME);
    if (data.q[d.properties.GU_A3]) {
        d3.select("div#countryinfo div#flag img").attr("src", "public/images/" + d.properties.GU_A3 + ".png").attr("alt", d.properties.GU_A3);
        d3.select("div#countryinfo div#info").text("");
        d3.select("div#countryinfo div#info").append("p").attr("id", "fifa_rank").text("Fifa-ranking: " + data.q[d.properties.GU_A3].fifa_rank);
        d3.select("div#countryinfo div#info").append("p").attr("id", "group").text("Grupp " + data.q[d.properties.GU_A3].group);
        d3.select("div#countryinfo div#info").append("p").attr("id", "odds").text("Odds för vinst: " + data.q[d.properties.GU_A3].odds + " (Unibet)"); 
        d3.select("div#countryinfo div#info").append("p").attr("id", "odds_14").text("Odds för placering 1-4: " + data.q[d.properties.GU_A3].odds_14 + " (Unibet)");
        var status;
        switch(data.q[d.properties.GU_A3].status) {
            case "active": status = ""; break;
            case "elim_grp": status = "Utslagna i gruppspelet"; break;
            case "elim_quarter": status = "Utslagna i kvartsfinal"; break;
            case "elim_semi": status = "Utslagna i semi-final"; break;
            case "win_3rd" : status = "Vinnare av EM-brons!"; break;
            case "elim_final": status = "Vinnare av EM-silver!"; break;
            case "win_final": status = "Vinnare av EM-guld!"; break;
        }
        d3.select("div#countryinfo div#info").append("p").attr("id", "status").text(status);
       
    } else if (data.dnq.indexOf(d.properties.GU_A3) != -1) {
        d3.select("div#countryinfo div#flag img").attr("src", "public/images/" + d.properties.GU_A3 + "-bw.png").attr("alt", d.properties.GU_A3);
        d3.select("div#countryinfo div#info").text("Lyckades ej kvalificera");
    } else {
        d3.select("div#countryinfo div#flag img").attr("src", "public/images/" + d.properties.GU_A3 + "-bw.png").attr("alt", d.properties.GU_A3);
        d3.select("div#countryinfo div#info").text("Ej medlem i UEFA");
    }
}

