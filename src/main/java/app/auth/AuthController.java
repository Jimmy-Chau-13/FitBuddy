package app.auth;

import app.bitbucket.thinbus.srp6.js.SRP6JavascriptServerSessionSHA256;
import app.util.Path;
import com.google.gson.Gson;
import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6ServerSession;
import org.jsoup.Jsoup;
import spark.ModelAndView;
import spark.*;

import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;


public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    static Gson gson = new Gson();
    static SRP6JavascriptServerSessionSHA256 server;
    static SRP6CryptoParams config;

    public static ModelAndView serveRegisterPage(Request req, Response res) {
        Map<String,Object> model = new HashMap<>();
        return new ModelAndView(model, Path.Template.REGISTER);
    }

    public static Object handleSignUp(Request req, Response res) {
        return doSignUp(req,res);
    }

    public static Object handleSignIn(Request req, Response res) {
        return doSignIn(req,res);
    }


    private static String doSignUp(Request req, Response res) {

        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
        String username = Jsoup.parse(req.queryParams("username")).text();
        String email = Jsoup.parse(req.queryParams("email")).text();
        BigInteger salt = new BigInteger(Jsoup.parse(req.queryParams("salt")).text());
        BigInteger verifier = new BigInteger(Jsoup.parse(req.queryParams("verifier")).text());

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

    private static String doSignIn(Request req, Response res) {

        HashMap<String, String> model = new HashMap<>();
        res.type("application/json");

        // Grab the email from the client
        String email = Jsoup.parse(req.queryParams("email")).text();
        logger.info("email from the client = " + email);

        // Send B value and salt to client
        if(email != null && !email.isEmpty()) {
            SRP6CryptoParams config = SRP6CryptoParams.getInstance();
            SRP6ServerSession server = new SRP6ServerSession(config);
            User user = UserController.getUserByEmail(email);
            // Generate public server value 'B'
            if(user != null) {
                BigInteger salt = user.getSalt();
                BigInteger verifier = user.getVerifier();
                BigInteger B = server.step1(email, salt, verifier);
                if(B != null) {
                    model.put("salt", salt.toString());
                    model.put("B", B.toString());
                    res.status(200);
                    model.put("code", "200");
                    model.put("status", "success");
                    return gson.toJson(model);
                }
            }
        }
        res.status(401);
        model.put("status", "Invalid User Credentials");
        model.put("code", "401");
        logger.info( "Email does not exist or null B value");
        return gson.toJson(model);

    }

}
