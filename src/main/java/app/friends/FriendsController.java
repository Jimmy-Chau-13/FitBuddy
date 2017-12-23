package app.friends;

import app.auth.AuthController;
import app.util.Path;
import spark.ModelAndView;
import spark.*;

import java.util.HashMap;

public class FriendsController {

    public static ModelAndView serveFriends(Request req, Response res) {
        String userId = AuthController.checkSessionHasUser(req);
        if(userId != null && !userId.isEmpty()) {
            HashMap<String, Object> model = new HashMap<>();
            String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
            model.put("username", username );
            return new ModelAndView(model, Path.Template.FRIENDS);
        }
        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }
}
