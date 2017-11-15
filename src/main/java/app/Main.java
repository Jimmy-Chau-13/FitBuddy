package app;

import app.index.IndexController;
import app.model.SimpleWorkOutDAO;

import app.model.WorkOutDAO;
import app.profile.ProfileController;
import app.util.Path;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;



public class Main {

    public static WorkOutDAO workoutDao;

    public static void main(String[] args) throws ClassNotFoundException {
        staticFileLocation("/public");
        workoutDao = new SimpleWorkOutDAO();

        get(Path.Web.INDEX, (req,res) -> IndexController.serveIndexPage(req,res)
                ,new HandlebarsTemplateEngine());

        post(Path.Web.INDEX, (req,res) -> IndexController.handleLogIn(req,res)
                ,new HandlebarsTemplateEngine());


        get("/register", (req,res) -> new ModelAndView(null,"register.hbs"), new HandlebarsTemplateEngine());

        get(Path.Web.PROFILE, (req,res) -> ProfileController.serveProfilePage(req,res)
                ,new HandlebarsTemplateEngine());

        post(Path.Web.PROFILE, (req,res) -> ProfileController.addWorkOut(req,res)
                ,new HandlebarsTemplateEngine());


    } //EOF MAIN
} // EOF CLASS
