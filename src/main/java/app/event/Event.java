package app.event;

// jQuery Full Calendar Event
public class Event {

    private String title;
    private String id;
    private String start;

    public Event(String title, String id, String start) {
        this.title = title;
        this.id = id;
        this.start = start;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
