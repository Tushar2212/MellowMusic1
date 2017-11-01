package com.example.mukeshsharma.mymusic;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mukesh Sharma on 31-07-2017.
 */

public class Song {
    Bitmap img;
    String  title;
    String artist;
    String duration;
    String id;
    String location;

    public Song(String title, String artist, String duration,String id,String location) {
        //  this.img = img;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.id=id;
        this.location=location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
