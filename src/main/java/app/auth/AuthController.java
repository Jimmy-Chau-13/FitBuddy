package app.auth;

import app.util.Path;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import spark.ModelAndView;
import spark.*;

import java.util.*;
import java.util.logging.Logger;


public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    static Gson gson = new Gson();

    public static ModelAndView serveRegisterPage(Request req, Response res) {
        Map<String,Object> model = new HashMap<>();
        return new ModelAndView(model, Path.Template.REGISTER);
    }

    public static Object handleSignUp(Request req, Response res) {
        return doSignUp(req,res);
    }


    private static String doSignUp(Request req, Response res) {

        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
        String username = Jsoup.parse(req.queryParams("username")).text();
        String email = Jsoup.parse(req.queryParams("email")).text();
        String salt = Jsoup.parse(req.queryParams("salt")).text();
        String verifier = Jsoup.parse(req.queryParams("verifier")).text();

        User user = new User(username, email, salt, verifier);
/*
        logger.info("Grabbing new user: " + user.getUsername() );
        logger.info("Grabbing new email: " + user.getEmail() );
        logger.info("Grabbing new salt: " + user.getSalt() );
        logger.info("Grabbing new verifier: " + user.getVerifier() );
*/
        int success = UserController.createUser(user);

        if( success > 0) {
            logger.info(user.getUsername() + " created succesfully");
            res.status(200);
            model.put("code", 200);
            model.put("status", "Account Creation Successful! Proceed to Login");
        }

        else {
            logger.info("Unable to create user " + user.getUsername());
            res.status(401);
            model.put("code", "401");
            model.put("status", "ERROR! username/email exists already!");
        }

        String json = gson.toJson(model);
        logger.info("json to be returned = " + json);
        return json;
    }


}
