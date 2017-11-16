package app.auth;

import app.db.DataBaseHelper;
import com.mongodb.DuplicateKeyException;
import org.mongodb.morphia.Datastore;

import java.util.logging.Logger;

public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    static DataBaseHelper dbHelper = new DataBaseHelper();
    static Datastore datastore;

    public static int createUser(User user) {
        logger.info("Attempting to create user");
        if(user==null) return -1;
        datastore = dbHelper.getDataStore();
        try {
            datastore.save(user);
            logger.info("Created " + user.getUsername());
        } catch (DuplicateKeyException e) {
            logger.info(user.getUsername() + " already exists");
            return -2;
        }
        return 1;
    }
}
