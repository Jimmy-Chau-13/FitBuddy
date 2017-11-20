package app.workout;

import java.util.ArrayList;
import java.util.List;

// Using data structures in place of databases for now
public class SimpleWorkOutDAO implements WorkOutDAO {
    private List<WorkOut> workOuts;

    public SimpleWorkOutDAO() {
        workOuts = new ArrayList<>();
    }

    @Override
    public boolean add(WorkOut workout) {
        return workOuts.add(workout);
    }

    @Override
    public List<WorkOut> findAll() {
        return new ArrayList<>(workOuts);
    }
}
