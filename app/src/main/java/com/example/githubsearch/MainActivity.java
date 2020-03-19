package com.example.githubsearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private HttpClient httpClient;

    private TextInputLayout githubSearchInputContainer;
    private Button githubSearchSubmit;
    private ProgressBar progressBar;
    private TextView statusMessage;
    private BottomNavigationView navigationBar;

    private ArrayList<User> foundUsers;

    private boolean usersNotFound;
    private boolean isDownloading;

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
        String a = "9";
        usersNotFound = true;
        githubSearchSubmit.setOnClickListener(new GithubSearchSubmitOnClickListener());
        navigationBar.setOnNavigationItemSelectedListener(new NavigationOnItemSelectedListener());
    }

    private void bindUI() {
        githubSearchInputContainer = (TextInputLayout) findViewById(R.id.githubSearchInputContainer);
        githubSearchInputContainer.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        githubSearchInputContainer.getEditText().setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        githubSearchSubmit = (Button) findViewById(R.id.githubSearchSubmit);
        githubSearchSubmit.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat));
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        statusMessage = (TextView) findViewById(R.id.message);
        navigationBar = (BottomNavigationView) findViewById(R.id.navigationHome);
    }

    private class DownloadUsersTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isDownloading = true;
            progressBar.setVisibility(View.VISIBLE);
            statusMessage.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<User> doInBackground(String... params) {
            String enteredUsername = null;
            foundUsers = null;
            if (params.length > 0) {
                enteredUsername = params[0];
            }
            try {
                foundUsers = httpClient.readUserInfo(enteredUsername);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return foundUsers;
        }

        @Override
        protected void onPostExecute(ArrayList<User> foundUsers) {
            progressBar.setVisibility(View.INVISIBLE);
            isDownloading = false;
            if (foundUsers.isEmpty()) {
                usersNotFound = true;
                if (httpClient.isOutOfConnection()) {
                    statusMessage.setText("Can't get connection to server. Check your internet connection");
                    statusMessage.setVisibility(View.VISIBLE);
                    return;
                }
                if (httpClient.isUsersNotFound()) {
                    statusMessage.setText("Users not found");
                    statusMessage.setVisibility(View.VISIBLE);
                    return;
                }
                if (httpClient.isRequestLimitExceeded()) {
                    statusMessage.setText("Request limit (10 per minute) to server has been exceeded");
                    statusMessage.setVisibility(View.VISIBLE);
                    return;
                } else {
                    statusMessage.setText("Something goes wrong...");
                    statusMessage.setVisibility(View.VISIBLE);
                    return;
                }
            } else {
                if (httpClient.getTotalUsersFound() > HttpClient.MAX_USERS_PER_SEARCH && foundUsers.size() < HttpClient.MAX_USERS_PER_SEARCH) {
                    statusMessage.setText("Request limit (10 per minute) to server has been exceeded. Only " + foundUsers.size() +
                            " of " + httpClient.MAX_USERS_PER_SEARCH + " users are available");
                    statusMessage.setVisibility(View.VISIBLE);
                }
                Intent intent = new Intent(MainActivity.this, PagesUsersActivity.class);
                intent.putExtra("users", foundUsers);
                startActivity(intent);
                usersNotFound = false;
            }
        }
    }

    private class NavigationOnItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_results:
                    if (isDownloading) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Downloading in progress", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    }
                    if (usersNotFound) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Users not found", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, PagesUsersActivity.class);
                        intent.putExtra("users", foundUsers);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    break;
            }
            return false;
        }
    }

    private class GithubSearchSubmitOnClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String enteredUsername = githubSearchInputContainer.getEditText().getText().toString();
            if (isDownloading) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Wait for the download to finish", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (!isInputCorrect(enteredUsername)) {
                return;
            } else {
                new DownloadUsersTask().execute(enteredUsername);
            }
        }

        private boolean isInputCorrect(String enteredUsername) {
            Pattern pattern = Pattern.compile("[a-zA-Z\\d\\s\\p{Punct}]+");
            Matcher matcher = pattern.matcher(enteredUsername);
            if (!matcher.matches()) {
                if (enteredUsername.equals("")) {
                    statusMessage.setText("Username shouldn't be empty");
                    statusMessage.setVisibility(View.VISIBLE);
                } else {
                    statusMessage.setText("You have entered wrong symbols");
                    statusMessage.setVisibility(View.VISIBLE);
                }
                return false;
            } else {
                return true;
            }
        }
    }

}
