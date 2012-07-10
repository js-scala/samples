(function () {
  var events = new EventSource(Routes.controllers.App.join().url);
  events.onmessage = function (e) {
    console.log(e.data);
    $('.messages').append(Chat.message(JSON.parse(e.data)));
  };
  events.onerror = function (e) {
    console.log(e);
  };

  $('.form input').keydown(function (e) {
    if (e.keyCode == 13) {
      Routes.controllers.App.postMessage().ajax({
        data: { content: $('[name=content]').val() }
      }); // TODO handle success/error
      $('[name=content]').val(''); // TODO history?
    }
  });
})();