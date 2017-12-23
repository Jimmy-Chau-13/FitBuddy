package app.friends;

import app.auth.AuthController;
import app.auth.User;
import app.auth.UserController;
import app.db.DataBaseHelper;
import app.util.Path;
import com.google.gson.Gson;
import org.mongodb.morphia.Datastore;
import spark.ModelAndView;
import spark.*;

import java.util.HashMap;

public class FriendsController {
    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;
    static Gson gson = new Gson();

    public static ModelAndView serveFriends(Request req, Response res) {
        String userId = AuthController.checkSessionHasUser(req);
        if(userId != null && !userId.isEmpty()) {
            HashMap<String, Object> model = new HashMap<>();
            String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
            User me = UserController.getUserByUsername(username);
            model.put("friends", me.getFriends());
            model.put("added_me", me.getPending_friends_Invitation());
            model.put("pending", me.getPending_friends_Added());
            model.put("username", username );
            return new ModelAndView(model, Path.Template.FRIENDS);
        }
        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }

    public static String handleAddFriend(Request req, Response res) {
        String userId = AuthController.checkSessionHasUser(req);
        if(userId == null || userId.isEmpty()) {
            res.redirect(Path.Web.GET_INDEX_PAGE);
            return null;
        }

        HashMap<String,Object> model = new HashMap<>();
        String friend_username = req.queryParams("friend_username");
        model.put("friend_username", friend_username);
        String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
        User friend = UserController.getUserByUsername(friend_username);
        User me = UserController.getUserByUsername(username);

        if(addFriendHasError(model,me,friend)) {
            res.status(500);
            return gson.toJson(model);
        }

        me.getPending_friends_Added().add(new Friends(friend_username));
        friend.getPending_friends_Invitation().add(new Friends(username));

        datastore = dbHelper.getDataStore();
        datastore.save(me);
        datastore.save(friend);

        res.status(500);
        return gson.toJson(model);
    }

    private static boolean addFriendHasError(HashMap<String,Object> model, User me, User friend) {
        // Check if username exists
        if(friend == null) {
            model.put("error_message", "Username does not exist!");
            return true;
        }
        // Check if the username is already a friend
        if(me.getFriends().contains(friend)) {
            model.put("error_message", "Username is already a friend!");
            return true;
        }
        // Check if username is on pending list, i.e. they added you or you added them
        if(me.getPending_friends_Added().contains(friend) || me.getPending_friends_Invitation().contains(friend)) {
            model.put("error_message", "This user is on your pending list!");
            return true;
        }
        return false;
    }
}
