package app.friends;

import java.util.HashSet;


public class Friends {

    private HashSet<String> current_friends;
    private HashSet<String> pending_friends_added;
    private HashSet<String> pending_friends_invitation;

    public Friends() {
        this.current_friends = new HashSet<>();
        this.pending_friends_added = new HashSet<>();
        this.pending_friends_invitation = new HashSet<>();
    }

    public HashSet<String> getCurrent_friends() {
        return current_friends;
    }

    public HashSet<String> getPending_friends_added() {
        return pending_friends_added;
    }

    public HashSet<String> getPending_friends_invitation() {
        return pending_friends_invitation;
    }
}
