package bonvoyage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import se.paulo.nackademin.examen.bonvoyage.R;


public class EditLimitValuePreference extends DialogPreference {

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
    SharedPreferences.Editor editor = pref.edit();
    String valueLimit = pref.getString("value_limit", null);

    private EditText setLimit;


    public EditLimitValuePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.limit_value_layout);

    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        setLimit = (EditText) v.findViewById(R.id.limit_value);
        setLimit.setHint(valueLimit);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        String value = setLimit.getEditableText().toString();

        // OK Button
        if (positiveResult) {

            if (!value.equals("")) {
                editor.putString("value_limit", value);
                editor.commit();
                valueLimit = pref.getString("value_limit", null);
                Toast.makeText(getContext(), "Limit value saved = " + valueLimit, Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getContext(), "No values were entered!", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), "Old value is: " + valueLimit, Toast.LENGTH_LONG).show();
            }

            // Cancel Button
        } else if (!positiveResult) {
            // Do nothing
        }
    }
}
