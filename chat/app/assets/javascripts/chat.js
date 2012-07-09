(function () {
  var events = new EventSource('/join');
  events.onmessage = function (e) {
    console.log(e.data);
    $('.messages').prepend(Chat.message(JSON.parse(e.data)));
  };
  events.onerror = function (e) {
    console.log(e);
  };

  $('.form button').click(function () {
    $.post('/post', { author: $('[name=author]').val(), content: $('[name=content]').val() }); // TODO handle success/error
    return false
  });
})();