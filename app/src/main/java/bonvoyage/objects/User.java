package bonvoyage.objects;

/**
 * Created by Paulo Vila Nova on 2016-04-22.
 */
public class User {

    public static final String TABLE = "user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String[] COLUMNS = new String[]{USERNAME, EMAIL, PASSWORD};

    private String username;
    private String email;
    private int user_id;
    private String password;


   public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Constructor with user_id
    public User(String username, String email, String password, int user_id) {
        this.username = username;
        this.email = email;
        this.user_id = user_id;

        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
