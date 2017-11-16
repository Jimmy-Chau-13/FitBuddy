package app.auth;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import java.math.BigInteger;

public class User {

    @Id
    private ObjectId id;
    private String username = "";
    @Indexed(options = @IndexOptions(unique = true))
    private String email = "";
    private BigInteger salt;
    private BigInteger verifier;
    private String token = "";

    public User(String username, String email, BigInteger salt, BigInteger verifier) {
        this.username = username;
        this.email = email;
        this.salt = salt;
        this.verifier = verifier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getSalt() {
        return salt;
    }

    public void setSalt(BigInteger salt) {
        this.salt = salt;
    }

    public BigInteger getVerifier() {
        return verifier;
    }

    public void setVerifier(BigInteger verifier) {
        this.verifier = verifier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
