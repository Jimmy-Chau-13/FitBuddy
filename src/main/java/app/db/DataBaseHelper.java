package app.db;

import app.auth.User;
import app.workout.WorkOut;
import app.util.Path;
import com.mongodb.MongoClient;
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
        MongoClient mongoClient = new MongoClient(Path.Database.HOST, Path.Database.PORT);
        datastore = morphia.createDatastore(mongoClient, Path.Database.LOCAL_DBNAME);
        datastore.ensureIndexes();
        logger.info("Database connection successful and Datastore initiated");
    }

    public Datastore getDataStore() {
        if(datastore == null)
            initDatastore();
        return datastore;
    }


}
