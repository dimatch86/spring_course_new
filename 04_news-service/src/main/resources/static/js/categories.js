
$(function (){

    const loadCategory = function(data){
        let categoryCode = '<a href="#" class="category-link" data-id="' + data.id + '">' + data.id + '.' + data.tag +  ' - ' + data.newsCount +'</a><br>';
        $('.tag-list')
            .append('<div>' + categoryCode + '</div>');
    };

    const appendCategory = function(data){
        let categoryCode = '<a href="#" class="category-link" data-id="' + data.id + '">' + data.id + '.' + data.tag +  ' - ' + data.newsList.length +'</a><br>';
        $('.tag-list')
            .append('<div>' + categoryCode + '</div>');
    };

    const showCategory = function(data){

        let categoryCode = '<h3 data-id="' + data.id + '">' + data.tag + '</h3>';
        let newsList = data['newsList'];
        let newsCode = '';
        for(let i in newsList) {
            newsCode = newsCode + '<div class="category-news">' + newsList[i]['text'] + '</div>'
        }
        if (newsCode.length === 0) {
            newsCode = '<span>Нет новостей у данной категории</span>'
        }
        $('.mo-content')
            .append('<div>' + categoryCode + '</div>').append('<hr>').append('<h5>Список новостей:</h5>').append('<div>' + newsCode + '</div>');
    };

    $('.close').click(function(event){
        if(event.target === this) {
            $('.mo').css('display', 'none');
        }
    });

    //Show adding category form
    $('#create-tag-btn').click(function(){
        $('#category-form').css('display', 'flex');
    });

    //Show edit form
    $('#edit-tag-btn').click(function(){
        $('#edit-category-form').css('display', 'flex');
    });

    //Show deleting category form
    $('#del-tag-btn').click(function(){
        $('#del-category-form').css('display', 'flex');
    });

    //Load categories
    $.get('/api/v1/category?pageSize=2&pageNumber=0', function(response)
    {
        const property = 'categoryResponseList';
        const categoryList = response[property];
        for(let i in categoryList) {

            loadCategory(categoryList[i]);
        }
    });
    $('#tag-page').click(function () {

        let page = $('#tag-page').val();
        $.ajax({
            method: "GET",
            url: '/api/v1/category?pageSize=2&pageNumber=' + page,
            success: function(response)
            {
                $('.tag-list').children().remove();
                const property = 'categoryResponseList';
                for(let i in response[property]) {

                    loadCategory(response[property][i]);
                }
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;

    });

    //Adding category
    $('#save-category').click(function()
    {
        const data = {
            'tag': $('#category_name').val()
        };
        $.ajax({
            method: "POST",
            url: '/api/v1/category',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(response)
            {
                $('.common-form').css('display', 'none');
                appendCategory(response);

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Update category
    $('#edit-category').click(function()
    {
        let data = {
            'tag': $('#newCategory').val()
        };

        let categoryId = $('#category_id').val();
        $.ajax({
            method: "POST",
            url: '/api/v1/category/' + categoryId,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function()
            {
                $('#edit-category-form').css('display', 'none');
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Getting categories
    $(document).on('click', '.category-link', function(){
        let link = $(this);
        let categoryId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/api/v1/category/' + categoryId,
            success: function(response)
            {

                $('.mo').css('display', 'flex');
                $('.mo-content').children().remove();
                showCategory(response);

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Deleting category
    $('#delete-category').click(function(){
        let categoryId = $('#category-id').val();

        $.ajax({
            method: "DELETE",
            url: '/api/v1/category/' + categoryId,
            success: function()
            {
                $('#del-category-form').css('display', 'none');
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