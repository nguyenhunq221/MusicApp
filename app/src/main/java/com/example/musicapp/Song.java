package com.example.musicapp;

import java.io.Serializable;

public class Song implements Serializable {
    String title;
    String Artist;
    String path;

    public Song(String title, String artist, String path) {
        this.title = title;
        Artist = artist;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
