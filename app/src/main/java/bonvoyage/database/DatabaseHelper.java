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

import bonvoyage.objects.Spending;
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



    public Voyage getVoyageInfo(int userId){

        Voyage voyage = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"_id", "user_id", "destiny", "type_voyage", "arrive_date", "exit_date", "budget","number_peoples"}; //, "actualTrip"
        String selection = "user_id=?";
        String[]selectArgs = {userId + ""};

        Cursor cursor = db.query("voyage", projection, selection,selectArgs, null, null, null);

        if(cursor.moveToFirst()){

            //public Voyage(Integer id, Integer user_id, String destiny, String typeTrip, String arrivalDate, String exitDate, Double budget, Integer numberPeoples, Integer actualTrip)
            voyage = new Voyage(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getDouble(6),
                    cursor.getInt(7),
                    cursor.getInt(0));  //actualTrip = _id
        }

        cursor.close();
        return voyage;

    }

    public Spending getSpendingInfo(int voyageId){

        //public Spending(Integer id, String date, String category, String description, Double value, String place, Integer voyageId) {

        Spending spending = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {"_id", "date", "category", "description", "value", "place", "voyage_id"};
        String selection = "voyage_id=?";
        String[]selectArgs = {voyageId + ""};

        Cursor cursor = db.query("spending", projection, selection,selectArgs, null, null, null);

        if(cursor.moveToFirst()){

            spending = new Spending(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getString(5),
                    cursor.getInt(6));

        }

        cursor.close();
        return spending;

    }


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
