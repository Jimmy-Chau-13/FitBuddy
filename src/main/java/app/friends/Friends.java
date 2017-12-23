package app.friends;

import java.util.Objects;

public class Friends {

    private String username;

    public Friends(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friends friends = (Friends) o;
        return Objects.equals(username, friends.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
