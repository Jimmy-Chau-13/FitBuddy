package app;

import app.auth.AuthController;
import app.index.IndexController;
import app.model.SimpleWorkOutDAO;

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

        get(Path.Web.GET_INDEX_PAGE, (req,res) -> IndexController.serveIndexPage(req,res)
                ,new HandlebarsTemplateEngine());

        post(Path.Web.DO_SIGNIN, (req,res) -> AuthController.handleSignIn(req,res));

        post(Path.Web.DO_AUTH, (req,res) -> AuthController.handleAuth(req,res));

        get(Path.Web.GET_REGISTER_PAGE, (req,res) -> AuthController.serveRegisterPage(req,res)
                , new HandlebarsTemplateEngine());

        post(Path.Web.DO_SIGNUP, (req,res) -> AuthController.handleSignUp(req,res));

        get(Path.Web.GET_PROFILE_PAGE, (req,res) -> ProfileController.serveProfilePage(req,res)
                ,new HandlebarsTemplateEngine());

        post(Path.Web.DO_WORKOUT, (req,res) -> ProfileController.addWorkOut(req,res)
                ,new HandlebarsTemplateEngine());


    } //EOF MAIN
} // EOF CLASS
