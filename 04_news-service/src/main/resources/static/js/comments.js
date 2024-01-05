
$(function (){
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');

    const appendComment = function(data){
        let commentCode = '<a href="#" class="comment-link" data-id="' + data.id + '">' + data.id + '.' + data.text + '</a><span> Новость №: ' + data.newsId + '</span><br>';
        $('.comments-list')
            .append('<div>' + commentCode + '</div>');
    };

    //Load comments
    $('#news').click(function () {
        let newsId = $('#news').val();
        $.ajax({
            method: "GET",
            url: '/api/v1/comment/' + newsId,
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function (response) {

                $('.comments-list').children().remove();
                const property = 'commentResponseList';
                const commentList = response[property];

                for (let i in commentList) {
                    appendComment(commentList[i]);
                }

            },
            error: function (response) {
                if (response.status === 404) {
                    alert('Пользователь не найден!');
                }
            }
        });
        return false;
    });

    //Show adding news form
    $('#create-comment-btn').click(function(){
        $('#comment-form').css('display', 'flex');
    });

    //Show edit comment form
    $('#edit-comment-btn').click(function(){
        $('#edit-comment-form').css('display', 'flex');
    });

    //Show delete comment form
    $('#del-comment-btn').click(function(){
        $('#del-comment-form').css('display', 'flex');
    });

    //Adding comment
    $('#save-comment-btn').click(function()
    {
        const data = {
            'userId': $('#user-id-comment').val(),
            'newsId': $('#news-id-comment').val(),
            'text': $('#commentText').val()

        };
        $.ajax({
            method: "POST",
            url: '/api/v1/comment',
            data: JSON.stringify(data),
            contentType: 'application/json',
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function(response)
            {
                $('.common-form').css('display', 'none');
                appendComment(response);

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

    //Update comment
    $('#edit-comment').click(function()
    {
        const data = {
            'commentId': $('#comment_id').val(),
            'text': $('#newcomment').val()

        };
        let commentId = $('#comment_id').val();
        $.ajax({
            method: "POST",
            url: '/api/v1/comment/' + commentId,
            data: JSON.stringify(data),
            contentType: 'application/json',
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function()
            {
                $('#edit-comment-form').css('display', 'none');
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Deleting comment
    $('#delete-comment').click(function(){
        let commentId = $('#comment-id').val();
        $.ajax({
            method: "DELETE",
            url: '/api/v1/comment/' + commentId,
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function()
            {
                $('#del-comment-form').css('display', 'none');
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