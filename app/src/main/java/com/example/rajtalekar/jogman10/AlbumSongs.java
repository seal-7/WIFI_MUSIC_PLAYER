package com.example.rajtalekar.jogman10;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumSongs extends AppCompatActivity {
    ArrayList<song> songArrayList;
    ArrayList<Albums> list;
    TextView Song_TitleTv;
    MainActivity mainActivity;
    public static long AlbumId;
    public static String SongTitle=new String();
    ImageView AlbumArt_imageview;
    public Uri trackUri;
    RelativeLayout AlbumArtBlured;
    Player  player=new Player();
    public static SeekBar seekBarAS;
    public static Boolean playing=false;
    Thread updateSeekbar;
    Bitmap BitmapArt;
    public static ImageButton PlayPause;
    public void Set_Playing(Boolean playing){
        this.playing=playing;
    }
    public void set_SongDetailes(String newSongTitle,long newAlbumId){
        SongTitle=newSongTitle;
        AlbumId=newAlbumId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        AlbumArtBlured=(RelativeLayout)findViewById(R.id.relativeLayout3);
        seekBarAS=(SeekBar)findViewById(R.id.seekBar);
        final SlidingDrawer slidingDrawer=(SlidingDrawer)findViewById(R.id.slidingDrawer1);
        Song_TitleTv=(TextView)findViewById(R.id.song_Title);
        ListView lv=(ListView)findViewById(R.id.listView3);
        Intent intent=getIntent();
        String recivedAlbum=intent.getStringExtra(Section_3.passalbum);
        final RelativeLayout AlbumArtBlured=(RelativeLayout)findViewById(R.id.relativeLayout3);
        AlbumArt_imageview=(ImageView)findViewById(R.id.AlbumArt_imageview);
        mainActivity=new MainActivity();
        final HearAlone hearAlone=new HearAlone();
        final ArtistSongs artistSongs=new ArtistSongs();
        PlayPause=(ImageButton)findViewById(R.id.play);
        final ImageButton Next=(ImageButton)findViewById(R.id.next);
        final ImageButton Prev=(ImageButton)findViewById(R.id.prev);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.nextSong();
                updateSdRight();
            }
        });
        Prev.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                player.prevSong();
                updateSdRight();
            }
        });
        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playing==true) {
                    PlayPause.setBackgroundResource(R.drawable.play);
                    playing = false;
                    hearAlone.Set_playing(false);
                    artistSongs.Set_Playing(false);
                    mainActivity.Set_Playing(false);
                }
                else {
                    PlayPause.setBackgroundResource(R.drawable.pause);
                    playing=true;
                    hearAlone.Set_playing(true);
                    artistSongs.Set_Playing(true);
                    mainActivity.Set_Playing(true);
                }
                player.playPause();

            }
        });
        seekBarAS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Player.mp.seekTo(seekBar.getProgress());
            }
        });

        TextView tv=(TextView)findViewById(R.id.textView_AlbumSongs);
        tv.setText(recivedAlbum);
        tv.setSelected(true);
        Song_TitleTv.setText(SongTitle);
        BitmapArt = hearAlone.getAlbumart(AlbumId, getApplicationContext(), getResources());
