package se.paulo.nackademin.examen.bonvoyage;

import android.content.Intent;
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
        int id = helper.getUserInfo(username).getUser_id();
        String name = helper.getUserInfo(username).getUsername();
        String passw = helper.getUserInfo(username).getPassword();
        String email = helper.getUserInfo(username).getEmail();
        Log.i("USER_ID", "" + id + " - " + name + " - " + passw + " - " + email);
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

    }
}
