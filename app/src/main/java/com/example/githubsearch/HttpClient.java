package com.example.githubsearch;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpClient{

    private final JsonParser jsonParser;

    public static final String HEADER_ACCEPT = "Accept";
    public int totalCount;
    public final String MAXIMUM_PER_PAGE = "100";
    public final int MAXIMUM_USERS = 1000;
    public boolean usersNotFound;
    public boolean requestLimitExceeded;

    public HttpClient(){
        jsonParser=new JsonParser();
    }

    public ArrayList<User> readUserInfo(String userName) throws IOException, JSONException {
        int currentPage = 1;
        boolean isLimitReached = false;
        ArrayList<User> users = new ArrayList<>();
        totalCount = 0;
        while(isLimitReached!=true){
            String requestUrl = "https://api.github.com/search/users?q="+userName+"&per_page="+MAXIMUM_PER_PAGE+"&page="+Integer.toString(currentPage);
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(HEADER_ACCEPT, "application/vnd.github.v3+json");
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream in;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                in = connection.getErrorStream();
            } else {
                in = connection.getInputStream();
            }
            String response = convertStreamToString(in);
            jsonParser.getData(response);
            if(jsonParser.isLimitExceeded()){
                requestLimitExceeded=true;
                return null;
            }
            if(currentPage==1){
                totalCount = jsonParser.getTotalCount();
            }
            if(totalCount==0){
                usersNotFound=true;
                return null;
            }
            if(totalCount <= 100){
                users = jsonParser.getListUsers();
                isLimitReached=true;
            }
            else {
                users.addAll(jsonParser.getListUsers());
                if(users.size()==totalCount || users.size()==MAXIMUM_USERS){
                    isLimitReached=true;
                }
                else {
                    currentPage++;
                }
            }
        }
        return users;
    }

    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
