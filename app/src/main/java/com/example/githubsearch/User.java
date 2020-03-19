package com.example.githubsearch;

import java.io.Serializable;

public class User implements Serializable {

    private String login;
    private long id;
    private String avatarUrl;
    private String url;
    private String type;
    private double score;

    public User(String login, long id, String avatar_url, String url, String type, double score) {
        this.setLogin(login);
        this.setId(id);
        setAvatarUrl(avatar_url);
        this.setUrl(url);
        this.setType(type);
        this.setScore(score);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
