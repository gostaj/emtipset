/* http://keith-wood.name/countdown.html
   Swedish initialisation for the jQuery countdown extension
		labels: ['år', 'månader', 'veckor', 'dagar', 'timmar', 'minuter', 'sekunder'],
		labels1: ['år', 'månad', 'vecka', 'dag', 'timme', 'minut', 'sekund'],
   Written by Carl (carl@nordenfelt.com). */
(function($) {
	$.countdown.regional['sv'] = {
		labels: ['år', 'månader', 'veckor', 'dagar', 'tim', 'min', 'sek'],
		labels1: ['år', 'månad', 'vecka', 'dag', 'tim', 'min', 'sek'],
		compactLabels: ['Å', 'M', 'V', 'D'],
		whichLabels: null,
		timeSeparator: ':', isRTL: false};
	$.countdown.setDefaults($.countdown.regional['sv']);
})(jQuery);
