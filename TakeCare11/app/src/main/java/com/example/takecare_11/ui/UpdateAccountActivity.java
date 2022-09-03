package com.example.takecare_11.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.R;
import com.example.takecare_11.storageclasses.User;
import com.example.takecare_11.storageclasses.database.api.FieldSetter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateAccountActivity extends AppCompatActivity {

    private String id;

    //ui
    private EditText passwordField, emailField, mobileField;
    private Button updateBtn;

    //validate
    private class Validate{
        boolean email, password, mobile;
        Validate(){ email = password = mobile = true; }
        void SetButton(Button button)
        {
            if(email && password && mobile)
                button.setEnabled(true);
            else button.setEnabled(false);
        }
    }
    private  Validate validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        //init
        id = getIntent().getStringExtra("user_id");
        validate = new Validate();

        emailField = findViewById(R.id.update_account_email);
        passwordField = findViewById(R.id.update_account_password);
        mobileField = findViewById(R.id.update_account_mobile);
        updateBtn = findViewById(R.id.update_account_update_btn);

        User user = new User(id, getApplicationContext());

        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        mobileField.setText(user.getMobile());

        emailField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String CheckMe = emailField.getText().toString();

                Pattern email = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                Matcher matches = email.matcher(CheckMe);
                if(!matches.matches())
                {
                    emailField.setError("Enter a valid Email Address");
                    validate.email = false;
                }

                if(matches.matches())
                    validate.email = true;

                //enable button callback
                validate.SetButton(updateBtn);
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String CheckMe = passwordField.getText().toString();
                if(CheckMe.length()<6)
                {
                    passwordField.setError("Password should contain atleast 6 characters");
                    validate.password = false;
                }

                if(CheckMe.length()>=6)
                    validate.password = true;

                //enable button callback
                validate.SetButton(updateBtn);
            }
        });

        mobileField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String CheckMe = mobileField.getText().toString();
                if(CheckMe.length()<10)
                {
                    mobileField.setError("Enter a 10-digit Mobile Number");
                    validate.mobile = false;
                }

                if(CheckMe.length()==10)
                    validate.mobile = true;

                //enable button callback
                validate.SetButton(updateBtn);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                try{
                    result = new FieldSetter(getApplicationContext(), id, "email", emailField.getText().toString(), "users").execute().get();
                    if(!result.equals("success"))
                        throw new Exception();
                    result = new FieldSetter(getApplicationContext(), id, "password", passwordField.getText().toString(), "users").execute().get();
                    if(!result.equals("success"))
                        throw new Exception();
                    result = new FieldSetter(getApplicationContext(), id, "mobile", mobileField.getText().toString(), "users").execute().get();
                    if(!result.equals("success"))
                        throw new Exception();
                    UpdateAccountActivity.this.finish();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
