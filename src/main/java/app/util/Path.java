package app.util;

public class Path {

    public static class Web {
        public static final String GET_PROFILE_PAGE = "/profile";
        public static final String GET_INDEX_PAGE = "/";
        public static final String GET_REGISTER_PAGE = "/register";

        public static final String DO_SIGNUP = "/register";
        public static final String DO_SIGNIN = "/";
        public static final String DO_WORKOUT = "/profile";
        public static final String DO_AUTH = "/auth";
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
