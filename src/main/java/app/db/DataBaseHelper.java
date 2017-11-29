package app.db;

import app.auth.User;
import app.workout.WorkOut;
import app.util.Path;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.logging.Logger;

public class DataBaseHelper {

    private static Morphia morphia = new Morphia();
    private static Datastore datastore = null;
    private static Logger logger = Logger.getLogger("DataBaseHelper.class");

    public DataBaseHelper() {
        if(!morphia.isMapped(WorkOut.class)) {
            morphia.map(WorkOut.class);
            morphia.map(User.class);
            initDatastore();
        } else {
            logger.info("Database Class Mapped Already!");
        }
    }

    void initDatastore() {

        ProcessBuilder processBuilder = new ProcessBuilder();
        MongoClient mongoClient;

        //this will fetch the MONGODB_URI environment variable on heroku
        //that holds the connection string to our database created by the heroku mLab add on
        String HEROKU_MLAB_URI = processBuilder.environment().get("MONGODB_URI");

        if (HEROKU_MLAB_URI != null && !HEROKU_MLAB_URI.isEmpty()) {
            //heroku environ
            logger.warning("Remote MLAB Database Detected");
            mongoClient = new MongoClient(new MongoClientURI(HEROKU_MLAB_URI));
            datastore = morphia.createDatastore(mongoClient, Path.Database.HEROKU_DB_NAME);
        } else {
//                //local environ
            logger.info("Local Database Detected");
            mongoClient = new MongoClient(Path.Database.HOST, Path.Database.PORT);
            datastore = morphia.createDatastore(mongoClient, Path.Database.LOCAL_DBNAME);
        }

        datastore.ensureIndexes();
        logger.info("Database connection successful and Datastore initiated");
    }

    public Datastore getDataStore() {
        if(datastore == null)
            initDatastore();
        return datastore;
    }


}
