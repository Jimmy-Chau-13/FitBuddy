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

function createWorkoutTable(list) {

    // EXTRACT VALUE FOR HTML HEADER.
    var col = [];
    for (var i = 0; i < list.length; i++) {
        for (var key in list[i]) {
            if (col.indexOf(key) === -1) {
                col.push(key);
            }
        }
    }

    var table = document.createElement("table");
    var tr = table.insertRow(-1);

    // CREATE HTML TABLE HEADER ROW USING THE EXTRACTED HEADERS ABOVE.
    for (var i = 0; i < col.length; i++) {
        var th = document.createElement("th");      // TABLE HEADER.
        th.innerHTML = col[i];
        tr.appendChild(th);
    }

    // ADD JSON DATA TO THE TABLE AS ROWS.
    for (var i = 0; i < list.length; i++) {

        tr = table.insertRow(-1);

        for (var j = 0; j < col.length; j++) {
            var tabCell = tr.insertCell(-1);
            tabCell.innerHTML = list[i][col[j]];
        }
    }

    var divContainer = document.getElementById("table");
    divContainer.innerHTML = "";
    divContainer.appendChild(table);
}

