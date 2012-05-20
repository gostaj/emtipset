var xy = d3.geo.mercator(),
    path = d3.geo.path().projection(xy);

var translate = xy.translate();
translate[0] = 500;
translate[1] = 800; 
 
xy.translate(translate);
xy.scale(3800);

var countries = d3.select("div#map")
  .append("svg")
  .append("g")
    .attr("id", "countries");

d3.json("/public/javascripts/europe.json", function(collection) {
  countries 
    .selectAll("path")
      .data(collection.features)
    .enter().append("path")
      .attr("id", function(d) { return d.properties.ISO_A2; })
      .attr("d", path)
      .on("mouseover", function(d){d3.select(this).style("fill", "#ddd"); getCountryInfo(d);})
      .on("mouseout", function(){d3.select(this).style("fill", "#eee");})
    .append("title")
      .text(function(d) { return d.properties.NAME; });
});

function getCountryInfo(d) {
    d3.select("div#countryinfo h1").text(d.properties.NAME);
}
