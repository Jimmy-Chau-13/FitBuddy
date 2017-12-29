package app.friends;

import com.google.gson.Gson;
import org.mongodb.morphia.annotations.Embedded;

import java.util.HashSet;


@Embedded
public class Friends {

    // Json String of HashSets
    private String current_friends;
    private String pending_friends_added;
    private String pending_friends_invitation;

    public Friends() {
        Gson gson = new Gson();
        this.current_friends = gson.toJson(new HashSet<String>());
        this.pending_friends_added = gson.toJson(new HashSet<String>());
        this.pending_friends_invitation = gson.toJson(new HashSet<String>());
    }

    public String getCurrent_friends() {
        return current_friends;
    }

    public void setCurrent_friends(String current_friends) {
        this.current_friends = current_friends;
    }

    public String getPending_friends_added() {
        return pending_friends_added;
    }

    public void setPending_friends_added(String pending_friends_added) {
        this.pending_friends_added = pending_friends_added;
    }

    public String getPending_friends_invitation() {
        return pending_friends_invitation;
    }

    public void setPending_friends_invitation(String pending_friends_invitation) {
        this.pending_friends_invitation = pending_friends_invitation;
    }
}
