$(function(){
    const username = localStorage.getItem('username');
    const password = localStorage.getItem('password');

    const loadNews = function(data){
        let newsCode = '<a href="#" class="news-link" data-id="' + data.id + '">' + data.id + '.' + data.text + ' - ' + data.userId + ' - ' + data.commentsCount +'</a><br>';
        $('.news-list')
            .append('<div>' + newsCode + '</div>');
    };

    const appendNews = function(data){
        let newsCode = '<a href="#" class="news-link" data-id="' + data.id + '">' + data.id + '.' + data.text + ' - ' + data.userId + '</a><br>';
        $('.news-list')
            .append('<div>' + newsCode + '</div>');
    };

    const showNews = function(data){

        let newsCode = '<h3 data-id="' + data.id + '">' + data.text + '</h3>';
        let commentList = data['commentList'];
        let commentCode = '';
        for(let i in commentList) {
            commentCode = commentCode + '<div class="news-comment">' + commentList[i]['text'] + '</div>'
        }
        if (commentCode.length === 0) {
            commentCode = '<span>Нет комментариев</span>'
        }
        $('.mo-content')
            .append('<div>' + newsCode + '</div>').append('<hr>').append('<h5>Список комментариев:</h5>').append('<div>' + commentCode + '</div>');
    };

    $('.close').click(function(event){
        if(event.target === this) {
            $('.mo').css('display', 'none');
        }
    });

    //Load news
    $.ajax({
        method: "GET",
        url: '/api/v1/news?pageSize=2&pageNumber=0',
        headers: {
            "Content-type": "application/x-www-form-urlencoded",
            Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
        },
        success: function (response) {
            const property = 'newsList';
            const newsList = response[property];
            for(let i in newsList) {

                loadNews(newsList[i]);
            }
        },
        error: function (response) {
            if(response.status === 401 || response.status === 403) {
                $('.news-list')
                    .append('<div style="color: red">' + 'Нет прав для просмотра' + '</div>');
            }
        }
    });

    $('#news-page').click(function () {

        let page = $('#news-page').val();
        $.ajax({
            method: "GET",
            url: '/api/v1/news?pageSize=2&pageNumber=' + page,
            headers: {
                "Content-type": "application/x-www-form-urlencoded",
                Authorization: "Basic " + btoa(username + ":" + password),
            },
            success: function(response)
            {
                $('.news-list').children().remove();
                const property = 'newsList';
                for(let i in response[property]) {

                    loadNews(response[property][i]);
                }
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;

    });

    //Show adding news form
    $('#create-news-btn').click(function(){
        $('#news-form').css('display', 'flex');
    });

    //Closing adding news form
    $('.common-form').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Show edit form
    $('#edit-news-btn').click(function(){
        $('#edit-news-form').css('display', 'flex');
    });

    //Show deleting news form
    $('#del-news-btn').click(function(){
        $('#del-news-form').css('display', 'flex');
    });

    //Adding news
    $('#save-news-btn').click(function()
    {
        const data = {
            'userId': $('#user-id').val(),
            'text': $('#newsText').val(),
            'categories': [
                $('#tag1-id').val(),
                $('#tag2-id').val()
            ]
        };
        $.ajax({
            method: "POST",
            url: '/api/v1/news',
            data: JSON.stringify(data),
            contentType: 'application/json',
            headers: {
                Authorization: "Basic " + btoa(username + ":" + password),
            },
            success: function(response)
            {
                $('.common-form').css('display', 'none');
                appendNews(response);

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Update news
    $('#edit-news').click(function()
    {
        let data = {
            'text': $('#newnew').val(),
            'categories': [
                $('#tag_id').val()
            ]
        };

        let newsId = $('#news_id').val();

        $.ajax({
            method: "POST",
            url: '/api/v1/news/' + newsId,
            data: JSON.stringify(data),
            contentType: 'application/json',
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function()
            {
                $('#edit-news-form').css('display', 'none');
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Deleting news
    $('#delete-news').click(function(){
        let newsId = $('#news-id').val();
        $.ajax({
            method: "DELETE",
            url: '/api/v1/news/' + newsId,
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function()
            {
                $('#del-news-form').css('display', 'none');
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);

            }
        });
        return false;
    });

//Getting news
    $(document).on('click', '.news-link', function(){
        let link = $(this);
        let newsId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/api/v1/news/' + newsId,
            headers: {
                Authorization: "Basic " + btoa(unescape(encodeURIComponent(username + ":" + password))),
            },
            success: function(response)
            {

                $('.mo').css('display', 'flex');
                $('.mo-content').children().remove();
                showNews(response);

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

});