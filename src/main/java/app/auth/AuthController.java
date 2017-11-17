package app.auth;


import app.bitbucket.thinbus.srp6.js.SRP6JavascriptServerSessionSHA256;
import app.util.Path;
import com.google.gson.Gson;
import com.nimbusds.srp6.SRP6CryptoParams;
import org.jsoup.Jsoup;
import spark.ModelAndView;
import spark.*;

import java.util.*;
import java.util.logging.Logger;


public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());
    static Gson gson = new Gson();
    static SRP6CryptoParams config;
    static SRP6JavascriptServerSessionSHA256 server;

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

    public static Object handleAuth(Request req, Response res) {
        return doAuth(req,res);
    }


    private static String doSignUp(Request req, Response res) {

        res.type("application/json");
        HashMap<String, Object> model = new HashMap<>();
        String username = Jsoup.parse(req.queryParams("username")).text();
        String email = Jsoup.parse(req.queryParams("email")).text();
        String salt = Jsoup.parse(req.queryParams("salt")).text();
        String verifier = Jsoup.parse(req.queryParams("verifier")).text();


        User user = new User(username, email, salt, verifier);

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
            server = new SRP6JavascriptServerSessionSHA256(CryptoParams.N_base10, CryptoParams.g_base10);
            User user = UserController.getUserByEmail(email);
            // Generate public server value 'B'
            if(user != null) {

                String salt = user.getSalt();
                String verifier = user.getVerifier();
                logger.info("Salt = " + salt + "\nVerifier = " + verifier);
                String B = server.step1(email, salt, verifier);

                logger.info("server  challenge B = " + B);
                if(B != null) {
                    model.put("salt", user.getSalt());
                    model.put("B", B);
                    res.status(200);
                    model.put("code", "200");
                    model.put("status", "success");
                    logger.info("JSON RESP SENT TO CLIENT = " + model.toString());
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

    private static String doAuth(Request req, Response res) {
        res.type("application/json");
        HashMap<String, String> model = new HashMap<>();
        String A = Jsoup.parse(req.queryParams("A")).text();
        String M1 = Jsoup.parse(req.queryParams("M1")).text();
        logger.info("Client Credentials Sent to Authenticate = \n: M1 = " + M1 + " \nA = " + A);
        String email = Jsoup.parse(req.queryParams("email")).text();
        String M2 = null;

        try {
            M2 = server.step2(A,M1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(M2 != null) {
            String m2 = M2.toString();
            logger.info("M2 Generated in Authenticate = " + m2);
            Session session = req.session(true);
            User user = UserController.getUserByEmail(email);

            session.attribute(Path.Attribute.USERNAME, user.getUsername());
            session.attribute(Path.Attribute.USERID, user.getId().toString()); //saves the id as String
            session.attribute(Path.Attribute.AUTH_STATUS, true);
            session.attribute(Path.Attribute.EMAIL, user.getEmail());

            model.put("M2", M2);
            model.put("code", "200");
            model.put("status", "success");
            model.put("target", Path.Web.GET_PROFILE_PAGE);

            String respjson = gson.toJson(model);
            logger.info("Final response sent By doAuth to client = " + respjson);
            res.status(200);
            return respjson;
        }


        res.status(401);
        model.put("code", "401");
        model.put("status", "Error! Invalid Login Credentials");
        return gson.toJson(model);



    }

}
