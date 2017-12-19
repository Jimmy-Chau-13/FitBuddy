
/////////////////////////////////////// ALL FUNCTIONS DEALING WITH THE ADD MODAL ////////////////////////////////

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
}

function fillAllSets(sets, reps, weight) {
    for (i = 1; i < sets*1 + 1; i++) {
        var repId = "#reps" + i;
        var weightId = "#weight" + i;
        $(repId).val(reps);
        $(weightId).val(weight);
    }
}

function getReps() {
    var sets = $("#sets").val();
    var reps = new Array(sets);
    for (i = 1; i < sets*1 + 1; i++) {
        var repId = "#reps" + i;
        reps[i*1-1] = $(repId).val();
    }
    return reps;
}

function getWeight() {
    var sets = $("#sets").val();
    var weight = new Array(sets);
    for (i = 1; i < sets*1 + 1; i++) {
        var weightId = "#weight" + i;
        weight[i*1-1] = $(weightId).val();
    }
    return weight;
}

function getWorkout() {
    var data = {};
    data.exercise = $("#exercise").val();
    data.sets = $("#sets").val();
    data.date = $("#date").val();
    data.reps = getReps().toString();
    data.weight = getWeight().toString();
    return data;
}

function checkAddModalBody2IsFilled() {
    var sets = $("#sets").val();
    for (i = 1; i < sets*1 + 1; i++) {
        var repId = "#reps" + i;
        var weightId = "#weight" + i;
        var reps = $(repId).val();
        var weight = $(weightId).val();
        if(reps=="" || weight=="") {
            $("#logModal2").html("Oops you forgot to fill it all");
            return -1;
        }
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
    $("#logModal2").html("");
    $("#body2").empty();
    $("#addModalBody2").hide();
    $("#addModalBody1").show();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////// ALL VIEW MODAL FUNCTIONALITY //////////////////////////////////////////

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
    var modal = $('#editModal');
    var sets = tr.find("td:nth-child(2)").text();
    modal.find('#editSets').val(sets);
    createEditModalBody(sets);

    var exercise = tr.find("td:nth-child(1)").text();
    modal.find('#editExercise').val(exercise);

    var reps = JSON.parse(tr.find("td:nth-child(3)").text());
    for(i = 1; i < sets*1 + 1; i++) {
        var repId = "#editReps" + i;
        $(repId).val(reps[i-1]);
    }

    var weight = JSON.parse(tr.find("td:nth-child(4)").text());
    for(i = 1; i < sets*1 + 1; i++) {
        var weightId = "#editWeight" + i;
        $(weightId).val(weight[i-1]);
    }

    var id = tr.attr("data-id");
    modal.find('#editId').val(id);

    modal.find('#editDate').datepicker('setDate',dateToShow);
    modal.modal('show');
}

function createEditModalBody(sets) {
    for(i = 1; i < sets*1 + 1; i++) {
        var setNum = "Set " + i;
        var repId = "editReps" + i;
        var weightId = "editWeight" + i;

        var html = '<div class="form-group row">' +
            '<div class="col-xs-4">' +
            '<label for="reps">' + setNum + '</label>' +
            '<input class="form-control" id=' + repId + ' type="number" placeholder="Enter # of Reps" required>' +
            '<input class="form-control" id=' + weightId + ' type="number" placeholder="Enter Weight" required>' +
            '</div>' +
            '</div>';
        $("#editBody").append(html);
    }
}

function getEditedReps() {
    var sets = $("#editSets").val();
    var reps = new Array(sets);
    for (i = 1; i < sets*1 + 1; i++) {
        var repId = "#editReps" + i;
        reps[i*1-1] = $(repId).val();
    }
    return reps;
}

function getEditedWeight() {
    var sets = $("#editSets").val();
    var weight = new Array(sets);
    for (i = 1; i < sets*1 + 1; i++) {
        var weightId = "#editWeight" + i;
        weight[i*1-1] = $(weightId).val();
    }
    return weight;
}

function getEditedWorkout() {
    var data = {};
    data.exercise = $("#editExercise").val();
    data.sets = $("#editSets").val();
    data.editId = $("#editId").val();
    data.date = $("#editDate").val();
    data.reps = getEditedReps().toString();
    data.weight = getEditedWeight().toString();
    return data;
}

function checkEditModalBodyIsFilled() {
    var sets = $("#editSets").val();
    if ($("#editDate").val() == "" || $("#editExercise").val() == "") {
        $("#logModal3").html("Oops you forgot to fill it all");
        return -1;
    }
    for (i = 1; i < sets*1 + 1; i++) {
        var repId = "#editReps" + i;
        var weightId = "#editWeight" + i;
        var reps = $(repId).val();
        var weight = $(weightId).val();
        if(reps=="" || weight=="") {
            $("#logModal3").html("Oops you forgot to fill it all");
            return -1;
        }
    }

}
function clearEditModalBody(){
    $("#logModal3").html("");
    $("#editBody").empty();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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



