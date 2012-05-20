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
      .on("mouseover", function(d){d3.select(this).style("fill-opacity", "0.7"); getCountryInfo(d);})
      .on("mouseout", function(){d3.select(this).style("fill-opacity", "1");})
    .append("title")
      .text(function(d) {
            return d.properties.NAME;
      });
});
});

function getCountryInfo(d) {
    d3.select("div#countryinfo h2").text(d.properties.NAME);
    var a2 = d.properties.GU_A3;
    d3.select("div#countryinfo span#flag img").attr("src", "img/" + a2 + ".png").attr("alt", a2);
}

