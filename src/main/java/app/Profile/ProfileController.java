package app.Profile;

import app.auth.AuthController;
import app.superset.SupersetController;
import app.util.Path;
import app.workout.WorkOutController;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class ProfileController {

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
}
