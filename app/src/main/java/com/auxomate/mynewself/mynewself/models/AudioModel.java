package com.auxomate.mynewself.mynewself.models;

public class AudioModel {
    String name;
    String url;
    AudioModel(){

    }

   int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AudioModel(String audioName, String audioUrl,int position) {
        this.name = audioName;
        this.url = audioUrl;
        this.position = position;
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
