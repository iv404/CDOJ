/**
 * Javascript for all page.
 */

var currentUser;
var currentUserType;

$(document).ready(function () {
  var $currentUser = $('#currentUser');
  if ($currentUser.length !== 0) {
    currentUser = js.lang.String.trim($currentUser[0].innerHTML);
    currentUserType = $currentUser.attr('type');
    $('#cdoj-user-avatar').setAvatar({
      image: 'http://www.acm.uestc.edu.cn/images/akari_small.jpg',
      size: $('#cdoj-user-avatar').width()
    });
  }

  $('#cdoj-login-button').setButton({
    callback: function() {
      var info=$('#cdoj-login-form').getFormData();

      jsonPost('/user/login', info, function(data) {
        $('#cdoj-login-form').formValidate({
          result: data,
          onSuccess: function() {
            window.location.reload();
          }
        });
      });
    }
  });

  $("#logoutButton").setButton({
    callback: function() {
      //noinspection JSUnresolvedFunction
      $.post('/user/logout', function(data) {
        if (data.result == "success")
          window.location.reload();
      });
    }
  });
});
