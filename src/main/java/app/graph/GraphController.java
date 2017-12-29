package app.graph;


import app.util.DateHelper;
import app.util.Path;
import app.workout.WorkOut;
import app.workout.WorkOutController;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

public class GraphController {

    static Gson gson = new Gson();

    // Get and graph the specific workout, returns an error if workout is not found
    public static String handleGraphWorkout(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();
        res.type("application/json");
        String userId = req.session(false).attribute(Path.Attribute.USERID);
        String exercise = req.queryParams("exercise");
        int num_exercises = Integer.parseInt(req.queryParams("num_exercises"));

        List<WorkOut> list = WorkOutController.getListOfAllSpecificExercise(userId, exercise);

        if(list.size() == 0) {
            res.status(500);
            String json = gson.toJson(model);
            return json;
        }

        int num_workouts_to_return = Math.min(num_exercises, list.size());

        WorkOut[] workouts = getMostRecentWorkouts(num_workouts_to_return, list);
        String[] workouts_dates = getDates(workouts);
        int[] workouts_score = getAverage(workouts);

        Datasets datasets = new Datasets(exercise, workouts_score);
        Graph graph = new Graph(workouts_dates, datasets);
        model.put("data", graph);
        model.put("list", workouts);
        String json = gson.toJson(model);
        System.out.println(json);
        return json;

    }

    private static WorkOut[] getMostRecentWorkouts(int num_exercise, List<WorkOut> list) {
        WorkOut[] workouts = new WorkOut[num_exercise];
        int list_index = list.size()-1;
        for(int i = workouts.length - 1; i >= 0; i--) {
            workouts[i] = list.get(list_index);
            list_index--;
        }
        return workouts;
    }

    private static String[] getDates(WorkOut[] workouts) {
        String[] workout_dates = new String[workouts.length];
        for (int i = 0; i < workouts.length; i++) {
            String date = DateHelper.dateToString(workouts[i].getDate());
            workout_dates[i] = date;
        }
        return workout_dates;
    }

    private static int[] getAverage(WorkOut[] workouts) {
        int[] workout_score = new int[workouts.length];
        for (int i = 0; i < workouts.length; i++) {
            int score = workouts[i].getAverage();
            workout_score[i] = score;
        }
        return workout_score;
    }


}
