package app.friends;

import app.auth.AuthController;
import app.util.Path;
import spark.ModelAndView;
import spark.*;

public class FriendsController {

    public static ModelAndView serveFriends(Request req, Response res) {
        String userId = AuthController.checkSessionHasUser(req);
        if(userId != null && !userId.isEmpty()) {
            return new ModelAndView(null, Path.Template.FRIENDS);
        }
        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }
}
