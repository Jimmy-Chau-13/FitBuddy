package app.model;

import java.util.List;

// Interface for WorkOut access in place of a database for now
public interface WorkOutDAO {

    boolean add (WorkOut workout);
    List<WorkOut> findAll();
}
