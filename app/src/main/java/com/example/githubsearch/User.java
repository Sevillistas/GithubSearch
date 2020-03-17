package com.example.githubsearch;

import java.io.Serializable;

public class User implements Serializable {

    public String login;
    public long id;
    public String avatarUrl;
    public String url;
    public String type;
    public double score;

    public User(String login, long id, String avatar_url, String url, String type, double score){
        this.login = login;
        this.id = id;
        avatarUrl =avatar_url;
        this.url =url;
        this.type =type;
        this.score =score;
    }

}
