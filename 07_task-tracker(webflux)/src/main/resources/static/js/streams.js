//Getting stream
$(document).ready(function() {
    const eventSource = new EventSource('/api/v1/tasks/stream');
    eventSource.onmessage = function(event) {
        const eventData = JSON.parse(event.data);
        console.log(eventData);
        addObserverToTask(eventData);
    };
    eventSource.onerror = function(event) {
        if (event.eventPhase === EventSource.CLOSED) {
            console.log('Connection to event stream was closed.');
        }
    };
});

const addObserverToTask = function(data){
    let taskID = data.id;
    let assigneeID = data.assignee.id;
    const observer = {
        'observerId': assigneeID
    }
    $.ajax({
        method: "PUT",
        url: '/api/v1/tasks/observer/' + taskID,
        data: JSON.stringify(observer),
        contentType: 'application/json',
        success: function()
        {
            console.log('Add observer to task');
        },
        error: function(response)
        {
            alert(response.responseJSON.error);
        }
    });
};