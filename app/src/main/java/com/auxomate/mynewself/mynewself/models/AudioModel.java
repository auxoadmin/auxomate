package com.auxomate.mynewself.mynewself.models;

public class AudioModel {
    String name;
    String url;
    AudioModel(){

    }

    public AudioModel(String audioName, String audioUrl) {
        this.name = audioName;
        this.url = audioUrl;
    }



    public String getName() {
        return name;
    }

    public void setName(String audioName) {
        this.name = audioName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String audioUrl) {
        this.url = audioUrl;
    }
}
