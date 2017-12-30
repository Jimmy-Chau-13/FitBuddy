package app.friends;

import app.auth.AuthController;
import app.auth.User;
import app.auth.UserController;
import app.db.DataBaseHelper;
import app.util.DateHelper;
import app.util.Path;
import app.workout.WorkOutController;
import com.google.gson.Gson;
import org.mongodb.morphia.Datastore;

import spark.ModelAndView;
import spark.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

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
            model.put("friends", me.getFriends().getCurrent_friends());
            model.put("added_me", me.getFriends().getPending_friends_invitation());
            model.put("pending", me.getFriends().getPending_friends_added());
            model.put("username", username );
            //System.out.println("GET FRIENDS PAGE RESPONSE: " + gson.toJson(model));
            return new ModelAndView(model, Path.Template.FRIENDS);
        }
        res.redirect(Path.Web.GET_INDEX_PAGE);
        return null;
    }

    public static  ModelAndView getFriendsInfo(Request req, Response res) {
        String userId = AuthController.checkSessionHasUser(req);
        String date_string = req.queryParams("date");
        Date date;
        if (date_string == null || date_string.isEmpty()){
            date = new Date();
        } else {
            date = DateHelper.stringToDate(date_string);
        }

        HashMap<String, Object> model = new HashMap<>();
        String friends_username = req.params(":username");
        System.out.println("ITS ME UR FIREND: " + friends_username);

        String your_username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
        model.put("username", your_username);
        model.put("friends_username", friends_username);
        model.put("date", DateHelper.dateToMonthYear(date));
        int num_my_workouts = WorkOutController.getListOfAllWorkoutsOfThisMonth(userId, date).size();
        int num_friends_workouts = WorkOutController.getListOfAllWorkoutsOfThisMonth(
                UserController.getUserByUsername(friends_username).getId().toString(), date).size();
        model.put("num_my_workouts", num_my_workouts);
        model.put("num_friends_workouts", num_friends_workouts);
        return new ModelAndView(model, "friendsInfo.hbs");
    }

    public static String handleFriendOption(Request req, Response res) {
        res.type("application/json");
        String userId = AuthController.checkSessionHasUser(req);
        if(userId == null || userId.isEmpty()) {
            res.redirect(Path.Web.GET_INDEX_PAGE);
            return null;
        }

        HashMap<String,Object> model = new HashMap<>();
        String friend_username = req.queryParams("friend_username");
        String option = req.queryParams("option");
        model.put("friend_username", friend_username);
        String username = req.session(false).attribute(Path.Attribute.USERNAME).toString();
        User friend = UserController.getUserByUsername(friend_username);
        User me = UserController.getUserByUsername(username);
        if(option.equals("add_friend")) {
            return handleAddFriend(me,friend,friend_username,username,model,res);
        }

        else if(option.equals("remove_friend")) {
            return handleRemoveFriend(me,friend,friend_username,username,model,res);
        }

        else if(option.equals("confirm_friend")) {
            return handleConfirmFriend(me,friend,friend_username,username,model,res);
        }

        else if(option.equals("decline_friend")) {
            return handleDeclineFriend(me,friend,friend_username,username,model,res);
        }

        else {
            res.status(500);
            return gson.toJson(model);
        }
    }


    public static String handleAddFriend(User me, User friend, String friend_username, String username
        , HashMap<String, Object> model, Response res) {

        if(addFriendHasError(model,me,friend)) {
            res.status(500);
            return gson.toJson(model);
        }

        datastore = dbHelper.getDataStore();
        me.setFriends(addToMyAdds(me, friend_username));
        datastore.save(me);

        friend.setFriends(addToMyInvitations(friend, username));
        datastore.save(friend);

        res.status(200);
        //System.out.print("RESPONSE: " + gson.toJson(model));
        return gson.toJson(model);
    }

    public static String handleRemoveFriend(User me, User friend, String friend_username, String username
            , HashMap<String, Object> model, Response res) {

        datastore = dbHelper.getDataStore();
        me.setFriends(removeFromMyFriendsList(me, friend_username));
        datastore.save(me);

        friend.setFriends(removeFromMyFriendsList(friend, username));
        datastore.save(friend);

        res.status(200);
        //System.out.print("RESPONSE: " + gson.toJson(model));
        return gson.toJson(model);
    }

    public static String handleConfirmFriend(User me, User friend, String friend_username, String username
            , HashMap<String, Object> model, Response res) {

        me.setFriends(deleteFromMyInvitations(me,friend_username));
        friend.setFriends(deleteFromMyAdds(friend,username));

        me.setFriends(addToMyFriendsList(me, friend_username));
        friend.setFriends(addToMyFriendsList(friend, username));

        datastore = dbHelper.getDataStore();
        datastore.save(me);
        datastore.save(friend);

        res.status(200);
        //System.out.print("RESPONSE: " + gson.toJson(model));
        return gson.toJson(model);
    }

    public static String handleDeclineFriend(User me, User friend, String friend_username, String username
            , HashMap<String, Object> model, Response res) {

        me.setFriends(deleteFromMyInvitations(me,friend_username));
        friend.setFriends(deleteFromMyAdds(friend,username));

        datastore = dbHelper.getDataStore();
        datastore.save(me);
        datastore.save(friend);

        res.status(200);
        //System.out.print("RESPONSE: " + gson.toJson(model));
        return gson.toJson(model);
    }



    private static boolean addFriendHasError(HashMap<String,Object> model, User me, User friend) {
        // Check if username exists
        if(friend == null) {
            model.put("error_message", "Username does not exist!");
            return true;
        }
        // Can not add yourself as a friend
        if(me.getId().equals(friend.getId())) {
            model.put("error_message", "Can not add yourself");
            return  true;
        }
        // Check if the username is already a friend
        if( gson.fromJson(me.getFriends().getCurrent_friends(), HashSet.class).contains(friend)) {
            model.put("error_message", "Username is already a friend!");
            return true;
        }
        // Check if username is on pending list, i.e. they added you or you added them
        if(gson.fromJson(me.getFriends().getPending_friends_added(),HashSet.class).contains(friend) ||
                gson.fromJson(me.getFriends().getPending_friends_invitation(), HashSet.class).contains(friend)) {
            model.put("error_message", "This user is on your pending list!");
            return true;
        }
        return false;
    }

    // Add a username to this User list of friends waiting to confirm as friend
    private static Friends addToMyAdds(User user, String username_to_add) {
        Friends my_friends = user.getFriends();
        HashSet<String> my_adds = gson.fromJson(my_friends.getPending_friends_added(), HashSet.class);
        my_adds.add(username_to_add);
        String jsonString = gson.toJson(my_adds);
        my_friends.setPending_friends_added(jsonString);
        return my_friends;
    }

    // Add a username to this User list of friends waiting to confirm as friend
    private static Friends deleteFromMyAdds(User user, String username_to_delete) {
        Friends my_friends = user.getFriends();
        HashSet<String> my_adds = gson.fromJson(my_friends.getPending_friends_added(), HashSet.class);
        my_adds.remove(username_to_delete);
        String jsonString = gson.toJson(my_adds);
        my_friends.setPending_friends_added(jsonString);
        return my_friends;
    }

    // Add the username to this User pending invitation
    private static Friends addToMyInvitations(User user, String username_invitation) {
        Friends my_friends = user.getFriends();
        HashSet<String> my_invitations = gson.fromJson(my_friends.getPending_friends_invitation(), HashSet.class);
        my_invitations.add(username_invitation);
        String jsonString = gson.toJson(my_invitations);
        my_friends.setPending_friends_invitation(jsonString);
        return my_friends;
    }

    // Delete the username to this User pending invitation
    private static Friends deleteFromMyInvitations(User user, String username_invitation) {
        Friends my_friends = user.getFriends();
        HashSet<String> my_invitations = gson.fromJson(my_friends.getPending_friends_invitation(), HashSet.class);
        my_invitations.remove(username_invitation);
        String jsonString = gson.toJson(my_invitations);
        my_friends.setPending_friends_invitation(jsonString);
        return my_friends;
    }

    // Add the username to this User friends list
    private static Friends addToMyFriendsList(User user, String username) {
        Friends my_friends = user.getFriends();
        HashSet<String> my_friends_list = gson.fromJson(my_friends.getCurrent_friends(), HashSet.class);
        my_friends_list.add(username);
        String jsonString = gson.toJson(my_friends_list);
        my_friends.setCurrent_friends(jsonString);
        return my_friends;
    }

    // Remove the username to this User friends list
    private static Friends removeFromMyFriendsList(User user, String username) {
        Friends my_friends = user.getFriends();
        HashSet<String> my_friends_list = gson.fromJson(my_friends.getCurrent_friends(), HashSet.class);
        my_friends_list.remove(username);
        String jsonString = gson.toJson(my_friends_list);
        my_friends.setCurrent_friends(jsonString);
        return my_friends;
    }
}
