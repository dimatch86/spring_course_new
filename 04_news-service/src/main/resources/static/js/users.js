$(function(){

    const loadUser = function(data){
        let userCode = '<a href="#" class="user-link" data-id="' + data.id + '">' + data.id + '.' + data.name + ' - ' + data.newsCount +'</a><br>';
        $('.user-list')
            .append('<div>' + userCode + '</div>');
    };

    const appendUser = function(data){
        let userCode = '<a href="#" class="user-link" data-id="' + data.id + '">' + data.id + '.' + data.name + ' - ' + data.newsList.length +'</a><br>';
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


   //Loading users on load page
   $.get('/api/v1/user?pageSize=2&pageNumber=0', function(response)
    {
        const property = 'users';
        for(let i in response[property]) {

            loadUser(response[property][i]);
        }
    });

    $('#users-page').click(function () {

        let page = $('#users-page').val();
        $.ajax({
            method: "GET",
            url: '/api/v1/user?pageSize=2&pageNumber=' + page,
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

    //Show adding user form
    $('#create-btn').click(function(){
        $('#contact-form').css('display', 'flex');
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

    //Adding user
    $('#save-contact').click(function()
    {
        const data = {
            'name': $('#firstName').val()
        }
        $.ajax({
            method: "POST",
            url: '/api/v1/user',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(response)
            {
                $('#contact-form').css('display', 'none');
                appendUser(response);
            },
            error: function(response)
            {
                if(response.status === 400) {
                    let error = response.responseJSON.error;
                    alert(error);
                }
            }
        });
        return false;
    });

    //Update user
    $('#edit-contact').click(function()
    {
        const data = {
            'name': $('#newName').val()
        }
        let userId = $('#existedId').val();
        $.ajax({
            method: "POST",
            url: '/api/v1/user/' + userId,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function()
            {
                $('#edit-form').css('display', 'none');
                window.location.reload();
            },
            error: function(response)
            {
                if(response.status === 404 || response.status === 400) {
                    let error = response.responseJSON.error;
                    alert(error);
                }
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
                success: function()
                {
                $('#del-user-form').css('display', 'none');
                window.location.reload();
                },
                error: function(response)
                {
                    if(response.status === 500) {
                        alert('Пользователь не найден!');
                    }
                }
            });
            return false;
        });


});