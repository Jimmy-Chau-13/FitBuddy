package app.index;

import app.util.Path;
import spark.*;
import java.util.*;

public class IndexController {

    // Index page contains a log in and a link to sign up
    public static ModelAndView serveIndexPage(Request req, Response res) {
        return new ModelAndView(null, Path.Template.INDEX);
    }

}


