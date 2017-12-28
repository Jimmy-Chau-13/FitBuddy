package app.Profile;

import app.auth.AuthController;
import app.event.EventController;
import app.superset.SupersetController;
import app.util.DateHelper;
import app.util.Path;
import app.workout.WorkOut;
import app.workout.WorkOutController;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProfileController {

    private static Gson gson = new Gson();

    public static ModelAndView serveProfile(Request req, Response res) {

        String userId = AuthController.checkSessionHasUser(req);
        String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();

        HashMap<String,Object> model = new HashMap<>();
        model.put("username", username);

        String eventArray = EventController.getAllWorkoutEvent(userId);
        String eventArray2 = SupersetController.getSupersetMonthEvent(userId);

        model.put("eventArray", eventArray);
        model.put("eventArray2", eventArray2);
        return new ModelAndView(model, Path.Template.PROFILE);
    }

    public static String handleViewDate(Request req, Response res) {
        res.type("application/json");
        String date_string = req.queryParams("date");
        Date date = DateHelper.stringToDate(date_string);
        String userId = req.session(false).attribute(Path.Attribute.USERID);
        HashMap<String,Object> model = new HashMap<>();
        List<WorkOut> workout_list = WorkOutController.getListOfWorkoutsOnADay(userId, date);

        model.put("workouts", workout_list);
        model.put("workouts_id", WorkOutController.getArrayOfWorkoutIdsFromList(workout_list));
        //model = SupersetController.fetchSupersets(userId, date, model);
        model.put("dateToShow", date_string);
        return gson.toJson(model);
    }
}
