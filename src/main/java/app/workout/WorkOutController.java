package app.workout;

import app.auth.AuthController;
import app.db.DataBaseHelper;
import app.graph.Datasets;
import app.graph.Graph;
import app.superset.SupersetController;
import app.util.Path;
import app.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.mongodb.morphia.Datastore;
import spark.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class WorkOutController {

    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;
    static Logger logger = Logger.getLogger("WorkOutController.class");
    static Gson gson = new Gson();


    public WorkOutController() { }

    public static String handleAddWorkout (Request req, Response res) {
        res.type("application/json");

        HashMap<String, Object> model = new HashMap<>();

        String userId = req.session(false).attribute(Path.Attribute.USERID);
        String exercise = Jsoup.parse(req.queryParams("exercise")).text();
        int sets = Integer.parseInt(Jsoup.parse(req.queryParams("sets")).text());
        String date = req.queryParams("date");
        int[] reps = StringHelper.stringToArray(Jsoup.parse(req.queryParams("reps")).text(),sets);
        int[] weight = StringHelper.stringToArray(Jsoup.parse(req.queryParams("weight")).text(),sets);

        WorkOut workout = new WorkOut(exercise,sets,reps,weight,date);

        logger.info( "Add workout: " + workout.getExercise() +
                        "\nSets " + workout.getSets() +
                        "\nReps " + StringHelper.printArray(workout.getReps()) +
                        "\nWeight " + StringHelper.printArray(workout.getWeight()) +
                        "\nDate " + workout.getDate());

        if(userId != null && !userId.isEmpty()) {
            workout.setUserId(userId);
            logger.info("Where user id is " + userId);

            datastore = dbHelper.getDataStore();
            datastore.save(workout);
            res.status(200);
            model.put("numberOfWorkouts", getNumberOfWorkout(date,userId));
            model.put("date",date);
            model.put("code", 200);
            model.put("status", "Added workout Successfully");
        }

        else {
            logger.warning("Can not add workout, user id is invalid");
            res.status(500);
            model.put("code", 500);
        }

        String json = gson.toJson(model);
        logger.info("json to be returned = " + json);
        return json;
    }

    public static String handleUpdateWorkout (Request req, Response res) {
        res.type("application/json");

        HashMap<String, Object> model = new HashMap<>();

        String userId = req.session(false).attribute(Path.Attribute.USERID);
        String exercise = Jsoup.parse(req.queryParams("exercise")).text();
        int sets = Integer.parseInt(Jsoup.parse(req.queryParams("sets")).text());
        String date = req.queryParams("date");
        int[] reps = StringHelper.stringToArray(Jsoup.parse(req.queryParams("reps")).text(),sets);
        int[] weight = StringHelper.stringToArray(Jsoup.parse(req.queryParams("weight")).text(),sets);

        WorkOut workout = new WorkOut(exercise,sets,reps,weight,date);

        logger.info("Edit workout: " + workout.getExercise() +
                "\nSets " + workout.getSets() +
                "\nReps " + StringHelper.printArray(workout.getReps()) +
                "\nWeight " + StringHelper.printArray(workout.getWeight()) +
                "\nDate " + workout.getDate());

        if(userId != null && !userId.isEmpty()) {
            workout.setUserId(userId);
            logger.info("Where user id is " + userId);
            String workoutId = Jsoup.parse(req.queryParams("editId")).text();
            logger.info("Editing workout id " + workoutId);
            if(workoutId != null && !workoutId.isEmpty()) {
                workout.setId(workoutId);

            }
            else {
                logger.warning("Can not edit workout, workout id is invalid");
                res.status(500);
                model.put("code", 500);
                String json = gson.toJson(model);
                logger.info("json to be returned = " + json);
                return json;
            }

            datastore = dbHelper.getDataStore();
            WorkOut old_workout = datastore.get(WorkOut.class, new ObjectId(workoutId));
            datastore.save(workout);
            if(old_workout.getDate() != date) {
                model.put("old_date", old_workout.getDate());
                model.put("num_workouts_new", getNumberOfWorkout(date,userId));
                model.put("num_workouts_old", getNumberOfWorkout(old_workout.getDate(),userId));
            }
            res.status(200);
            model.put("date",date);
            model.put("code", 200);
            model.put("status", "Edited workout Successfully");
        }

        else {
            logger.warning("Can not workout, user id is invalid");
            res.status(500);
            model.put("code", 500);
        }

        String json = gson.toJson(model);
        logger.info("json to be returned = " + json);
        return json;
    }

    public static String handleDeleteWorkout(Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
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
                String date = workout.getDate();
                logger.info("DELETING WORKOUT: " + workout.getExercise());
                datastore.delete(workout);
                res.status(200);
                model.put("numberOfWorkouts", getNumberOfWorkout(date,userId));
                model.put("date", date);
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

    // Fetch all workouts of a day to show on view modal
    public static HashMap<String,Object> fetchWorkOuts(String userId, String date, HashMap<String,Object> model ) {

        // Grab all workouts owned by current user
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .field("date").equal(date)
                .asList();

        StringBuilder jsonData = new StringBuilder();
        if(list != null && list.size() > 0) {
            logger.info("found " + list.size() + " workout ");
            for(int i = 0; i < list.size(); i++) {
                WorkOut workout = list.get(i);
                jsonData.append(workout.toJson()).append(",");
                         logger.info("jsonData " + i + " = " + workout.toJson());
            }
            jsonData.deleteCharAt(jsonData.lastIndexOf(",")); //removes the last comma
            jsonData.insert(0, "[" ).append("]"); //name the array


        } else {
            logger.warning("No user data found for userId " + userId);
            jsonData.append("{}");
        }

        logger.info(jsonData.toString());
        model.put("jsonData", jsonData.toString() );

        return model;
    }


    public static String getWorkoutMonthEvent(String userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        StringBuilder eventArray = new StringBuilder();
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .order("date")
                .asList();

        if (list.size() == 0) return null;

        int numberOfWorkout = 0;
        String prevDate = list.get(0).getDate();
        for(int i = 0; i < list.size(); i++) {
            WorkOut currWorkout = list.get(i);
            String currDate = currWorkout.getDate();

            if(!currDate.equals(prevDate)) {
                eventArray.append( "{ title: '" + numberOfWorkout + " workouts', " +
                        "id: '" + prevDate + " workout', " +
                        "start : '" + LocalDate.parse(prevDate, df)+ "' }, ");

                numberOfWorkout = 1;
                prevDate = currDate;

            }

            else {
                numberOfWorkout++;
            }
        }
        eventArray.append( "{ title: '" + numberOfWorkout + " workouts', " +
                "id: '" + prevDate + " workout', " +
                "start : '" + LocalDate.parse(prevDate, df)+ "' }, ");
        return eventArray.toString();
    }

    // Return total number of workouts on a single day
    private static String getNumberOfWorkout(String date, String userId) {
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .field("date").equal(date)
                .asList();
        System.out.println("WORKOUTS: " + list.size());
        return list.size() + " workouts";
    }


}
