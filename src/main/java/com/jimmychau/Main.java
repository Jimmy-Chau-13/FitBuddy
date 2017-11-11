package com.jimmychau;

import com.jimmychau.model.SimpleWorkOutDAO;
import com.jimmychau.model.WorkOut;
import com.jimmychau.model.WorkOutDAO;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;


import java.util.HashMap;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

public class Main {
 /*
    // Create a hibernate building session
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    } */

    public static void main(String[] args) throws ClassNotFoundException {
        staticFileLocation("/public");
        WorkOutDAO dao = new SimpleWorkOutDAO();

        get("/", (req,res)->
            new ModelAndView(null,"index.hbs")
        , new HandlebarsTemplateEngine());

        post("/", (req,res) -> {
            Map<String ,String> model = new HashMap<>();
            String username = req.queryParams("email");
            model.put("username", username);
            return new ModelAndView(model,"profile.hbs");
        }, new HandlebarsTemplateEngine());

        get("/register", (req,res) ->
            new ModelAndView(null,"register.hbs")
        , new HandlebarsTemplateEngine());

        get("/profile", (req,res) -> {
            Map<String,Object> model = new HashMap<>();
            model.put("workout", dao.findAll());
            return new ModelAndView(model,"profile.hbs");
        }, new HandlebarsTemplateEngine());

        post("/profile", (req,res) -> {
            String exercise = req.queryParams("exercise");
            int sets = Integer.parseInt(req.queryParams("sets"));
            int reps = Integer.parseInt(req.queryParams("reps"));
            int weight = Integer.parseInt(req.queryParams("weight"));
            WorkOut workout = new WorkOut(exercise,sets,reps,weight);
            dao.add(workout);
            res.redirect("/profile");
            return null;
        });

    } //EOF MAIN
} // EOF CLASS
