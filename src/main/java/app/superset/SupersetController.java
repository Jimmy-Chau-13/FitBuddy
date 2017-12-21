package app.superset;

import app.util.Path;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class SupersetController {

    Gson gson = new Gson();

    public static String handleAddSuperset(Request req, Response res) {
        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
        String userId = req.session(false).attribute(Path.Attribute.USERID);

        String superset = req.queryParams("superset");
        System.out.println(superset);
        return null;
    }
}
