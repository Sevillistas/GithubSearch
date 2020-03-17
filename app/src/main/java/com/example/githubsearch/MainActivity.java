package com.example.githubsearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HttpClient httpClient;
    private TextInputLayout githubSearchInputContainer;
    private Button githubSearchSubmit;
    private ProgressBar progressBar;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        httpClient = new HttpClient();
        bindUI();
        githubSearchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = githubSearchInputContainer.getEditText().getText().toString();
                if (username.equals("")) {
                    message.setText("Username shouldn't be empty");
                    message.setVisibility(View.VISIBLE);
                    return;
                }
                new DownloadUsersTask().execute(username);
            }
        });
    }

    public void bindUI() {
        githubSearchInputContainer = (TextInputLayout) findViewById(R.id.githubSearchInputContainer);
        githubSearchInputContainer.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        githubSearchInputContainer.getEditText().setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        githubSearchSubmit = (Button) findViewById(R.id.githubSearchSubmit);
        githubSearchSubmit.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        message = (TextView) findViewById(R.id.message);
    }

    public class DownloadUsersTask extends AsyncTask<String, Void, ArrayList<User>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            message.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            String username=null;
            ArrayList<User> users = null;
            if(params.length>0){
                username=params[0];
            }
            try{
                users = httpClient.readUserInfo(username);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return users;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            progressBar.setVisibility(View.INVISIBLE);
            if(users == null && httpClient.outOfConnection){
                message.setText("Can't get connection to server. Check your internet connection");
                message.setVisibility(View.VISIBLE);
                return;
            }
            if (users == null && httpClient.usersNotFound) {
                message.setText("Users not found");
                message.setVisibility(View.VISIBLE);
                return;
            }
            if (users == null && httpClient.requestLimitExceeded) {
                message.setText("Request limit has been exceeded");
                message.setVisibility(View.VISIBLE);
                return;
            }
            if (users!=null) {
                Intent intent = new Intent(MainActivity.this, PagesUsers.class);
                intent.putExtra("users", users);
                startActivity(intent);
            }
            else{
                message.setText("Something goes wrong...");
                message.setVisibility(View.VISIBLE);
                return;
            }
        }
    }
}
