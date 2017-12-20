package app.graph;

import app.db.DataBaseHelper;
import app.util.Path;
import app.workout.WorkOut;
import app.workout.WorkoutComparator;
import com.google.gson.Gson;
import org.mongodb.morphia.Datastore;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

public class GraphController {

    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;
    static Gson gson = new Gson();

    // Get and graph the specific workout, returns an error if workout is not found
    public static String handleGraphWorkout(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        res.type("application/json");
        String userId = req.session(false).attribute(Path.Attribute.USERID);
        String exercise = req.queryParams("exercise");
        int num_exercises = Integer.parseInt(req.queryParams("num_exercises"));

        List<WorkOut> list = getAllExercisesToGraph(userId, exercise);

        if(list.size() == 0) {
            res.status(500);
            model.put("code", 500);
            System.out.println("ERROR");
            String json = gson.toJson(model);
            return json;
        }

        String[] workouts_dates = new String[Math.min(num_exercises, list.size())];
        int[] workouts_score = new int[workouts_dates.length];
        int i = workouts_dates.length-1;
        int j = 0;
        while(i >= 0) {
            WorkOut workout = list.get(j);
            workouts_dates[i] = workout.getDate();
            workouts_score[i] = workout.getAverage();
            i--;
            j++;
        }

        Datasets datasets = new Datasets(exercise, workouts_score);
        Graph graph = new Graph(workouts_dates, datasets);
        model.put("code", 200);
        model.put("data", graph);
        String json = gson.toJson(model);
        System.out.println(json);
        return json;

    }

    // Return all of a specific workout owned by the user id that is sorted by the most recent
    private static List<WorkOut> getAllExercisesToGraph(String userId, String exercise ) {
        datastore = dbHelper.getDataStore();
        List<WorkOut> list = datastore.createQuery(WorkOut.class)
                .field("userId").equal(userId)
                .field("exercise").equal(exercise)
                .asList();

        list.sort(new WorkoutComparator.SortByDate());
        return list;
    }
}
