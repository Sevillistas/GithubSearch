package com.example.githubsearch;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpClient {

    public static final int MAX_USERS_PER_PAGE = 100;
    public static final int MAX_USERS_PER_SEARCH = 1000;

    private final JsonParser jsonParser;

    private int totalUsersFound;
    private boolean usersNotFound;
    private boolean requestLimitExceeded;
    private boolean outOfConnection;

    public HttpClient() {
        jsonParser = new JsonParser();
    }

    public ArrayList<User> readUserInfo(String userName) throws IOException, JSONException {
        int currentPage = 1;
        boolean isUsersLimitReached = false;
        ArrayList<User> foundedUsers = new ArrayList<>();
        setTotalUsersFound(0);
        while (isUsersLimitReached != true) {
            String requestUrl = "https://api.github.com/search/users?q=" + userName + "&per_page=" + MAX_USERS_PER_PAGE + "&page=" + Integer.toString(currentPage);
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.connect();
                setOutOfConnection(false);
            } catch (IOException e) {
                setOutOfConnection(true);
            }
            InputStream in;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                in = connection.getErrorStream();
            } else {
                in = connection.getInputStream();
            }
            String response = convertStreamToString(in);
            jsonParser.getData(response);

            if (jsonParser.isRequestLimitExceeded()) {
                setRequestLimitExceeded(true);
                return foundedUsers;
            } else {
                setRequestLimitExceeded(false);
                if (currentPage == 1) {
                    setTotalUsersFound(jsonParser.getTotalUsersFound());
                    if (getTotalUsersFound() == 0) {
                        setUsersNotFound(true);
                        return foundedUsers;
                    }
                    setUsersNotFound(false);
                }
            }

            if (getTotalUsersFound() <= MAX_USERS_PER_PAGE) {
                foundedUsers = jsonParser.getListUsers();
                isUsersLimitReached = true;
            } else {
                foundedUsers.addAll(jsonParser.getListUsers());
                if (foundedUsers.size() == getTotalUsersFound() || foundedUsers.size() == MAX_USERS_PER_SEARCH) {
                    isUsersLimitReached = true;
                } else {
                    currentPage++;
                }
            }
        }
        return foundedUsers;
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public int getTotalUsersFound() {
        return totalUsersFound;
    }

    public void setTotalUsersFound(int totalUsersFound) {
        this.totalUsersFound = totalUsersFound;
    }

    public boolean isUsersNotFound() {
        return usersNotFound;
    }

    public void setUsersNotFound(boolean usersNotFound) {
        this.usersNotFound = usersNotFound;
    }

    public boolean isRequestLimitExceeded() {
        return requestLimitExceeded;
    }

    public void setRequestLimitExceeded(boolean requestLimitExceeded) {
        this.requestLimitExceeded = requestLimitExceeded;
    }

    public boolean isOutOfConnection() {
        return outOfConnection;
    }

    public void setOutOfConnection(boolean outOfConnection) {
        this.outOfConnection = outOfConnection;
    }
}
