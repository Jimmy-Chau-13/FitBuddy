package app.Profile;

import app.auth.AuthController;
import app.superset.SupersetController;
import app.util.Path;
import app.workout.WorkOutController;
import com.google.gson.Gson;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class ProfileController {

    static Gson gson = new Gson();

    public static ModelAndView serveProfile(Request req, Response res) {

        String userId = AuthController.checkSessionHasUser(req);

        if(userId != null && !userId.isEmpty()) {
            String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();

            HashMap<String,Object> model = new HashMap<>();
            model.put("username", username);

            String eventArray = WorkOutController.getWorkoutMonthEvent(userId);
            String eventArray2 = SupersetController.getSupersetMonthEvent(userId);

            model.put("eventArray", eventArray);
            model.put("eventArray2", eventArray2);
            return new ModelAndView(model, Path.Template.PROFILE);
        }

        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }

    public static String handleViewDate(Request req, Response res) {
        res.type("application/json");
        String date = req.queryParams("date");
        String userId = req.session(false).attribute(Path.Attribute.USERID);
        HashMap<String,Object> model = new HashMap<>();

        if(userId == null || userId.isEmpty()) {
            res.status(500);
            res.redirect(Path.Web.GET_INDEX_PAGE);
            return  gson.toJson(model);
        }

        model = WorkOutController.fetchWorkOuts(userId, date, model);
        model = SupersetController.fetchSupersets(userId, date, model);
        model.put("dateToShow", date);
        return gson.toJson(model);
    }
}
