package app.util;

public class Path {

    public static class Web {
        public static final String GET_PROFILE_PAGE = "/profile";
        public static final String GET_INDEX_PAGE = "/";
        public static final String GET_REGISTER_PAGE = "/register";

        public static final String DO_SIGNUP = "/register";
        public static final String DO_SIGNIN = "/";
        public static final String DO_AUTH = "/auth";

        public static final String ADD_WORKOUT = "/add";
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
    }

    public static class Database {
        public static final String LOCAL_DBNAME = "workout_db";
        public static final String HOST = "127.0.0.1";
        public static final int PORT = 27017;
    }
}
