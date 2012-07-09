(function () {
	var events = new EventSource('/join');
	events.onmessage = function (data) {
		console.log(data);
	};
  events.onerror = function (e) {
    console.log(e);
  }
})();