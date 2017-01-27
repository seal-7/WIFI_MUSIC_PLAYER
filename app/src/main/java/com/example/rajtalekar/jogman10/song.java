package com.example.rajtalekar.jogman10;

import android.media.Image;

/**
 * Created by Raj Talekar on 23-Jan-16.
 */
public class song {
    private long id;
    private String title;
    private String artist;
    private String albumArt;
    private long album_id;
    public song(long songID, String songTitle, String songArtist,String album,long Album_id) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        albumArt=album;
        album_id=Album_id;


    }
    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbumArt(){return albumArt;}
    public long getAlbumID(){return album_id;}
}
