package com.example.rajtalekar.jogman10;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Raj Talekar on 4/9/2016.
 */
public class Player {
    private Uri trackUri;
    public static MediaPlayer mp=new MediaPlayer();
    private static Context context;
    static ArrayList<String> nowPlaying;
    static int currentSong;
    Thread updateSeekBar;
   static ArrayList<song> songArrayList=MainActivity.songList;
    public ArrayList<String> getNowPlaying(){
        return nowPlaying;
    }
    public Player(){
    }
    public Player(Uri trackUri,Context context){
        this.trackUri=trackUri;
        Player.context =context;
    }
    public void play(ArrayList<String> nowPlaying1, final int currentSong1){
        nowPlaying=nowPlaying1;
        currentSong=currentSong1;
        ArrayAdapter<String> adp=new ArrayAdapter<String>(context,R.layout.row_layout,R.id.textView,nowPlaying);
        HearAlone.listViewHA.setAdapter(adp);
        ArrayAdapter<String> adp2=new ArrayAdapter<String>(MainActivity.context,R.layout.row_layout,R.id.textView,nowPlaying);
        MainActivity.listViewMA.setAdapter(adp2);
        if(mp!=null){
            mp.stop();
            mp.release();
        }
        mp=new MediaPlayer();
        mp.release();
        mp = MediaPlayer.create(context, trackUri);
        if(mp!=null) {
            mp.start();
            updateSeekBar=new Thread(){
                @Override
                public void run() {
                    int totalDur=mp.getDuration();
                    int currentpos=0;
                    MainActivity.seekBarMA.setMax(totalDur);
                    while(currentpos<totalDur){
                        try{
                            sleep(500);
                            currentpos=mp.getCurrentPosition();
                            MainActivity.seekBarMA.setProgress(currentpos);

                        }catch(Exception e){
                            Log.e("SEEKBAR","PROBLEM IN THREAD/PLAYER.JAVA");
                            e.printStackTrace();
                        }
                    }
                }
            };
            try {
                updateSeekBar.start();
            }catch(Exception e){
                e.printStackTrace();
                while(updateSeekBar.isAlive()){
                    updateSeekBar.interrupt();
                }
            }
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    HearAlone.playing=false;
                    ArtistSongs.playing=false;
                    AlbumSongs.playing=false;
                    nextSong();
                }
            });
        }
    }
    public void stop(){
        mp.stop();
        mp.release();
    }
    public void nextSong(){
        songArrayList=MainActivity.songList;
        try {
            for (int j = 0; j < songArrayList.size(); j++) {
                if (songArrayList.get(j).getTitle().equals(nowPlaying.get(currentSong+1))) {
                    trackUri = ContentUris.withAppendedId(
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            songArrayList.get(j).getID());
                    currentSong=currentSong+1;
                    if (mp.isPlaying()) {
                        stop();
                    }
                    mp = MediaPlayer.create(context, trackUri);
                    play(nowPlaying, currentSong);
                    HearAlone.AlbumId = songArrayList.get(j).getAlbumID();
                    MainActivity.AlbumId = songArrayList.get(j).getAlbumID();
                    HearAlone.ArtistTitle_HearAlone = songArrayList.get(j).getArtist();
                    HearAlone.SongTitle_HearAlone = songArrayList.get(j).getTitle();
                    MainActivity.SongTitle = songArrayList.get(j).getTitle();
                    ArtistSongs.SongTitle = songArrayList.get(j).getTitle();
                    AlbumSongs.SongTitle = songArrayList.get(j).getTitle();
                    ArrayAdapter<String> adp=new ArrayAdapter<String>(context,R.layout.row_layout,R.id.textView,nowPlaying);
                    HearAlone.listViewHA.setAdapter(adp);
                    ArrayAdapter<String> adp2=new ArrayAdapter<String>(MainActivity.context,R.layout.row_layout,R.id.textView,nowPlaying);
                    MainActivity.listViewMA.setAdapter(adp2);
                    HearAlone.seekBarHA.setProgress(0);
                    MainActivity.seekBarMA.setProgress(0);
                    break;
                }
            }
        }catch (IllegalStateException e){
            Toast.makeText(context,"No Next Song",Toast.LENGTH_SHORT).show();

        }
        catch (IndexOutOfBoundsException e){
            Toast.makeText(context,"No Next Song",Toast.LENGTH_SHORT).show();
        }
    }
    public void prevSong(){
        songArrayList=MainActivity.songList;
        try {
            for (int j = 0; j < songArrayList.size(); j++) {
                if (songArrayList.get(j).getTitle().equals(nowPlaying.get(currentSong - 1))) {
                    trackUri = ContentUris.withAppendedId(
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            songArrayList.get(j).getID());
                    currentSong=currentSong-1;
                    if (mp.isPlaying()) {
                        stop();
                    }
                    mp = MediaPlayer.create(context, trackUri);
                    play(nowPlaying, currentSong);
                    HearAlone.AlbumId = songArrayList.get(j).getAlbumID();
                    MainActivity.AlbumId = songArrayList.get(j).getAlbumID();
                    HearAlone.ArtistTitle_HearAlone = songArrayList.get(j).getArtist();
                    HearAlone.SongTitle_HearAlone = songArrayList.get(j).getTitle();
                    MainActivity.SongTitle = songArrayList.get(j).getTitle();
                    ArtistSongs.SongTitle = songArrayList.get(j).getTitle();
                    AlbumSongs.SongTitle = songArrayList.get(j).getTitle();
                    ArrayAdapter<String> adp=new ArrayAdapter<String>(context,R.layout.row_layout,R.id.textView,nowPlaying);
                    HearAlone.listViewHA.setAdapter(adp);
                    ArrayAdapter<String> adp2=new ArrayAdapter<String>(MainActivity.context,R.layout.row_layout,R.id.textView,nowPlaying);
                    MainActivity.listViewMA.setAdapter(adp2);
                    break;
                }
            }
        }catch (IllegalStateException e){
            Toast.makeText(context,"No Prev Song",Toast.LENGTH_SHORT).show();
        }
        catch (IndexOutOfBoundsException e){
            Toast.makeText(context,"No prev Song",Toast.LENGTH_SHORT).show();
        }
    }
    public void playPause(){
        if(mp.isPlaying()){
            mp.pause();
        }
        else{
            mp.start();
        }
        ArrayAdapter<String> adp=new ArrayAdapter<String>(context,R.layout.row_layout,R.id.textView,nowPlaying);
        HearAlone.listViewHA.setAdapter(adp);
        ArrayAdapter<String> adp2=new ArrayAdapter<String>(MainActivity.context,R.layout.row_layout,R.id.textView,nowPlaying);
        MainActivity.listViewMA.setAdapter(adp2);
    }

}
