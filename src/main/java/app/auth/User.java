package app.auth;

import app.friends.Friends;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class User {


    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username = "";
    private String salt;
    private String verifier;
    private String token = "";
    private HashSet<Friends> friends;
    private HashSet<Friends> pending_friends_Added;
    private HashSet<Friends> pending_friends_Invitation;

    public User() {

    }

    public User(String username, String salt, String verifier) {
        this.username = username;
        this.salt = salt;
        this.verifier = verifier;
        this.friends = new HashSet<>();
        this.pending_friends_Added = new HashSet<>();
        this.pending_friends_Invitation = new HashSet<>();
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

    public HashSet<Friends> getFriends() {
        return friends;
    }

    public void setFriends(HashSet<Friends> friends) {
        this.friends = friends;
    }

    public HashSet<Friends> getPending_friends_Added() {
        return pending_friends_Added;
    }

    public void setPending_friends_Added(HashSet<Friends> pending_friends_Added) {
        this.pending_friends_Added = pending_friends_Added;
    }

    public HashSet<Friends> getPending_friends_Invitation() {
        return pending_friends_Invitation;
    }

    public void setPending_friends_Invitation(HashSet<Friends> pending_friends_Invitation) {
        this.pending_friends_Invitation = pending_friends_Invitation;
    }
}
