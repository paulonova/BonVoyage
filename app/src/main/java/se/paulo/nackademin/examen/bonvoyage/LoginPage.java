package se.paulo.nackademin.examen.bonvoyage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.User;

public class LoginPage extends AppCompatActivity {

    Button confirmBtn;
    TextView register;
    EditText username, password;
    private DatabaseHelper helper;
    User user;

    public static final String USER_INFO_PREFERENCE = "user_info";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_layout);

        username = (EditText)findViewById(R.id.edt_login_username);
        password = (EditText)findViewById(R.id.edt_login_password);

        // Prepare access to database..
        helper = new DatabaseHelper(this);

        confirmBtn = (Button)findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkUserInfo(username.getText().toString(), password.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), MenuVoyageActivity.class);
                    startActivity(intent);
                    String name = helper.getUserInfo(username.getText().toString()).getUsername();
                    Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_SHORT).show();
                    saveUserInfoInSharedPreferences(name);
                    finish();
                }else{
                    String msg = "Username or Password is wrong! try again..";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void sendToRegister(View v){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public boolean checkUserInfo(String username, String password){

        user = new User();

        if(!username.equals("") || !password.equals("")){ //*******?????????????????????????????

            int id = helper.getUserInfo(username).getUser_id();

            //Getting from Database all user information to login and everything else..
            String user_name = helper.getUserInfo(username).getUsername();
            String user_password = helper.getUserInfo(username).getPassword();
            String user_email = helper.getUserInfo(username).getEmail();
            Log.i("USER_ID", "" + id + " - " + user_name + " - " + user_password + " - " + user_email);

            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT username, password, email FROM user WHERE _id=" + id, null);
            cursor.moveToFirst();

            for (int i = 0; i <cursor.getCount() ; i++) {
                user.setUsername(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                cursor.moveToNext();
            }
            cursor.close();

            return user.getUsername().equals(username) && user.getPassword().equals(password);

        }else {

            Toast.makeText(getApplicationContext(), "Fields cannot be empty..", Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    public void saveUserInfoInSharedPreferences(String username){

        SharedPreferences preferences = getSharedPreferences(USER_INFO_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", helper.getUserInfo(username).getUser_id());
        editor.putString("user_name", helper.getUserInfo(username).getUsername());
        editor.putString("user_password", helper.getUserInfo(username).getPassword());
        editor.putString("user_email", helper.getUserInfo(username).getEmail());
        editor.apply();

        Log.i("SharedPreferences", "**Saved in Preferences**");
    }
}
