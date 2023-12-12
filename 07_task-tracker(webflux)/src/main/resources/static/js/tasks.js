$(function(){

    const appendTask = function(data){
        let taskCode = '<a href="#" class="task-link" data-id="' + data.id + '">' +  data.name + ' - ' + data.description +'</a><br>';
        $('.task-list')
            .append('<div>' + taskCode + '</div>');
    };

    const showTask = function(data){

        let taskCode = '<h3 id="tsk" data-id="' + data.id + '">' + data.name + '</h3>';
        let observers = data.observers;

        $('.mo-content')
            .append('<div>' + taskCode + '</div>').append('<hr>')
            .append('<h5 id="tsk-id" data-id="' + data.id + '">Id: ' + data.id + '</h5>')
            .append('<h5 id="tsk-name" data-descr="' + data.name + '">Name: ' + data.name + '</h5>')
            .append('<h5 id="tsk-description" data-descr="' + data.description + '">Description: ' + data.description + '</h5>')
            .append('<h5 id="tsk-status" data-descr="' + data.status + '">Status: ' + data.status + '</h5>')
            .append('<h5>Author: ' + data.author.userName + ', ' + data.author.email + '</h5>')
            .append('<h5 id="task-assignee" data-descr="' + data.assignee.id + '">Assignee: ' + data.assignee.userName + ', ' + data.assignee.email + '</h5>')
            .append('<h5>Observers:</h5>');
        for (let i in observers) {
            $('.mo-content').append('<div> - ' + observers[i].userName + ', ' + observers[i].email + '</div>')
        }
    };

    //Show adding task form
    $('#create-task-btn').click(function(){
        $('#create-task-form').css('display', 'flex');
    });

    //Show edit task form
    $('#edit-task-btn').click(function(){
        $('#edit-task-form').css('display', 'flex');
    });

    //Show del task form
    $('#del-task-btn').click(function(){
        $('#del-task-form').css('display', 'flex');
    });


    $('#add-task').click(function(){
        let assigneeId = $('#usr-id').data('id');
        $('#create-task-form').css('display', 'flex');
        $('#assignee').val(assigneeId);
    });

    //Loading tasks on page
    $.get('/api/v1/tasks', function(response)
    {
        for(let i in response) {
            appendTask(response[i]);
        }
    });

    //Adding task
    $('#save-task').click(function()
    {
        const data = {
            'name': $('#taskName').val(),
            'description': $('#description').val(),
            'authorId': $('#author').val(),
            'assigneeId': $('#assignee').val()
        }
        $.ajax({
            method: "POST",
            url: '/api/v1/tasks',
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function(response)
            {
                $('#create-task-form').css('display', 'none');
                appendTask(response);
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //Getting task
    $(document).on('click', '.task-link', function(){
        let link = $(this);
        let taskId = link.data('id');
        $.ajax({
            method: "GET",
            url: '/api/v1/tasks/' + taskId,
            success: function(response)
            {

                $('#task-modal').css('display', 'flex');
                $('.mo-content').children().remove();
                showTask(response);

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    //adding observers
    $('#add-observer').click(function(){
        let taskId = $('#tsk-id').data('id');
        $('#to-task').val(taskId);

        $.ajax({
            method: "GET",
            url: '/api/v1/users',
            success: function(response)
            {

                $('#observers-modal').css('display', 'flex');
                $('.observers-content').children().remove();
                for (let i in response) {
                    $('.observers-content').append('<div><a href="#" class="observer-link" data-id="' + response[i].id + '">' + response[i].userName + '</a></div>')
                }

            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    $(document).on('click', '.observer-link', function(){
        let observerId = $(this).data('id');
        let taskId = $('#to-task').val();
        const data = {
            'observerId': observerId
        }
        $.ajax({
            method: "PUT",
            url: '/api/v1/tasks/observer/' + taskId,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function()
            {
                $('#observers-modal').css('display', 'none');
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });
    //Closing modal
    $('.mod').click(function(event){
        if(event.target === this) {
            $(this).css('display', 'none');
        }
    });

    //Update task
    $('#edit-task').click(function()
    {
        const data = {
            'name': $('#task-name').val(),
            'description': $('#task-description').val(),
            'taskStatus': $('#status-select').val(),
            'assigneeId': $('#assignee-new').val()
        }
        let taskId = $('#existedTaskId').val();
        $.ajax({
            method: "PUT",
            url: '/api/v1/tasks/' + taskId,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function()
            {
                $('#edit-task-form').css('display', 'none');
                clenEditTaskForm();
                window.location.reload();
            },
            error: function(response)
            {
                alert(response.responseJSON.error);
            }
        });
        return false;
    });

    $('#edit-task-modal').click(function(){
        let taskId = $('#tsk-id').data('id');
        let taskName = $('#tsk-name').data('descr');
        let taskDescr = $('#tsk-description').data('descr');
        let taskStatus = $('#tsk-status').data('descr');
        let assigneeId = $('#task-assignee').data('descr');
        $('#edit-task-form').css('display', 'flex');
        $('#existedTaskId').val(taskId);
        $('#task-name').val(taskName);
        $('#task-description').val(taskDescr);
        $('#status-select').val(taskStatus);
        $('#assignee-new').val(assigneeId);
    });

    //Clean edit form
    const clenEditTaskForm = function(){
        $('#existedTaskId').val();
        $('#task-name').val();
        $('#task-description').val();
        $('#status-select').val();
        $('#assignee-new').val();
    };


    //Deleting task from modal
    $('#delete-task-modal').click(function(){
        let taskId = $('#tsk-id').data('id');
        $.ajax({
            method: "DELETE",
            url: '/api/v1/tasks/' + taskId,
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

    //Deleting task from del-form
    $('#delete-task').click(function(){
        let taskId = $('#task-id').val();
        $.ajax({
            method: "DELETE",
            url: '/api/v1/tasks/' + taskId,
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