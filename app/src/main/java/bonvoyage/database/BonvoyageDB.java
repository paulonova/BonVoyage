package bonvoyage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import bonvoyage.objects.Spending;
import bonvoyage.objects.User;
import bonvoyage.objects.Voyage;


public class BonvoyageDB {

    private SQLiteDatabase db;
    User user;
    Voyage voyage;
    Spending spend;
    DatabaseHelper dbHelper;


    public BonvoyageDB(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }


    // Using in ResetAllDatabase..
    public void dropAllDataBase(){

        try {
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS voyage");
            db.execSQL("DROP TABLE IF EXISTS spending");

            db.execSQL( "CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
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

            Log.d("Drop and Re-create"," Was successfully!!");


        }catch (SQLiteException e){
            Log.e("dropAllDataBase", "ERROR: dropAllDataBase" + e.getMessage());
        }

        db.close();

    }

    // Using in ResetAllDatabase..
    public void dropVoyageDataBase(){

        try {
//            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS voyage");
            db.execSQL("DROP TABLE IF EXISTS spending");

//            db.execSQL( "CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    "username TEXT," +
//                    "email TEXT," +
//                    "password TEXT);");

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

            Log.d("Drop and Re-create"," Was successfully!!");


        }catch (SQLiteException e){
            Log.e("dropAllDataBase", "ERROR: dropAllDataBase" + e.getMessage());
        }

        db.close();


    }


}
