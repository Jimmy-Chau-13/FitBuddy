package app.workout;


import app.db.DataBaseHelper;
import app.util.DateHelper;
import app.util.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import spark.*;


import java.util.*;
import java.util.logging.Logger;


public class WorkOutController {

    private static DataBaseHelper dbHelper = new DataBaseHelper();
    private static Datastore datastore;
    private static Logger logger = Logger.getLogger("WorkOutController.class");
    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();


    public WorkOutController() { }

    public static String handleAddWorkout (Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();

        String jsonString = req.body();
        System.out.println(jsonString);
        JsonObject obj =  parser.parse(jsonString).getAsJsonObject();

        WorkOut workout = getWorkoutFromJsonObject(obj);
        Date date = getDateFromJsonObject(obj);

        workout.setDate(date);
        String userId = req.session(false).attribute(Path.Attribute.USERID);
        workout.setUserId(userId);

        datastore = dbHelper.getDataStore();
        datastore.save(workout);

        res.status(200);
        model.put("numberOfWorkouts", getNumberOfWorkoutOnASingleDay(date,userId));
        model.put("date", DateHelper.dateToEventDateString(date));
        String json = gson.toJson(model);
        return json;
    }

    public static String handleUpdateWorkout (Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();

        String jsonString = req.body();
        JsonObject obj =  parser.parse(jsonString).getAsJsonObject();

        WorkOut workout = getWorkoutFromJsonObject(obj);
        Date date = getDateFromJsonObject(obj);
        workout.setDate(date);

        String userId = req.session(false).attribute(Path.Attribute.USERID);
        workout.setUserId(userId);

        String workoutId = obj.getAsJsonPrimitive("editId").getAsString();

        if(workoutId != null && !workoutId.isEmpty()) {
            workout.setId(workoutId);
        } else {
            logger.warning("Can not edit workout, workout id is invalid");
            res.status(500);
            String json = gson.toJson(model);
            logger.info("json to be returned = " + json);
            return json;
        }

        datastore = dbHelper.getDataStore();
        WorkOut old_workout = datastore.get(WorkOut.class, new ObjectId(workoutId));
        datastore.save(workout);
        if(!old_workout.getDate().equals(date)) {
            model.put("old_date", DateHelper.dateToEventDateString(old_workout.getDate()));
            model.put("num_workouts_new", getNumberOfWorkoutOnASingleDay(date,userId));
            model.put("num_workouts_old", getNumberOfWorkoutOnASingleDay(old_workout.getDate(),userId));
        }

        res.status(200);
        model.put("date",DateHelper.dateToEventDateString(date));
        String json = gson.toJson(model);
        logger.info("json to be returned = " + json);
        return json;
    }

    public static String handleDeleteWorkout(Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
        String workoutId = req.queryParams("workoutId");
        System.out.println(workoutId);
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
                Date date = workout.getDate();
                logger.info("DELETING WORKOUT: " + workout.getExercise());
                datastore.delete(workout);
                res.status(200);
                model.put("numberOfWorkouts", getNumberOfWorkoutOnASingleDay(date,userId));
                model.put("date", DateHelper.dateToEventDateString(date));
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

        String json = gson.toJson(model);
        return json;

    }

    // Return a list of all workouts on a day to put on the view Modal
    public static List<WorkOut> getListOfWorkoutsOnADay(String userId, Date date) {
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .field("date").equal(date)
                .asList();
        return list;
    }


    public static List<WorkOut> getListOfAllWorkouts(String userId) {
        datastore = dbHelper.getDataStore();
        return datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .order("date")
                .asList();

    }

    public static List<WorkOut> getListOfAllSpecificExercise(String userId, String exercise) {
        datastore = dbHelper.getDataStore();
        return datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .field("exercise").equal(exercise)
                .order("date")
                .asList();

    }

    public static int getNumberOfWorkoutsForAMonth(String userId, String month, String year) {
        return 0;
    }

    public static String[] getArrayOfWorkoutIdsFromList(List<WorkOut> workout_list) {
        String[] ids = new String[workout_list.size()];
        for (int i = 0; i < workout_list.size(); i++) {
            ids[i] = workout_list.get(i).getId().toString();
        }
        return ids;
    }

    // Return total number of workouts on a single day
    private static String getNumberOfWorkoutOnASingleDay(Date date, String userId) {
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .field("date").equal(date)
                .asList();
        if(list.size() == 1) return list.size() + " workout";
        else return list.size() + " workouts";
    }


    private static WorkOut getWorkoutFromJsonObject(JsonObject obj) {
        JsonObject workout_obj = obj.getAsJsonObject("workout");
        WorkOut workout = gson.fromJson(workout_obj, WorkOut.class);
        return workout;
    }

    private static Date getDateFromJsonObject(JsonObject obj) {
        JsonPrimitive date_obj = obj.getAsJsonPrimitive("date");
        Date date = DateHelper.jsonToDate(date_obj);
        return date;
    }



}
