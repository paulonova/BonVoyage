package se.paulo.nackademin.examen.bonvoyage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.EditText;


public class EditLimitValuePreference extends DialogPreference {

//    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
//    SharedPreferences.Editor editor = pref.edit();
//    String valueLimit = pref.getString("value_limit", null);
    private EditText setLimit;


    public EditLimitValuePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.limit_value_layout);

    }





}
