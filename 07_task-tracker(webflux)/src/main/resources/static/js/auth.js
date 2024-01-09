$(function(){

    $('#login').click(function (){
        let username = $('#username').val();
        let password = $('#password').val();

        localStorage.setItem('username', username);
        localStorage.setItem('password', password);

        let url = 'http://localhost:8080';
        let xhr = new XMLHttpRequest();
        xhr.open("GET", url, true);
        xhr.setRequestHeader('Authorization', "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))));
        xhr.onreadystatechange = function () {


            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    //window.location.href = xhr.getResponseHeader('Location');
                    window.location.href = '/';

                } else {
                    console.log('WRING!!')
                }
            }
        };
        xhr.send();

    });

    $('#reg-link').click(function () {
        window.location.href = '/registration';
    });

    $('#reg-btn').click(function () {

        let role = $('#regroletype').val();
        const data = {
            'userName': $('#username').val(),
            'email': $('#email').val(),
            'password': $('#password').val()
        }

        $.ajax({
            method: "POST",
            url: '/api/v1/users?roleType=' + role,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function()
            {
                window.location.href = '/login';
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    $('#logout').click(function () {
        localStorage.removeItem('username');
        localStorage.removeItem('password');
        window.location.href = '/login';
    });
});