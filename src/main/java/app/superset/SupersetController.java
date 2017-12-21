package app.superset;

import app.db.DataBaseHelper;
import app.util.Path;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mongodb.morphia.Datastore;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class SupersetController {
    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;
    static Gson gson = new Gson();
    static JsonParser parser = new JsonParser();


    public static String handleAddSuperset(Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
        String userId = req.session(false).attribute(Path.Attribute.USERID);

        if(userId != null && !userId.isEmpty()) {
            String jsonString = req.body();

            JsonObject obj =  parser.parse(jsonString).getAsJsonObject().getAsJsonObject("superset");
            System.out.println(obj);
            Superset superset = gson.fromJson(obj, Superset.class);
            superset.setUserId(userId);
            datastore = dbHelper.getDataStore();
            datastore.save(superset);
            res.status(200);
            model.put("code", 200);
            model.put("status", "Added workout Successfully");
            model.put("numberOfSupersets", getNumberOfSupersetsForSingleDay(superset.getDate(),userId));
            model.put("date",superset.getDate());
        }

        else {
            res.status(500);
            model.put("code", 500);
        }

        String json = gson.toJson(model);
        return json;

    }

    public static String getSupersetMonthEvent(String userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        StringBuilder eventArray = new StringBuilder();
        datastore = dbHelper.getDataStore();
        List<Superset> list = datastore.createQuery(Superset.class)
                .field("userId").equal(userId)
                .order("date")
                .asList();

        if (list.size() == 0) return null;

        int numberOfSuperset = 0;
        String prevDate = list.get(0).getDate();
        for(int i = 0; i < list.size(); i++) {
           Superset currSuperset = list.get(i);
            String currDate = currSuperset.getDate();

            if(!currDate.equals(prevDate)) {
                eventArray.append( "{ title: '" + numberOfSuperset + " supersets', " +
                        "id: '" + prevDate + "', " +
                        "start : '" + LocalDate.parse(prevDate, df)+ "' }, ");

                numberOfSuperset = 1;
                prevDate = currDate;

            }

            else {
                numberOfSuperset++;
            }
        }
        eventArray.append( "{ title: '" + numberOfSuperset + " supersets', " +
                "id: '" + prevDate + "', " +
                "color: 'green', " +
                "start : '" + LocalDate.parse(prevDate, df)+ "' }, ");
        return eventArray.toString();
    }

    // Return total number of workouts on a single day
    private static String getNumberOfSupersetsForSingleDay(String date, String userId) {
        datastore = dbHelper.getDataStore();
        List<Superset> list = datastore.createQuery(Superset.class)
                .field("userId").equal(userId)
                .field("date").equal(date)
                .asList();
        return list.size() + " supersets";
    }
}
