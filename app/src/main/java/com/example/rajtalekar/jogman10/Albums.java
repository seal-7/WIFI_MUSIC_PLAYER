package com.example.rajtalekar.jogman10;

/**
 * Created by Raj Talekar on 4/4/2016.
 */
public class Albums {
    private String AlbumArt;
    private long AlbumId;
    private String Artist;
    private long SongId;
    public Albums(String AlbumArt,long AlbumId,String Artist,long SongId){
        this.AlbumArt=AlbumArt;
        this.AlbumId=AlbumId;
        this.Artist=Artist;
        this.SongId=SongId;
    }
    public String getAlbumTitle(){
    return AlbumArt;
    }
    public long getAlbumId(){
        return AlbumId;
    }
    public long getSongId(){
        return SongId;
    }
    public String getArtist(){
        return Artist;
    }
}
