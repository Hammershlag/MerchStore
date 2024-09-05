window.addEventListener('load', function() {
    var cookies = document.cookie.split('; ');

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].split('=');
        var key = cookie[0];
        var value = cookie[1];

        if (key === 'login' && value === 'new') {
            // Change the value to 'loaded'
            document.cookie = 'login=loaded; path=/';
            // Display a popup
            alert('Login successful');
            break;
        }
    }
});