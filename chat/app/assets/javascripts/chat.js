(function () {
  // --- Message stream
  var events = new EventSource(Routes.controllers.Chat.messages().url);
  events.onmessage = function (e) {
    console.log(e.data);
    $('.messages').append(Chat.message(JSON.parse(e.data)));
    var ms = $('.messages')[0];
    ms.scrollTop = ms.scrollHeight - ms.offsetHeight;
  };
  events.onerror = function (e) {
    console.log(e);
  };


  // --- Join
  // TODO Use a state machine instead of global handlers
  $(document).on('keydown', '.login input', function (e) {
    if (e.keyCode == 13) {
      login($('.login input').val());
    }
  });
  $(document).on('click', '.login button', function (e) {
    login($('.login input').val());
  });

  var login = function (username) {
    // TODO Client side validation
    Routes.controllers.Chat.login(username).ajax({
      success: function () {
        $('.login').replaceWith(Chat.connectedUser(username));
      },
      error: function () {
        alert('Unable to log in!'); // TODO distinguish between 4xx and 5xx errors
      }
    });
  };


  // --- Post
  $(document).on('keydown', '.form input', function (e) {
    if (e.keyCode == 13) {
      Routes.controllers.Chat.postMessage().ajax({
        data: { content: $('[name=content]').val() }
      }); // TODO handle success/error
      $('[name=content]').val(''); // TODO history?
    }
  });


  // --- Leave
  $(document).on('click', '.form button', function () {
    Routes.controllers.Chat.logout().ajax({
      success: function () {
        $('.form').replaceWith(Chat.login());
      }
    });
  });

})();