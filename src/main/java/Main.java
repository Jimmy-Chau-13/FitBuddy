

import app.Profile.ProfileController;
import app.auth.AuthController;
import app.db.DataBaseHelper;
import app.friends.FriendsController;
import app.graph.GraphController;
import app.index.IndexController;

import app.superset.SupersetController;
import app.util.Filters;
import app.workout.WorkOutController;
import app.util.Path;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.logging.Logger;

import static spark.Spark.*;



public class Main {

    //private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {


        staticFileLocation("/public");
        port(getHerokuAssignedPort());

        new DataBaseHelper();

        before("/*", Filters.addTrailingSlashes);
        //ensure user is logged in to have access to protected routes
        before("/*/", (req, res) -> {
            if (req.pathInfo().equals(Path.Web.DO_AUTH)) return;

            Session session = req.session(true);
            boolean auth = session.attribute(Path.Attribute.AUTH_STATUS) != null  ?
                    session.attribute(Path.Attribute.AUTH_STATUS) : false;
            if(!auth) {
                res.redirect(Path.Web.GET_INDEX_PAGE);
                halt(401);
            }
        });

        // Serving Pages
        get(Path.Web.GET_INDEX_PAGE, (req,res) -> IndexController.serveIndexPage(req,res)
               ,new HandlebarsTemplateEngine());

        get(Path.Web.GET_PROFILE_PAGE, (req,res) -> ProfileController.serveProfile(req,res)
                ,new HandlebarsTemplateEngine());

        get(Path.Web.GET_FRIENDS_INFO, (req,res) -> FriendsController.getFriendsInfo(req,res),
                new HandlebarsTemplateEngine());

        get(Path.Web.GET_FRIENDS_PAGE, (req,res) -> FriendsController.serveFriends(req,res)
                , new HandlebarsTemplateEngine());

        get(Path.Web.GET_MONTHLY_PROGRESS, (req,res) -> ProfileController.serveInfo(req,res),
                new HandlebarsTemplateEngine());


        // Handle Authentication
        post(Path.Web.DO_SIGNIN, (req,res) -> AuthController.handleSignIn(req,res));

        post(Path.Web.DO_AUTH, (req,res) -> AuthController.handleAuth(req,res));

        post(Path.Web.DO_SIGNUP, (req,res) -> AuthController.handleSignUp(req,res));

        post(Path.Web.DO_LOGOUT, (req,res) -> AuthController.handleLogout(req,res));

        // CRUD operations for work outs
        post(Path.Web.ADD_WORKOUT, (req,res) -> WorkOutController.handleAddWorkout(req,res));

        post(Path.Web.EDIT_WORKOUT, (req,res) -> WorkOutController.handleUpdateWorkout(req,res));

        post(Path.Web.DELETE_WORKOUT, (req,res) -> WorkOutController.handleDeleteWorkout(req,res));

        post(Path.Web.VIEW_WORKOUT, (req,res) -> ProfileController.handleViewDate(req,res));

        post(Path.Web.GRAPH_WORKOUT, (req,res) -> GraphController.handleGraphWorkout(req,res));

        // CRUD operations for Supersets
        post(Path.Web.ADD_SUPERSET, (req,res) -> SupersetController.handleAddSuperset(req,res));

        post(Path.Web.DELETE_SUPERSET, (req,res) -> SupersetController.handleDeleteSuperset(req,res));

        // Friends Operations
        post(Path.Web.FRIEND_OPTION, (req,res) -> FriendsController.handleFriendOption(req,res));

    } //EOF MAIN



    public static int getHerokuAssignedPort() {
    //this will get the heroku assigned port in production

        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

} // EOF CLASS
