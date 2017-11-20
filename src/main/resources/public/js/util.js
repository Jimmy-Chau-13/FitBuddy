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