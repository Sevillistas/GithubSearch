package com.example.githubsearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public JSONObject data;

    public void getData(String response) throws JSONException {
        data = new JSONObject(response);
    }

    public int getTotalCount() throws JSONException {
        return data.getInt("total_count");
    }

    public boolean isLimitExceeded() throws JSONException {
        return data.has("message");
    }

    public ArrayList<User> getListUsers() throws JSONException {
        ArrayList<User> users = new ArrayList<>();
        JSONArray items = data.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject userJson = items.getJSONObject(i);
            User user = bindData(userJson);
            users.add(user);
        }
        return users;
    }

    public User bindData(JSONObject userJson) throws JSONException {
        String login = userJson.getString("login");
        long id = userJson.getLong("id");
        String avatar_url = userJson.getString("avatar_url");
        String url = userJson.getString("html_url");
        String type = userJson.getString("type");
        double score = userJson.getDouble("score");
        return new User(login, id, avatar_url, url, type, score);
    }
}
