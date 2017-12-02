

// Opens a modal for editing existing workout or adding a new workout
function openAddModal(mode){
    var modal = $('#addModal');
    modal.modal('show');

    if(mode == "add" ) {
        console.log("ADD BUTTON CLICKED");
        $(".modal-title").html("Add a New Workout");
    }
    else {
        console.log("EDIT BUTTON CLICKED");
        $(".modal-title").html("Edit Workout");
    }
    modal.find('#mode').val(mode);
}

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
                console.log("DELETE BUTTON CLICKED");
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

// Construct a table for a workout on a certain day
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
            if(response.numberOfWorkouts == "0 workouts")
                $("#calendar").fullCalendar('removeEvents', response.date);
            else {
                var event = $("#calendar").fullCalendar('clientEvents', response.date);
                console.log(event[0].title);
                event[0].title = response.numberOfWorkouts;
                $("#calendar").fullCalendar('updateEvent', event[0]);
            }
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

function clearAddModal(){
    var modal = $('#addModal');
    modal.find('#editExercise').val("");
    modal.find('#editSets').val("");
    modal.find('#editReps').val("");
    modal.find('#editWeight').val("");
    modal.find('#editId').val("");
    modal.find('#date').datepicker('setDate',new Date());
    $("#logModal").html("");

}

// Given date (YYYY-MM-dd) reformat it to (MM/dd/YYYY)
function formatDate(date) {
    var dArr = date.split("-");
    return dArr[1]+ "/" +dArr[2]+ "/" +dArr[0];

}



