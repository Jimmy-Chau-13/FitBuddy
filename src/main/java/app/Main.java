package app;

import app.auth.AuthController;
import app.index.IndexController;
import app.model.SimpleWorkOutDAO;

import app.model.WorkOutController;
import app.model.WorkOutDAO;
import app.profile.ProfileController;
import app.util.Path;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.logging.Logger;

import static spark.Spark.*;



public class Main {

    public static WorkOutDAO workoutDao;
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws ClassNotFoundException {
        staticFileLocation("/public");
        workoutDao = new SimpleWorkOutDAO();

        // Serving Pages
        get(Path.Web.GET_INDEX_PAGE, (req,res) -> IndexController.serveIndexPage(req,res)
                ,new HandlebarsTemplateEngine());

        get(Path.Web.GET_REGISTER_PAGE, (req,res) -> AuthController.serveRegisterPage(req,res)
                , new HandlebarsTemplateEngine());

        get(Path.Web.GET_PROFILE_PAGE, (req,res) -> WorkOutController.serveProfile(req,res)
                ,new HandlebarsTemplateEngine());


        // Handle Authentication
        post(Path.Web.DO_SIGNIN, (req,res) -> AuthController.handleSignIn(req,res));

        post(Path.Web.DO_AUTH, (req,res) -> AuthController.handleAuth(req,res));

        post(Path.Web.DO_SIGNUP, (req,res) -> AuthController.handleSignUp(req,res));

        // CRUD operations for work outs
        post(Path.Web.ADD_WORKOUT, (req,res) -> WorkOutController.handleNewWorkout(req,res));


    } //EOF MAIN
} // EOF CLASS
