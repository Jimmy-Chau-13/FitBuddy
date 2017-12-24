package app.auth;

import app.friends.Friends;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;


import java.util.HashSet;


public class User {


    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username = "";
    private String salt;
    private String verifier;
    private String token = "";
    private Object friends;

    public User() {

    }

    public User(String username, String salt, String verifier ) {
        this.username = username;
        this.salt = salt;
        this.verifier = verifier;
        this.friends = new Friends();
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

    public Object getFriends() {
        return friends;
    }
}
