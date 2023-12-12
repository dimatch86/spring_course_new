$(function(){

    const loadUser = function(data){
        let userCode = '<a href="#" class="user-link" data-id="' + data.id + '">' +  data.userName + ' - ' + data.email +'</a><br>';
        $('.user-list')
            .append('<div>' + userCode + '</div>');
    };

    const appendUser = function(data){
        let userCode = '<a href="#" class="user-link" data-id="' + data.id + '">' +  data.userName + ' - ' + data.email +'</a><br>';
        $('.user-list')
            .append('<div>' + userCode + '</div>');
    };

    const showUser = function(data){

        let userCode = '<h3 id="usr" data-id="' + data.id + '">' + data.userName + '</h3>';

        $('.mo-content')
            .append('<div>' + userCode + '</div>').append('<hr>')
            .append('<h5>Id:</h5>').append('<h5 id="usr-id" data-id="' + data.id + '">' + data.id + '</h5>');
    };

    $('.close').click(function(event){
        if(event.target === this) {
            $('.mo').css('display', 'none');
        }
    });


   //Loading users on load page
   $.get('/api/v1/users', function(response)
    {

        for(let i in response) {

            loadUser(response[i]);
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
                alert(response.responseJSON.error);
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
    $('.common-form').click(function(event){
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

    //Closing modal
    $('.mo').click(function(event){
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
            url: '/api/v1/users/' + userId,
            success: function(response)
            {

                $('#user-modal').css('display', 'flex');
                $('.mo-content').children().remove();
                showUser(response);

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Adding user
    $('#save-contact').click(function()
    {
        const data = {
            'userName': $('#firstName').val(),
            'email': $('#email').val()
        }
        $.ajax({
            method: "POST",
            url: '/api/v1/users',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(response)
            {
                $('#contact-form').css('display', 'none');
                appendUser(response);
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Update user
    $('#edit-contact').click(function()
    {
        const data = {
            'userName': $('#newName').val(),
            'email': $('#newEmail').val()
        }
        let userId = $('#existedId').val();
        $.ajax({
            method: "PUT",
            url: '/api/v1/users/' + userId,
            data: JSON.stringify(data),
            contentType: 'application/json',
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
                url: '/api/v1/users/' + userId,
                success: function()
                {
                $('#del-user-form').css('display', 'none');
                window.location.reload();
                },
                error: function(response)
                {
                    alert(response.responseJSON.error);
                }
            });
            return false;
        });

    //Deleting user from modal
    $('#delete-user-modal').click(function(){
        let userId = $('#usr').data('id');
        $.ajax({
            method: "DELETE",
            url: '/api/v1/users/' + userId,
            success: function()
            {
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });



});