package app.util;

public class Path {

    public static class Web {
        public static final String GET_PROFILE_PAGE = "/profile/";
        public static final String GET_INDEX_PAGE = "/";
        public static final String GET_FRIENDS_PAGE = "/friends/";
        public static final String GET_MONTHLY_PROGRESS = "/monthly_progress/";
        public static final String GET_FRIENDS_INFO = "/friends/:username/";

        public static final String DO_SIGNUP = "/register";
        public static final String DO_SIGNIN = "/";
        public static final String DO_AUTH = "/auth";
        public static final String DO_LOGOUT = "/logout";

        public static final String ADD_WORKOUT = "/add";
        public static final String EDIT_WORKOUT = "/edit";
        public static final String DELETE_WORKOUT = "/delete";
        public static final String VIEW_WORKOUT = "/view";

        public static final String ADD_SUPERSET = "/add_superset";
        public static final String DELETE_SUPERSET = "/delete_superset";

        public static final String FRIEND_OPTION = "/friend_option";

        public static final String GRAPH_WORKOUT = "/graph";
    }

    public static class Attribute {
        public static final String USERNAME = "username";
        public static final String USERID = "userId";
        public static final String EMAIL = "email";
        public static final String AUTH_STATUS = "AUTH_STATUS";
    }

    public static class Template {
        public static final String PROFILE = "profile.hbs";
        public static final String INDEX = "index.hbs";
        public static final String REGISTER = "register.hbs";
        public static final String FRIENDS = "friends.hbs";
    }

    public static class Database {
        public static final String LOCAL_DBNAME = "workout_db";
        public static final String HOST = "127.0.0.1";
        public static final int PORT = 27017;


        public static String HEROKU_DB_URI = "mongodb://heroku_6tnh94hh:khe2h2oilo3c9qdcejh8tlhk7b@ds125896.mlab.com:25896/heroku_6tnh94hh";
        public static String HEROKU_DB_NAME = "heroku_6tnh94hh"; //this is the last part of the HEROKU_DB_URI
    }
}
