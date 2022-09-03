package com.example.takecare_11.signin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.takecare_11.BeginActivity;
import com.example.takecare_11.MainActivity;
import com.example.takecare_11.MyApplication;
import com.example.takecare_11.R;
import com.example.takecare_11.register.RegisterUserActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    //ui elements
    private EditText usernameField, passwordField;
    private Button signInBtn, registerBtn;

    //form validation
    private class Validate{
        boolean username, password;

        Validate()
        {
            username = password = false;
        }

        void SetButton(Button button)
        {
            if(username && password)
                button.setEnabled(true);
            else button.setEnabled(false);
        }
    }
    private Validate validate;

    //Async task for sign in
    private class ProceedSignIn extends AsyncTask <String, Integer, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try{
                String host = ((MyApplication)getApplication()).getHost();
                String username = strings[0];
                String password = strings[1];
                String link = host + "signin.php?username=" + username + "&password=" + password;

                //make connection
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.111 Mobile Safari/537.36");
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


            }catch (java.net.SocketTimeoutException timeOut){
                return "Internet not Available";

            }
            catch (Exception e)
            {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            AfterSignIn(result);
        }
    }

    private void AfterSignIn(String result)
    {
        try{
            //success if valid id
            int id = Integer.parseInt(result);  //valid result if no exception
            //make sign in token
            SignInToken signInToken = new SignInToken(getApplicationContext());
            signInToken.CreateToken(usernameField.getText().toString(), passwordField.getText().toString());

            //make a key less sign in
            startActivity(new Intent(this, BeginActivity.class));
            this.finish();
        }
        catch (Exception e)
        {
            //something wrong in prev step, show error
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //init
        usernameField = findViewById(R.id.signin_username);
        passwordField = findViewById(R.id.signin_password);
        signInBtn = findViewById(R.id.signin_btn);
        registerBtn = findViewById(R.id.signin_register_btn);
        signInBtn.setEnabled(false);
        validate = new Validate();

        //set text watchers
        usernameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String checkMe = usernameField.getText().toString();
                if(checkMe.length()<6)
                    usernameField.setError("Username must be of at least 6 characters");

                Pattern splchar = Pattern.compile("^[a-zA-Z._]*$");
                Matcher matcher = splchar.matcher(checkMe);
                if(!matcher.matches())
                    usernameField.setError("Name Can't have any Special Characters other than '.' or '_'");

                if(matcher.matches() && checkMe.length()>=6)
                    validate.username = true;
                else  validate.username = false;

                validate.SetButton(signInBtn);
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String checkMe = passwordField.getText().toString();
                if(checkMe.length()<5) {
                    passwordField.setError("Username must be of at least 5 characters");
                    validate.password = false;
                }
                else validate.password = true;

                validate.SetButton(signInBtn);
            }
        });

        //set onClick listeners
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //proceed sign in
                new ProceedSignIn().execute(usernameField.getText().toString(),
                        passwordField.getText().toString());
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to register activity
                startActivity(new Intent(SignInActivity.this, RegisterUserActivity.class));
            }
        });
    }
}
