
/////////////////////////////////////// ALL FUNCTIONS DEALING WITH THE ADD MODAL ////////////////////////////////

// Opens a modal for editing existing workout or adding a new workout
function openAddModal(mode){
    var modal = $('#addModal');
    modal.modal('show');

    if(mode == "add" ) {
        $(".modal-title").html("Add a New Workout");
    }
    else {
        $(".modal-title").html("Edit Workout");
    }
    modal.find('#mode').val(mode);
}

function createAddModalBody2(sets) {
    for(i = 1; i < sets*1 + 1; i++) {
        var setNum = "Set " + i;
        var repId = "reps" + i;
        var weightId = "weight" + i;

        var html = '<div class="form-group row">' +
            '<div class="col-xs-4">' +
            '<label for="reps">' + setNum + '</label>' +
            '<input class="form-control" id=' + repId + ' type="number" placeholder="Enter # of Reps" required>' +
            '<input class="form-control" id=' + weightId + ' type="number" placeholder="Enter Weight" required>' +
            '</div>' +
            '</div>';
        $("#body2").append(html);
    }
    var confirmBtn = '<button type="button" class="btn btn-primary" id="confirmBtn">Confirm</button>';
    $("#addModalBody2").append(confirmBtn);
}

function fillAllSets(sets, reps, weight) {
    for (i = 1; i < sets*1 + 1; i++) {
        var repId = "#reps" + i;
        var weightId = "#weight" + i;
        $(repId).val(reps);
        $(weightId).val(weight);
    }
}

function clearAddModalBody1(){
    var modal = $('#addModal');
    $("#logModal").html("");
    modal.find('#exercise').val("");
    modal.find('#sets').val("");
    modal.find('#editId').val("");
    modal.find('#date').datepicker('setDate',new Date());
}

function clearAddModalBody2(){
    $("#logModal").html("");
    $("#body2").empty();
    $("#confirmBtn").remove();
    $("#addModalBody2").hide();
    $("#addModalBody1").show();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// date should be in the format as MM/dd/YYYY
function openViewModal(date) {
    $.ajax({
        type: "post",
        url: "/view",
        data: {"date": date},
        success: function(response) {
            var workout = JSON.parse(response.jsonData);
            var dateToShow = response.dateToShow;

            $("#theDateToShow").html(dateToShow);
            createWorkoutTable(workout);
            $("#viewModal").modal('show');

            $(".deleteBtn").on("click", function () {
                //console.log("DELETE BUTTON CLICKED");
                var tr = $(this).closest("tr");
                deleteBtnClicked(tr);
            });

            $(".editBtn").on("click", function () {
                var tr = $(this).closest("tr");
                editBtnClicked(tr,dateToShow);
            });
        }
    });

}

// Construct a table when the view modal is open for a workout on a certain day
function createWorkoutTable(list) {

    var trHTML = '';
    $.each(list, function(i,item) {
        //console.log("exercise: " + item.exercise +
         //           "\nid: " + item.id);
        trHTML += '<tr data-id=' +item.id+ '><td>' + item.exercise + '</td><td>' + item.sets + '</td><td>' +item.reps +
        '</td><td>' + item.weight +
            '</td><td><button class="editBtn" name="edit"> Edit </button></td>' +
            '<td><button class="deleteBtn"> Delete </button></td></tr>';
    });

    $('#workoutTable').append(trHTML);
}


function deleteBtnClicked(tr) {
    var data = {};
    data.workoutId = tr.attr("data-id");
    console.log("DELETING = " + data.workoutId );
    $.ajax({
        type: "post",
        url: "/delete",
        data: data,
        success: function(response) {
            $("#viewModalLog").html("<strong>DELETED</strong>");
            tr.remove();
            deleteWorkoutEvent(response.date, response.numberOfWorkouts);
        },
        error: function() {
            $("#viewModalLog").html("<strong>OOPS! UNABLE TO DELETE WORKOUT! PLEASE TRY AGAIN</strong>");
        }
    });
}

// Populate forms with the exercise we want to edit
function editBtnClicked(tr, dateToShow) {

    $("#viewModal").modal('hide');
    openAddModal("edit");
    var modal = $('#addModal');

    var exercise = tr.find("td:nth-child(1)").text();
    modal.find('#editExercise').val(exercise);

    var sets = tr.find("td:nth-child(2)").text();
    modal.find('#editSets').val(sets);

    var reps = tr.find("td:nth-child(3)").text();
    modal.find('#editReps').val(reps);

    var weight = tr.find("td:nth-child(4)").text();
    modal.find('#editWeight').val(weight);

    var id = tr.attr("data-id");
    modal.find('#editId').val(id);

    modal.find('#date').datepicker('setDate',dateToShow);
}



// increment the calendar's event by one
function addWorkoutEvent(date, numberOfWorkouts) {

    if(numberOfWorkouts == "1 workouts") {
        var event = {id : date, title : numberOfWorkouts,
            start : date, allDay : true};
        $("#calendar").fullCalendar('renderEvent', event, true);
    }

    else {
        var event = $("#calendar").fullCalendar('clientEvents', date);
        event[0].title = numberOfWorkouts;
        $("#calendar").fullCalendar('updateEvent', event[0]);
    }

}

// decrement the calendar's event by one
function deleteWorkoutEvent(date, numberOfWorkouts) {
    if(numberOfWorkouts == "0 workouts")
        $("#calendar").fullCalendar('removeEvents',date);
    else {
        var event = $("#calendar").fullCalendar('clientEvents', date);
        event[0].title = numberOfWorkouts;
        $("#calendar").fullCalendar('updateEvent', event[0]);
    }
}



// Given date (YYYY-MM-dd) reformat it to (MM/dd/YYYY)
function formatDate(date) {
    var dArr = date.split("-");
    return dArr[1]+ "/" +dArr[2]+ "/" +dArr[0];

}



