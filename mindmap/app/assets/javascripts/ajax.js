window.Ajax = window.Ajax || (function () {
  var Ajax = {
    call: function (settings) {
      
      var contentTypes = {
        'xml': 'text/xml',
        'html': 'text/html',
        'json': 'application/json',
        'text': 'text/plain'
      };
      var textParser = function (xhr) { return xhr.responseText };
      var dataParsers = {
        'xml': function (xhr) { return xhr.responseXML; },
        'html': textParser,
        'json': function (xhr) { return JSON.parse(xhr.responseText); },
        'text': textParser
      };

      var parseResponse = function (type, xhr) {
        return (dataParsers[type] || textParser)(xhr)
      };
      
      var xhr = new XMLHttpRequest();
      xhr.open(settings.method || (settings.action && settings.action.method) || 'GET', settings.url || (settings.action && settings.action.url), true);
      xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
          var status = Math.floor(xhr.status / 100);
          settings.complete && settings.complete(xhr);
          if (status === 2 || status === 3) {
            settings.success && settings.success(parseResponse(settings.type, xhr), xhr);
          } else {
            settings.error && settings.error(parseResponse(settings.type, xhr), xhr);
          }
        }
      };
      var data = new FormData();
      if (settings.data) {
        for (var p in settings.data) {
          data.append(p, settings.data[p])
        }
      }
      settings.progress && (xhr.onprogress = settings.progress);
      contentTypes[settings.type] && xhr.setRequestHeader('Accept', contentTypes[settings.type]);
      xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
      xhr.send(data);
      return xhr;
    },
    
    getJSON: function (url, callback) {
      return Ajax.call({
        url: url,
        method: 'GET',
        type: 'json',
        success: callback
      })
    }
  };

  return Ajax;
})();