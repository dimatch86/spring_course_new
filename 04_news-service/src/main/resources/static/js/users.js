$(function(){
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');
    $('#current').append(username);

    const loadUser = function(data){
        let userCode = '<a href="#" class="user-link" data-id="' + data.id + '">' + data.id + '.' + data.name + ' - ' + data.newsCount +'</a><br>';
        $('.user-list')
            .append('<div>' + userCode + '</div>');
    };


    const showUser = function(data){

        let userCode = '<h3 data-id="' + data.id + '">' + data.name + '</h3>';
        let newsList = data['newsList'];
        let newsCode = '';
        for(let i in newsList) {
            newsCode = newsCode + '<div class="user-news" data-id="' + newsList[i]['id'] + '">' + newsList[i]['text'] + '</div>'
        }
        if (newsCode.length === 0) {
            newsCode = '<span>Нет новостей</span>'
        }
        $('.mo-content')
            .append('<div>' + userCode + '</div>').append('<hr>').append('<h5>Список новостей:</h5>').append('<div>' + newsCode + '</div>');
    };

    $('.close').click(function(event){
        if(event.target === this) {
            $('.mo').css('display', 'none');
        }
    });

    $.ajax({
        method: "GET",
        url: '/api/v1/user?pageSize=2&pageNumber=0',
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
            Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
        },
        success: function (response) {
            const property = 'users';
            for(let i in response[property]) {

                loadUser(response[property][i]);
            }
        },
        error: function (response) {
            if(response.status === 401 || response.status === 403) {
                $('.user-list')
                    .append('<div style="color: red">' + 'Нет прав для просмотра' + '</div>');
            }
        }
    });

    $('#users-page').click(function () {

        let page = $('#users-page').val();
        $.ajax({
            method: "GET",
            url: '/api/v1/user?pageSize=2&pageNumber=' + page,
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function(response)
            {
                $('.user-list').children().remove();
                const property = 'users';
                for(let i in response[property]) {

                    loadUser(response[property][i]);
                }
            },
            error: function(response)
            {
                if(response.status === 404) {
                    alert('Пользователь не найден!');
                }
            }
        });
        return false;

    });


    //Show edit form
    $('#edit-btn').click(function(){
        $('#edit-form').css('display', 'flex');
    });

    //Show deleting user form
        $('#del-btn').click(function(){
            $('#del-user-form').css('display', 'flex');
        });

    //Closing adding user form
    $('#contact-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Closing edit form
    $('#edit-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Closing deleting user form
        $('#del-user-form').click(function(event){
            if(event.target === this) {
                $(this).css('display', 'none');
            }
        });

    //Getting user
    $(document).on('click', '.user-link', function(){
        let link = $(this);
        let userId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/api/v1/user/' + userId,
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function(response)
            {

                $('.mo').css('display', 'flex');
                $('.mo-content').children().remove();
                showUser(response);

            },
            error: function(response)
            {
                if(response.status === 404) {
                    alert('Пользователь не найден!');
                }
            }
        });
        return false;
    });

    //Update user
    $('#edit-contact').click(function()
    {
        const data = {
            'name': $('#newName').val(),
            'password': $('#newPassword').val()
        }
        let userId = $('#existedId').val();
        let role = $('#roletype').val();
        $.ajax({
            method: "POST",
            url: '/api/v1/user/' + userId + '?roleType=' + role,
            data: JSON.stringify(data),
            contentType: 'application/json',
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function()
            {
                $('#edit-form').css('display', 'none');
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Deleting user
        $('#delete-user').click(function(){
        let userId = $('#id').val();
            $.ajax({
                method: "DELETE",
                url: '/api/v1/user/' + userId,
                headers: {
                    "Content-type": "application/x-www-form-urlencoded",
                    Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
                },
                success: function()
                {
                $('#del-user-form').css('display', 'none');
                localStorage.removeItem('username');
                localStorage.removeItem('password');
                window.location.href = "/login";
                },
                error: function(response)
                {
                    alert(response.responseJSON.error);
                }
            });
            return false;
        });


});