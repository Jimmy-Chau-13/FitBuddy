package app.profile;

import app.model.WorkOut;
import app.util.Path;
import spark.*;

import java.util.*;
import static app.Main.*;

public class ProfileController {

    public static ModelAndView serveProfilePage(Request req, Response res) {
        Map<String,Object> model = new HashMap<>();
        model.put("workout", workoutDao.findAll());
        String username = req.session().attribute("username");
        model.put("username", username);
        return new ModelAndView(model, Path.Template.PROFILE);
    }

    public static ModelAndView addWorkOut(Request req, Response res) {
        String exercise = req.queryParams("exercise");
        int sets = Integer.parseInt(req.queryParams("sets"));
        int reps = Integer.parseInt(req.queryParams("reps"));
        int weight = Integer.parseInt(req.queryParams("weight"));
        WorkOut workout = new WorkOut(exercise,sets,reps,weight);
        workoutDao.add(workout);
        res.redirect(Path.Web.GET_PROFILE_PAGE);
        return null;
    }

}

