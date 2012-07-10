(function () {
  var events = new EventSource(Routes.controllers.Chat.join().url);
  events.onmessage = function (e) {
    console.log(e.data);
    $('.messages').prepend(Chat.message(JSON.parse(e.data)));
  };
  events.onerror = function (e) {
    console.log(e);
  };

  $('.form button').click(function () {
    Routes.controllers.Chat.postMessage().ajax({
      data: { author: $('[name=author]').val(), content: $('[name=content]').val() }
    }); // TODO handle success/error
    return false
  });
})();