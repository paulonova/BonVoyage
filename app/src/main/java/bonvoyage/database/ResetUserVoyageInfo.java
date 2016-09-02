package bonvoyage.database;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.Toast;

import se.paulo.nackademin.examen.bonvoyage.MenuVoyageActivity;
import se.paulo.nackademin.examen.bonvoyage.R;


public class ResetUserVoyageInfo extends DialogPreference {

    private TextView alertText;
    BonvoyageDB dbDropAllDatabase;



    public ResetUserVoyageInfo(Context context, AttributeSet attrs) {
        super(context, attrs);

        /** If i need to create a more complex AlertDialog, I can create a new Layout and then use setDialogLayoutResource */
        //setDialogLayoutResource(R.layout.delete_database);
    }


    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        this.setDialogMessage("");

        if(positiveResult){
            String msg = "Drop and re-create only Voyages table was successfully!";

            try {
                dbDropAllDatabase = new BonvoyageDB(getContext());
                dbDropAllDatabase.dropVoyageDataBase();
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                startMenuVoyageActivity();

            }catch (SQLiteException e){
                Toast.makeText(getContext(), "Drop and re-create table did not worked.." + e.getMessage(), Toast.LENGTH_LONG).show();
                startMenuVoyageActivity();
            }


        }else {
            //do nothing
        }

    }


    //Method to start MenuVoyage
    public void startMenuVoyageActivity(){
        Intent intent = new Intent(getContext(), MenuVoyageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getContext().startActivity(intent);
    }
}
