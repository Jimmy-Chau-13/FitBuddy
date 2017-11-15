package app.index;

import app.util.Path;
import spark.*;
import java.util.*;

public class IndexController {

    public static ModelAndView serveIndexPage(Request req, Response res) {
        return new ModelAndView(null, Path.Template.INDEX);
    }

    public static ModelAndView handleLogIn(Request req, Response res) {
        Map<String, String> model = new HashMap<>();
        String username = req.queryParams("email");
        model.put("username", username);
        return new ModelAndView(model, Path.Template.PROFILE);
    }
}


