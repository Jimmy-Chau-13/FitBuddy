
/////////////////////////////////////// ALL FUNCTIONS DEALING WITH THE ADD MODAL ////////////////////////////////

function createAddModalBody2(sets) {
    for(var i = 1; i < sets*1 + 1; i++) {
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
    for (var i = 1; i < sets*1 + 1; i++) {
        var repId = "#reps" + i;
        reps[i-1] = $(repId).val();
    }
    return reps;
}

function getWeight() {
    var sets = $("#sets").val();
    var weight = new Array(sets);
    for (i = 1; i < sets*1 + 1; i++) {
        var weightId = "#weight" + i;
        weight[i-1] = $(weightId).val();
    }
    return weight;
}

function getWorkout() {
    var data = {};
    data.exercise = $("#exercise").val();
    data.sets = $("#sets").val();
    data.reps = getReps();
    data.weight = getWeight();
    return data;
}

function checkAddModalBody2IsFilled() {
    var sets = $("#sets").val();
    for (var i = 1; i < sets*1 + 1; i++) {
        var repId = "#reps" + i;
        var weightId = "#weight" + i;
        var reps = $(repId).val();
        var weight = $(weightId).val();
        if(reps==="" || weight==="") {
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

/////////////////////////////////////////////////////END OF ADD MODAL FUNCTIONS/////////////////////////////////////////////////////////////

/////////////////////////////////////SUPERSET FUNCTIONS///////////////////////////////////////////////////////////////
function createSupersetModalBody2(sets, num_exercises, curr_exercise) {

    var the_exercise_number = curr_exercise * 1 + 1;
    var html =  '<label for="superset_exercise_name"> Exercise ' + the_exercise_number + '</label>' +
                 '<input class="form-control" id = "superset_exercise_name" placeholder="Exercise Name" required>';
    $("#supersetBody2").append(html);

    for(var i = 0; i < sets*1 ; i++) {
        var setNum = "Set " + (i*1 + 1);
        var repId = "reps" + i;
        var weightId = "weight" + i;

        var html = '<div class="form-group row">' +
            '<div class="col-xs-4">' +
            '<label for="reps">' + setNum + '</label>' +
            '<input class="form-control" id=' + repId + ' type="number" placeholder="Enter # of Reps" required>' +
            '<input class="form-control" id=' + weightId + ' type="number" placeholder="Enter Weight" required>' +
            '</div>' +
            '</div>';
        $("#supersetBody2").append(html);
    }

    if(curr_exercise === num_exercises*1 - 1) {
        var html = '<button type="button" class="btn btn-primary" id="supersetConfirmBtn">Confirm</button>';
        $("#supersetBody2").append(html);
    }
    else {
        var html = '<button type="button" class="btn btn-primary" id="supersetBody2NextBtn">Next</button>';
        $("#supersetBody2").append(html);
    }
}

function getSupersetReps() {
    var sets = $("#superset_sets").val();
    var reps = [];
    for (var i = 0; i < sets*1 ; i++) {
        var repId = "#reps" + i;
        reps[i*1] = $(repId).val();
    }
    return reps;
}

function getSupersetWeight() {
    var sets = $("#superset_sets").val();
    var weight = [];
    for (var i = 0; i < sets*1; i++) {
        var weightId = "#weight" + i;
        weight[i] = $(weightId).val();
    }
    return weight;
}

function getSupersetExercise() {
    var workout = {};
    workout.exercise = $("#superset_exercise_name").val();
    workout.sets = $("#superset_sets").val();
    workout.reps = getSupersetReps();
    workout.weight = getSupersetWeight();
    return workout;
}




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////// ALL VIEW MODAL FUNCTIONALITY //////////////////////////////////////////

// date should be in the format as MM/dd/YYYY
function openViewModal(date) {
    $.ajax({
        type: "post",
        url: "/view",
        data: {"date": date},
        success: function(response) {
            var workout = response.workouts;
            var workouts_id = response.workouts_id;
            var dateToShow = response.dateToShow;
            var supersets = response.supersets;
            var supersets_id = response.supersets_id;
            createWorkoutTable(workout, workouts_id);
            createSupersetTable(supersets, supersets_id);
            $("#theDateToShow").html(dateToShow);
            $("#viewModal").modal('show');

            $(".deleteBtn").on("click", function () {
                var tr = $(this).closest("tr");
                deleteBtnClicked(tr);
            });

            $(".editBtn").on("click", function () {
                var tr = $(this).closest("tr");
                editBtnClicked(tr,dateToShow);
            });

            $(".deleteSupersetBtn").on('click', function () {
                console.log("DELETE");
                var tr = $(this).closest("tr");
                deleteSupersetBtnClicked(tr);
            });
        }
    });

}

// Construct a table when the view modal is open for a workout on a certain day
function createWorkoutTable(list, workouts_id) {

    var trHTML = '<table id="workoutTable"><thead><tr><th>EXERCISE</th>' +
        '<th>SETS</th> <th>REPS</th><th>WEIGHT</th></tr></thead>';
    $.each(list, function(i,item) {
        trHTML += '<tr data-id=' +workouts_id[i]+ '><td>' + item.exercise + '</td><td>' + item.sets + '</td><td>' +item.reps +
        '</td><td>' + item.weight +
            '</td><td><button class="editBtn" name="edit"> Edit </button></td>' +
            '<td><button class="deleteBtn"> Delete </button></td></tr>';
    });
    trHTML += '</table>'
    $('#workoutTableDiv').append(trHTML);
}

function  createSupersetTable(list, supersets_id) {
    var html = '';
    $.each(list, function (i,item) {
        var superset_num = "Superset " + (i*1+1);
        html += '<br><table><tr data-id='+ supersets_id[i] + '><th colspan="3" style="text-align: center;">' +superset_num+ '</th>' +
            '<th style="align: right;"><button class="deleteSupersetBtn" style="background-color: darkblue; ' +
            'border-color: darkblue "> Delete </button></th></tr>' +
            '<tr><th>EXERCISE</th><th>SETS</th>' +
           '<th>REPS</th><th>WEIGHT</th></tr>';

       $.each(item.workouts, function (j, workout) {
           html += '<tr ><td>' + workout.exercise + '</td><td>' + workout.sets + '</td><td>' + workout.reps +
               '</td><td>' + workout.weight + '</td></tr>';
       });
       html += '</table><br>';
    });

    $('#supersetTableDiv').append(html);
}

function deleteSupersetBtnClicked(tr) {
    var data = {};
    data.supersetId = tr.attr("data-id");
    $.ajax({
        type: "post",
        url: "/delete_superset",
        data: data,
        success: function(response) {
            $("#viewModalLog").html("<strong>DELETED</strong>");
            tr.parent().remove();
            deleteSupersetEvent(response.date, response.numberOfSupersets);
        },
        error: function() {
            $("#viewModalLog").html("<strong>OOPS! UNABLE TO DELETE WORKOUT! PLEASE TRY AGAIN</strong>");
        }
    });
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

    var reps = tr.find("td:nth-child(3)").text().split(',');
    for(var i = 1; i < sets*1 + 1; i++) {
        var repId = "#editReps" + i;
        $(repId).val(reps[i-1]);
    }

    var weight = tr.find("td:nth-child(4)").text().split(',');
    for( i = 1; i < sets*1 + 1; i++) {
        var weightId = "#editWeight" + i;
        $(weightId).val(weight[i-1]);
    }

    var id = tr.attr("data-id");
    modal.find('#editId').val(id);

    modal.find('#editDate').datepicker('setDate',dateToShow);
    modal.modal('show');
}

function createEditModalBody(sets) {
    for(var i = 1; i < sets*1 + 1; i++) {
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
    for (var i = 1; i < sets*1 + 1; i++) {
        var repId = "#editReps" + i;
        reps[i-1] = $(repId).val();
    }
    return reps;
}

function getEditedWeight() {
    var sets = $("#editSets").val();
    var weight = new Array(sets);
    for (var i = 1; i < sets*1 + 1; i++) {
        var weightId = "#editWeight" + i;
        weight[i-1] = $(weightId).val();
    }
    return weight;
}

function getEditedWorkout() {
    var data = {};
    data.exercise = $("#editExercise").val();
    data.sets = $("#editSets").val();
    data.reps = getEditedReps();
    data.weight = getEditedWeight();
    return data;
}

function checkEditModalBodyIsFilled() {
    var sets = $("#editSets").val();
    if ($("#editDate").val() === "" || $("#editExercise").val() === "") {
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

////////////////////////////////////////////////END OF VIEW MODAL////////////////////////////////////////////////////////////////

/////////////////////////////////////////////FULL CALENDAR EVENT//////////////////////////////////////////
// increment the calendar's event by one
function addWorkoutEvent(date, numberOfWorkouts) {
    var event;
    var calendar = $("#calendar");
    if(numberOfWorkouts === "1 workout") {
         event = {id : date + " workout", title : numberOfWorkouts,
            start : date, allDay : true};
        calendar.fullCalendar('renderEvent', event, true);
    }

    else {
        event = calendar.fullCalendar('clientEvents', date + " workout");
        event[0].title = numberOfWorkouts;
        calendar.fullCalendar('updateEvent', event[0]);
    }
}

// decrement the calendar's event by one
function deleteWorkoutEvent(date, numberOfWorkouts) {
    if(numberOfWorkouts === "0 workouts")
        $("#calendar").fullCalendar('removeEvents',date + " workout");
    else {
        var event = $("#calendar").fullCalendar('clientEvents', date + " workout");
        event[0].title = numberOfWorkouts;
        $("#calendar").fullCalendar('updateEvent', event[0]);
    }
}

function addSupersetEvent(date, numberOfSupersets) {

    if(numberOfSupersets == "1 supersets") {
        var event = {id : date + " superset", title : numberOfSupersets,
            start : date, allDay : true, color: "green"};
        $("#calendar").fullCalendar('renderEvent', event, true);
    }

    else {
        var event = $("#calendar").fullCalendar('clientEvents', date + " superset");
        event[0].title = numberOfSupersets;
        $("#calendar").fullCalendar('updateEvent', event[0]);
    }

}

// decrement the calendar's event by one
function deleteSupersetEvent(date, numberOfSupersets) {
    if(numberOfSupersets == "0 supersets")
        $("#calendar").fullCalendar('removeEvents',date + " superset");
    else {
        var event = $("#calendar").fullCalendar('clientEvents', date + " superset");
        event[0].title = numberOfSupersets;
        $("#calendar").fullCalendar('updateEvent', event[0]);
    }
}

// Construct a table for the graph
function createGraphTable(list,dates) {

    var trHTML = '<table><thead><tr><th>EXERCISE</th>' +
        '<th>SETS</th> <th>REPS</th><th>WEIGHT</th><th>DATE</th></tr></thead>';
    $.each(list, function(i,item) {
        trHTML += '<tr><td>' + item.exercise + '</td><td>' + item.sets + '</td><td>' +item.reps +
            '</td><td>' + item.weight +'</td><td>'+dates[i]+'</td></tr>';
    });
    trHTML += '</table>';
    $('#graphTable').append(trHTML);
}

// Given date (YYYY-MM-dd) reformat it to (MM/dd/YYYY)
function formatDate(date) {
    var dArr = date.split("-");
    return dArr[1]+ "/" +dArr[2]+ "/" +dArr[0];

}

// Given date (MM/dd/YYYY)
// return in Json format date = {year:year, month:month, day:day
function dateToJson(date) {
    var dArr = date.split("/");
    var date = {year: dArr[2], month: dArr[0], day: dArr[1] }
    return date;

}




