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

import java.util.ArrayList;
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

    public static ModelAndView serveInfo(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
        String date_string = req.queryParams("date");
        Date date;
        if (date_string == null || date_string.isEmpty()){
            date = new Date();
        } else {
            date = DateHelper.stringToDate(date_string);
        }

        String userId = req.session(false).attribute(Path.Attribute.USERID);
        List<WorkOut> list = WorkOutController.getListOfAllWorkoutsOfThisMonth(userId, date);
        if(list.isEmpty()) {
            model.put("username", username);
            model.put("no_workouts", "You have not done any workouts this month");
            model.put("month", DateHelper.dateToMonthYear(date));
            model.put("best_workout", "null");
            return new ModelAndView(model, "monthly_progress.hbs");
        }
        //for (WorkOut w : list) System.out.println(w.toString());

        String total_workout = "Total exercises done: " + list.size();
        String num_days = "Total number of days worked out: " + WorkOutController.getNumberOfDaysWorkoutInAMonth(list);
        ArrayList<WorkOut> fav_workout_list = WorkOutController.getListOfFavoriteWorkoutOfMonth(list);
        String favorite_workout = "Favorite workout: " + fav_workout_list.get(0).getExercise();
        WorkOut best_workout = WorkOutController.getBestWorkoutFromList(fav_workout_list);

        model.put("username", username);
        model.put("total_workout", total_workout);
        model.put("num_days", num_days);
        model.put("favorite_workout", favorite_workout);
        model.put("best_workout", gson.toJson(best_workout));
        model.put("month", DateHelper.dateToMonthYear(date));

        return new ModelAndView(model, "monthly_progress.hbs");
    }
}
