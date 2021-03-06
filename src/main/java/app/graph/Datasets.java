package app.graph;

// Represents the y-axis of the graph which are the average weight lifted of a workout
public class Datasets {
    private String label;
    private String borderColor;
    private int[] data;

    public Datasets(String label, int[] data) {
        this.label = label;
        this.borderColor = "rgb(255, 99, 132)";
        this.data = data;
    }
}
