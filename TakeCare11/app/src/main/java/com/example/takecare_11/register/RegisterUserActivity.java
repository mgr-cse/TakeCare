package com.example.takecare_11.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity {

    //ui elements
    private EditText nameField, usernameField, passwordField, emailField, mobileField, dobField;
    private Button nextBtn;

    //validation
    private class Validate{
        boolean name, username, password, email, mobile, dob;
        Validate()
        {
            name = username = password = email = mobile = dob = false;
        }
        void SetButton(Button button)
        {
            if(name && username && password && email && mobile && dob)
                button.setEnabled(true);
            else button.setEnabled(false);
        }
    }
    private Validate validate;

    //Async task for registration
    private class ProceedRegistration extends AsyncTask<String, Integer, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String host = ((MyApplication)getApplication()).getHost();

            String name = strings[0];
            String username = strings[1];
            String password = strings[2];
            String email = strings[3];
            String mobile = strings[4];
            String dob = strings[5];

            String link = host + "registeruser.php" +
                    "?name=" + name +
                    "&username=" + username +
                    "&password=" + password +
                    "&email=" + email +
                    "&mobile=" + mobile +
                    "&dob=" + dob;

            //make connection
           try{
               URL url = new URL(link);
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

               urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
               urlConnection.setRequestProperty("Cookie", "__test=2ad4957fd60a8b74775f11b309b4496a; expires=Friday, 1 January 2038 at 01:55:55; path=/");

               //read data
               InputStream is = urlConnection.getInputStream();
               InputStreamReader isr = new InputStreamReader(is);
               BufferedReader br = new BufferedReader(isr);

               String data = "";
               StringBuffer pageContent = new StringBuffer("");

               while ((data=br.readLine())!=null)
                   pageContent.append(data);
               return  pageContent.toString();

           }catch (Exception e)
           {
               return e.getMessage();
           }
        }

        @Override
        protected void onPostExecute(String result)
        {
            AfterRegistration(result);
        }

    }

    private void AfterRegistration(String result)
    {
        try {
            int id = Integer.parseInt(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, RegisterUserType.class);
            intent.putExtra("user_id", result);
            startActivity(intent);
            this.finish();

        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //init
        nameField = findViewById(R.id.register_user_name);
        usernameField = findViewById(R.id.register_user_username);
        passwordField = findViewById(R.id.register_user_password);
        emailField = findViewById(R.id.register_user_email);
        mobileField = findViewById(R.id.register_user_mobile);
        dobField = findViewById(R.id.ragister_user_dob);
        nextBtn = findViewById(R.id.register_user_next);
        nextBtn.setEnabled(false);
        validate = new Validate();

        //set text watchers
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String CheckMe = nameField.getText().toString();
                if(CheckMe.length()==0)
                {
                    nameField.setError("This Cant be Empty!");
                    validate.name = false;
                }

                Pattern splchar = Pattern.compile("^[a-zA-Z ]*$");
                Matcher matches = splchar.matcher(CheckMe);
                if(!matches.matches())
                {
                    nameField.setError("Name Can't have any Special Characters");
                    validate.name = false;
                }

                if(matches.matches() && CheckMe.length()!=0)
                    validate.name = true;

                //enable button
                validate.SetButton(nextBtn);
            }
        });

        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String CheckMe = usernameField.getText().toString();
                if(CheckMe.length()<6)
                {
                    usernameField.setError("Username should contain atleast 6 characters");
                    validate.username = false;
                }

                Pattern splchar = Pattern.compile("^[a-zA-Z._]*$");
                Matcher matches = splchar.matcher(CheckMe);
                if(!matches.matches())
                {
                    usernameField.setError("Name Can't have any Special Characters other than '.' or '_'");
                    validate.username = false;
                }

                if(matches.matches() && CheckMe.length()>=6)
                    validate.username = true;

                //enable button callback
                validate.SetButton(nextBtn);
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
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
                validate.SetButton(nextBtn);
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
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
                validate.SetButton(nextBtn);
            }
        });

        mobileField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
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
                validate.SetButton(nextBtn);
            }
        });

        dobField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String CheckMe = dobField.getText().toString();

                Pattern dob = Pattern.compile("^(0?[1-9]|[12][0-9]|3[01])[\\/\\-](0?[1-9]|1[012])[\\/\\-]\\d{4}$");
                Matcher matches = dob.matcher(CheckMe);
                if(!matches.matches())
                {
                    dobField.setError("Enter a valid Date in dd-mm-yyyy format");
                    validate.dob = false;
                }

                if(matches.matches())
                {
                    int year = Integer.parseInt(CheckMe.substring(6, 10));
                    int CurrYear = Calendar.getInstance().get(Calendar.YEAR);
                    int age = CurrYear-year;
                    if(age<18)
                    {
                        dobField.setError("You Should be atleast 18!");
                        validate.dob = false;
                    }
                    else validate.dob = true;
                }

                //enable button callback
                validate.SetButton(nextBtn);
            }
        });

        //add on click listeners
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProceedRegistration().execute(
                        nameField.getText().toString(),
                        usernameField.getText().toString(),
                        passwordField.getText().toString(),
                        emailField.getText().toString(),
                        mobileField.getText().toString(),
                        dobField.getText().toString()
                );
            }
        });
    }
}
