package bonvoyage.database;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import bonvoyage.objects.User;
import bonvoyage.objects.Voyage;

/**
 * Created by Paulo Vila Nova on 2016-04-22.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE = "BonVoyage";
    private static int VERSION = 1;
    private AlertDialog alert;
    Context context;



    public DatabaseHelper(Context context) {
        super(context, DATA_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "email TEXT," +
                    "password TEXT);");

        db.execSQL("CREATE TABLE voyage (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "destiny TEXT," +
                    "type_voyage TEXT," +
                    "arrive_date DATE," +
                    "exit_date DATE," +
                    "budget DOUBLE," +
                    "number_peoples INTEGER," +
                    "user_id INTEGER," +
                    "FOREIGN KEY(user_id) REFERENCES user(_id));");

        db.execSQL("CREATE TABLE spending (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "category TEXT," +
                    "date DATE," +
                    "value DOUBLE," +
                    "description TEXT," +
                    "place TEXT," +
                    "voyage_id INTEGER," +
                    "FOREIGN KEY(voyage_id) REFERENCES voyage(_id));");

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE user");
        db.execSQL("DROP TABLE voyage");
        db.execSQL("DROP TABLE spending");
        onCreate(db);

    }

    //Method to get user information..
    //CONSTRUCTOR ==> public User(String username, String email, String password, int user_id)
    public User getUserInfo(String username){

        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"username", "email", "password", "_id"};
        String selection = "username=?";
        String[]selectArgs = {username};

        Cursor cursor = db.query("user",projection, selection, selectArgs, null, null, null);

        if(cursor.moveToFirst()){

            user = new User(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3));
        }

        cursor.close();
        return user;

    }

    /*              "destiny TEXT," +
                    "type_voyage TEXT," +
                    "arrive_date DATE," +
                    "exit_date DATE," +
                    "budget DOUBLE," +
                    "number_peoples INTEGER," +
                    "user_id INTEGER," +*/



    public void alertOmDatabase (Context context, String title, String message ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("ON CLICK"," GONE!");
            }
        });
        alert = builder.create();
        alert.show();

    }

    public boolean ifDatabaseIsEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor checkDb = db.rawQuery("SELECT COUNT(*) FROM user", null);

        if(checkDb == null){
            return true;
        }else {
            return false;
        }

    }





}