//                BlurBuilder blurBuilder=new BlurBuilder();
//                Bitmap   bitmap1=blurBuilder.blur(getApplicationContext(),BitmapArt);
        if (BitmapArt == null) {
            AlbumArtBlured.setBackgroundResource(R.drawable.mpbackgrounddefault);
        } else {
            BlurBuilder blurBuilder = new BlurBuilder();
            Bitmap bitmap1 = blurBuilder.blur(getApplicationContext(), BitmapArt);
            Drawable d = new BitmapDrawable(getResources(), bitmap1);
            Drawable d1 = new BitmapDrawable(getResources(), BitmapArt);
            AlbumArtBlured.setBackground(d);
            AlbumArt_imageview.setBackground(d1);
        }
        slidingDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollStarted() {
                Song_TitleTv.setText(SongTitle);
                if (playing) {
                    PlayPause.setBackgroundResource(R.drawable.pause);
                } else {
                    PlayPause.setBackgroundResource(R.drawable.play);
                }

            }

            @Override
            public void onScrollEnded() {

            }
        });
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                Song_TitleTv.setSelected(true);
                slidingDrawer.setClickable(true);
            }
        });
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                slidingDrawer.setClickable(false);
            }
        });
        songArrayList=mainActivity.return_ArrayList();
        list=new ArrayList<Albums>();
        for(int i=0;i<songArrayList.size();i++){
            if(recivedAlbum.equals(songArrayList.get(i).getAlbumArt())){
                list.add(new Albums(songArrayList.get(i).getTitle(), songArrayList.get(i).getAlbumID(),songArrayList.get(i).getArtist(),songArrayList.get(i).getID()));
            }
        }
        Collections.sort(list, new Comparator<Albums>() {
            @Override
            public int compare(Albums a, Albums b) {
                return a.getAlbumTitle().compareTo(b.getAlbumTitle());
            }

        });

        final ArrayList<String > list1=new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                list1.add(list.get(i).getAlbumTitle());
            }
        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),R.layout.row_layout,R.id.textView,list1);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongTitle=list.get(position).getAlbumTitle();
                AlbumId=list.get(position).getAlbumId();
                hearAlone.Set_SongDetailes(list.get(position).getAlbumTitle(), list.get(position).getArtist(), list.get(position).getAlbumId());
                hearAlone.Set_nowPlaying(list1);
                mainActivity.set_SongDetailes(list.get(position).getAlbumTitle(), list.get(position).getAlbumId());
                artistSongs.set_SongDetailes(SongTitle, list.get(position).getAlbumId());
                hearAlone.Set_playing(true);
                mainActivity.Set_Playing(true);
                artistSongs.Set_Playing(true);
                BitmapArt = hearAlone.getAlbumart(AlbumId, getApplicationContext(), getResources());
//                BlurBuilder blurBuilder=new BlurBuilder();
//                Bitmap   bitmap1=blurBuilder.blur(getApplicationContext(),BitmapArt);
                if (BitmapArt == null) {
                    AlbumArtBlured.setBackgroundResource(R.drawable.mpbackgrounddefault);
                } else {
                    BlurBuilder blurBuilder = new BlurBuilder();
                    Bitmap bitmap1 = blurBuilder.blur(getApplicationContext(), BitmapArt);
                    Drawable d = new BitmapDrawable(getResources(), bitmap1);
                    Drawable d1 = new BitmapDrawable(getResources(), BitmapArt);
                    AlbumArtBlured.setBackground(d);
                    AlbumArt_imageview.setBackground(d1);
                }
                onStart();
                //play pause buttouns
                PlayPause.setBackgroundResource(R.drawable.pause);
                playing=true;
                //play pause buttons
                trackUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        list.get(position).getSongId());
                player=new Player(trackUri,getApplicationContext());
                player.play(list1,position);

            }
        });
    }
    public void updateSdRight(){
        Song_TitleTv.setText(HearAlone.SongTitle_HearAlone);

        BitmapArt = HearAlone.getAlbumart(AlbumId, getApplicationContext(), getResources());
//                BlurBuilder blurBuilder=new BlurBuilder();
//                Bitmap   bitmap1=blurBuilder.blur(getApplicationContext(),BitmapArt);
        if (BitmapArt == null) {
            AlbumArtBlured.setBackgroundResource(R.drawable.mpbackgrounddefault);
        } else {
            BlurBuilder blurBuilder = new BlurBuilder();
            Bitmap bitmap1 = blurBuilder.blur(getApplicationContext(), BitmapArt);
            Drawable d = new BitmapDrawable(getResources(), bitmap1);
            Drawable d1 = new BitmapDrawable(getResources(), BitmapArt);
            AlbumArtBlured.setBackground(d);
            AlbumArt_imageview.setBackground(d1);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        songArrayList=mainActivity.return_ArrayList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateSeekbar=new Thread(){
            @Override
            public void run() {
                int totalDur=Player.mp.getDuration();
                int currentPos=0;
                seekBarAS.setMax(totalDur);
                while(currentPos<totalDur){
                    try {
                        sleep(500);
                        currentPos=MainActivity.seekBarMA.getProgress();
                        seekBarAS.setProgress(currentPos);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(Player.mp.isPlaying())
            try {
                updateSeekbar.start();
            }catch (Exception e){
                e.printStackTrace();
                while(updateSeekbar.isAlive()){
                    updateSeekbar.interrupt();
                }
            }
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateSeekbar.interrupt();
    }

}
