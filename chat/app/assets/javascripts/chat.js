(function () {
  var events = new EventSource('/join');
  events.onmessage = function (e) {
    console.log(e.data);
    $('#children').prepend(Chat.child(JSON.parse(e.data).name));
  };
  events.onerror = function (e) {
    console.log(e);
  };
})();