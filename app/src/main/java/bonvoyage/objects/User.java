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

    private String usename;
    private String email;
    private String password;


   public User() {
    }

    public User(String usename, String email, String password) {
        this.usename = usename;
        this.email = email;
        this.password = password;
    }


    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
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
}
