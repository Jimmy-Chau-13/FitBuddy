package app.workout;

import app.db.DataBaseHelper;
import app.util.Path;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.mongodb.morphia.Datastore;
import spark.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class WorkOutController {

    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;
    static Logger logger = Logger.getLogger("WorkOutController.class");
    static Gson gson = new Gson();

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
            String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
            String email = req.session(false).attribute(Path.Attribute.EMAIL).toString();
            HashMap<String,Object> model = fetchWorkOuts(userId);
            model.put("username", username);
            return new ModelAndView(model, Path.Template.PROFILE);
        }

        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }

    public static String handleUpdateWorkout (Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();

        String userId = req.session(false).attribute(Path.Attribute.USERID);

        String exercise = Jsoup.parse(req.queryParams("exercise")).text();
        int sets = Integer.parseInt(Jsoup.parse(req.queryParams("sets")).text());
        int reps = Integer.parseInt(Jsoup.parse(req.queryParams("reps")).text());
        int weight = Integer.parseInt(Jsoup.parse(req.queryParams("weight")).text());

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        try {
            date = df.parse(req.queryParams("date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String mode = Jsoup.parse(req.queryParams("mode")).text();
        WorkOut workout = new WorkOut(exercise,sets,reps,weight,date);

        logger.info(mode + " workout: " + workout.getExercise() +
                        "\nSets " + workout.getSets() +
                        "\nReps " +workout.getReps() +
                        "\nWeight " + workout.getWeight() +
                        "\nDate " + df.format(date));

        if(userId != null && !userId.isEmpty()) {
            workout.setUserId(userId);
            logger.info("Where user id is " + userId);

            if(mode.equals("edit")) {
                String workoutId = Jsoup.parse(req.queryParams("editId")).text();
                logger.info("Editing workout id " + workoutId);
                if(workoutId != null && !workoutId.isEmpty())
                    workout.setId(workoutId);
                else {
                    logger.warning("Can not " + mode + " workout, workout id is invalid");
                    res.status(500);
                    model.put("code", 500);
                    String json = gson.toJson(model);
                    logger.info("json to be returned = " + json);
                    return json;
                }
            }

            datastore = dbHelper.getDataStore();
            datastore.save(workout);
            res.status(200);
            model.put("target", Path.Web.GET_PROFILE_PAGE);
            model.put("code", 200);
            model.put("status", "Added workout Successfully");
        }

        else {
            logger.warning("Can not " + mode + " workout, user id is invalid");
            res.status(500);
            model.put("code", 500);
        }

        String json = gson.toJson(model);
        logger.info("json to be returned = " + json);
        return json;
    }

    public static String handleDeleteWorkout(Request req, Response res) {
        res.type("application/json");
        String workoutId = Jsoup.parse(req.queryParams("workoutId")).text();
        String userId = req.session(false).attribute(Path.Attribute.USERID);
        logger.info("Workout id to be deleted: " + workoutId +
                "\nUser of workout " + userId);

        if(workoutId != null && !workoutId.isEmpty() && userId != null && !userId.isEmpty()) {
            Datastore datastore = dbHelper.getDataStore();
            WorkOut workout = datastore.createQuery(WorkOut.class)
                    .field("id").equal(new ObjectId(workoutId))
                    .field("userId").equal(userId)
                    .get();

            if(workout != null) {
                logger.info("DELETING WORKOUT: " + workout.getExercise());
                datastore.delete(workout);
                res.status(200);
            }

            else {
                logger.info("UNABLE TO DELETE WORKOUT b/c can not find workout in db ");
                res.status(500);
            }
        }

        else {
            logger.info("UNABLE TO DELETE WORKOUT b/c user or workout id is invalid ");
            res.status(500);
        }

        HashMap<String, Object> model = new HashMap<>();
        model.put("target", Path.Web.GET_PROFILE_PAGE );
        String json = gson.toJson(model);
        return json;

    }

    private static HashMap<String,Object> fetchWorkOuts(String userId) {

        // Grab all workouts owned by current user
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId")
                .equal(userId)
                .asList();

        for(int i = 0; i < list.size();i++) {
            WorkOut workout = list.get(i);
            workout.setRowId();
        }

        HashMap<String,Object> model = new HashMap<>();
        model.put("workout", list);
        String json = gson.toJson(model);
        logger.info("json to be returned = " + json);
        return model;
    }

}
