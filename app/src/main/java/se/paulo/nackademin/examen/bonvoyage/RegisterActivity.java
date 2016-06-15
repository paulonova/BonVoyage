package se.paulo.nackademin.examen.bonvoyage;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener  {

    private User user;
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";

    private EditText username;
    private EditText email;
    private EditText password;

    private Button save;
    private Button cancel;

    private DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.userName);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        save = (Button)findViewById(R.id.saveRegisterBtn);
        save.setOnClickListener(this);

        cancel = (Button)findViewById(R.id.cancelRegisterBtn);
        cancel.setOnClickListener(this);

        // Prepare access to database..
        helper = new DatabaseHelper(this);


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.saveRegisterBtn:
                saveInputValues();
                break;

            case R.id.cancelRegisterBtn:
                finish();
                Toast.makeText(getApplicationContext(), "Register canceled", Toast.LENGTH_SHORT).show();
        }

    }

    public void saveInputValues() {

        user = new User();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());

        SQLiteDatabase db = helper.getReadableDatabase();

        try {

            ContentValues values = new ContentValues();
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            values.put("email", user.getEmail());

            long result = db.insert("user", null, values);

            if (result != -1) {
                Toast.makeText(this, "Register saved successfully..", Toast.LENGTH_SHORT).show();
                finish();
            }

        }catch (SQLiteException e){
            Log.e("SQLiteException","" + e.getMessage());
            Toast.makeText(this, "Register NOT saved! ", Toast.LENGTH_SHORT).show();
        }


        String msg = user.getUsername() + " - " + user.getPassword() + " - " + user.getEmail();
        Log.i("USER INFO","" + msg);

    }

}
