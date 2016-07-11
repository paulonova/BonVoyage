package se.paulo.nackademin.examen.bonvoyage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    AlertDialog alert;

    public static final String DEFAULT_CONNECTED = "be_connected";
    public static CheckBox beConnected;
    public static final String USER_INFO_PREFERENCE = "user_info";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_layout);

        username = (EditText)findViewById(R.id.edt_login_username);
        password = (EditText)findViewById(R.id.edt_login_password);
        beConnected = (CheckBox)findViewById(R.id.cbxKeepInlog);

        //Getting info if the checkBox is checked or not..
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean checked = preferences.getBoolean(DEFAULT_CONNECTED, false);
        Log.d("DEFAULT_CONNECTED", "Value: " + checked);

        if(!checked){
            //do nothing
            Log.i("CHECKBOX login","NOT CHECKED!!");
        }else{
            Log.i("CHECKBOX login","CHECKED!!");
            startActivity(new Intent(this, MenuVoyageActivity.class));
            finish();
        }

        // Prepare access to database..
        helper = new DatabaseHelper(this);

        confirmBtn = (Button)findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkIfDatabaseIsEmpty()){

                        if(checkUserInfo(username.getText().toString(), password.getText().toString())){

                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginPage.this);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean(DEFAULT_CONNECTED, beConnected.isChecked());
                            Log.d("CheckBox Default", "Value: " + beConnected.isChecked());
                            editor.apply();


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

                }else {
                    Toast.makeText(getApplicationContext(), "No user information has found! \n Register first..", Toast.LENGTH_SHORT).show();
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

                if(!username.equals("") && !password.equals("")){

                    //Getting from Database all user information to login and everything else..
                    int user_id = helper.getUserInfo(username).getUser_id();
                    String user_name = helper.getUserInfo(username).getUsername();
                    String user_password = helper.getUserInfo(username).getPassword();
                    String user_email = helper.getUserInfo(username).getEmail();
                    Log.i("USER_ID", "" + user_id + " - " + user_name + " - " + user_password + " - " + user_email);

                    SQLiteDatabase db = helper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT username, password, email FROM user WHERE _id=" + user_id, null);
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


    public boolean checkIfDatabaseIsEmpty(){
        SQLiteDatabase db = helper.getReadableDatabase();
        String count = "SELECT * FROM user";
        Cursor cursor = db.rawQuery(count, null);
        boolean a =  cursor.moveToFirst();
        Log.i("Empty database", "" + a);

        cursor.close();
        return a;



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

    /**
     * Method to use when onBackPressed() is used.
     */
    public void alertBeforeClose() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(R.string.close_alert);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Nothing
            }
        });

        alert = builder.create();
        alert.show();

    }


    @Override
    public void onBackPressed() {
        alertBeforeClose();

    }


}
