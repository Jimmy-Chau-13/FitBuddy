function saveNew(data) {

    console.log("Saving = \n" + JSON.stringify(data));

    $.ajax({
        type: "post",
        url: "/add",
        data: data,
        success: function(response) {
            $("#logModal").html("<strong>WORKOUT SAVED SUCCESSFULLY!</strong>");
            console.log("response location = " + response.target);
            if(response.target) {
                window.location.href = response.target;
            }
        },
        error: function() {
            $("#logModal").html("<strong>OOPS! UNABLE TO SAVE CONTACT! PLEASE TRY AGAIN</strong>");
        }
    });
}

// Opens a modal for editing existing workout or adding a new workout
function openModal(button){
    var mode = button.attr("name");
    var modal = $('#workoutModal');

    if(mode == "add" ) {
        console.log("ADD BUTTON CLICKED");
        $(".modal-title").html("Add a New Workout");
    }

    else {
        console.log("EDIT BUTTON CLICKED");
        $(".modal-title").html("Edit a Workout");
        var exercise = button.data('exercise');
        var sets = button.data('sets');
        var reps = button.data('reps');
        var weight = button.data('weight');
        var id = button.data('id');
        var date = button.data('date');

        modal.find('#editExercise').val(exercise);
        modal.find('#editSets').val(sets);
        modal.find('#editReps').val(reps);
        modal.find('#editWeight').val(weight);
        modal.find('#editId').val(id);
        modal.find('#date').datepicker('setDate', date);
    }

    modal.find('#mode').val(mode);
}

// Construct a table for a workout on a certain day
function createWorkoutTable(list) {

    var trHTML = '';
    $.each(list, function(i,item) {
        console.log("exercise: " + item.exercise +
                    "\nid: " + item.id);
        trHTML += '<tr data-id=' +item.id+ '><td>' + item.exercise + '</td><td>' + item.sets + '</td><td>' +item.reps +
        '</td><td>' + item.weight +
            '</td><td><button type="button" class="editBtn"> Edit </button></td>' +
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
        },
        error: function() {
            $("#viewModalLog").html("<strong>OOPS! UNABLE TO DELETE WORKOUT! PLEASE TRY AGAIN</strong>");
        }
    });
}
