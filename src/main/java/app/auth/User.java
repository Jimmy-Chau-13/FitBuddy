package app.auth;

import app.friends.Friends;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import java.math.BigInteger;
import java.util.ArrayList;

public class User {


    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username = "";
    private String salt;
    private String verifier;
    private String token = "";
    private ArrayList<Friends> friends;
    private ArrayList<Friends> pending_friends_Added;
    private ArrayList<Friends> pending_friends_Invitation;

    public User() {

    }

    public User(String username, String salt, String verifier) {
        this.username = username;
        this.salt = salt;
        this.verifier = verifier;
        this.friends = new ArrayList<>();
        this.pending_friends_Added = new ArrayList<>();
        this.pending_friends_Invitation = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Friends> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Friends> friends) {
        this.friends = friends;
    }

    public ArrayList<Friends> getPending_friends_Added() {
        return pending_friends_Added;
    }

    public void setPending_friends_Added(ArrayList<Friends> pending_friends_Added) {
        this.pending_friends_Added = pending_friends_Added;
    }

    public ArrayList<Friends> getPending_friends_Invitation() {
        return pending_friends_Invitation;
    }

    public void setPending_friends_Invitation(ArrayList<Friends> pending_friends_Invitation) {
        this.pending_friends_Invitation = pending_friends_Invitation;
    }
}
