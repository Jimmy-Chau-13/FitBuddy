package app.model;

import app.db.DataBaseHelper;
import app.util.Path;
import org.mongodb.morphia.Datastore;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class WorkOutController {

    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;
    static Logger logger = Logger.getLogger("WorkOutController.class");

    public WorkOutController() {

    }

    public static ModelAndView serveProfile(Request req, Response res) {

        // Check if current session is already logged in else return to login page
        String userId = null;
        try {
            userId = req.session(false).attribute(Path.Attribute.USERID);
        }
        catch(Exception e) {
            logger.warning("No current UserId in session");
            res.redirect(Path.Web.GET_INDEX_PAGE);
            return null;
        }

        if(userId != null && !userId.isEmpty()) {
            System.out.println("USER: " + userId);
            String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
            String email = req.session(false).attribute(Path.Attribute.EMAIL).toString();
            HashMap<String,Object> model = fetchWorkOuts(userId);
            model.put("username", username);
            return new ModelAndView(model, Path.Template.PROFILE);
        }

        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }

    public static int handleNewWorkout (Request req, Response res) {

        String userId = req.session(false).attribute(Path.Attribute.USERID);
        String exercise = req.queryParams("exercise");
        int sets = new Integer(req.queryParams("sets"));
        int reps = new Integer(req.queryParams("reps"));
        int weight = new Integer(req.queryParams("weight"));
        WorkOut workout = new WorkOut(exercise,sets,reps,weight);
        logger.info("Adding workout: " + workout.getExercise());
        workout.setUserId(userId);
        datastore = dbHelper.getDataStore();
        datastore.save(workout);
        res.status(200);
        res.redirect(Path.Web.GET_PROFILE_PAGE);
        return 1;

    }

    private static HashMap<String,Object> fetchWorkOuts(String userId) {

        System.out.println("FETCHWORKOUTS");
        // Grab all workouts owned by current user
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId")
                .equal(userId)
                .asList();

        for(int i = 0; i < list.size(); i++) {
            System.out.println("fetched " + list.get(i).getExercise());
        }

        HashMap<String,Object> model = new HashMap<>();
        model.put("workout", list);
        return model;
    }

}
