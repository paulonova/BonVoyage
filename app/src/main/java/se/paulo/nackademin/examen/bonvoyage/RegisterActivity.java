package se.paulo.nackademin.examen.bonvoyage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        user = new User();

        username = (EditText)findViewById(R.id.userName);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);

        save = (Button)findViewById(R.id.saveRegisterBtn);
        save.setOnClickListener(this);

        cancel = (Button)findViewById(R.id.cancelRegisterBtn);
        cancel.setOnClickListener(this);


    }


    public void cancelActivity(View v){
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.saveRegisterBtn:
                checkInputValues();
                break;

            case R.id.cancelRegisterBtn:
                finish();
                Toast.makeText(getApplicationContext(), "Register canceled", Toast.LENGTH_SHORT).show();
        }

    }

    public void checkInputValues() {




    }

}
