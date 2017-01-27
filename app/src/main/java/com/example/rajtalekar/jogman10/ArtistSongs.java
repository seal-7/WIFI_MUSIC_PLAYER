package com.example.rajtalekar.jogman10;

import android.content.ContentUris;
import android.content.Intent;
import android.database.DataSetObserver;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArtistSongs extends AppCompatActivity {
    ImageView AlbumArt_imageview;
    private ArrayList<String> list1;
    private Uri trackUri;
    public static Boolean playing = false;
    public static SeekBar seekBarARS;
    private Thread updateSeekbar;
    public static ImageButton PlayPause;
    HearAlone hearAlone;
    RelativeLayout AlbumArtBlured;
    TextView Song_TitleTv;

    public void Set_Playing(Boolean playing) {
        this.playing = playing;
    }

    Player player = new Player();

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.rightout, R.anim.rightin);
    }

    ArrayList<song> songArrayList;
    ArrayList<Albums> list;
    long albumId;
    public static String SongTitle = new String();

    public void set_SongDetailes(String newSongTitle, long albumId) {
        SongTitle = newSongTitle;
        this.albumId = albumId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_songs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list1 = new ArrayList<String>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        ListView lv = (ListView) findViewById(R.id.listView3);
        Intent intent = getIntent();
        AlbumArtBlured = (RelativeLayout) findViewById(R.id.relativeLayout3);
        AlbumArt_imageview = (ImageView) findViewById(R.id.AlbumArt_imageview);
        final SlidingDrawer slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
        Song_TitleTv = (TextView) findViewById(R.id.song_Title);
        String recivedArtist = intent.getStringExtra(Section_2.passartist);
        final MainActivity mainActivity = new MainActivity();
        hearAlone = new HearAlone();
        final AlbumSongs albumSongs = new AlbumSongs();
        TextView tv = (TextView) findViewById(R.id.textView_ArtistSongs);
        tv.setText(recivedArtist);
        tv.setSelected(true);
        seekBarARS = (SeekBar) findViewById(R.id.seekBar);
        PlayPause = (ImageButton) findViewById(R.id.play);
        final ImageButton Next = (ImageButton) findViewById(R.id.next);
        final ImageButton Prev = (ImageButton) findViewById(R.id.prev);
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
                if (playing == true) {
                    PlayPause.setBackgroundResource(R.drawable.play);
                    playing = false;
                    hearAlone.Set_playing(false);
                    mainActivity.Set_Playing(false);
                    albumSongs.Set_Playing(false);
                } else {
                    PlayPause.setBackgroundResource(R.drawable.pause);
                    playing = true;
                    hearAlone.Set_playing(true);
                    mainActivity.Set_Playing(true);
                    albumSongs.Set_Playing(true);
                }
                player.playPause();

            }
        });
        seekBarARS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    Player.mp.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        songArrayList = mainActivity.return_ArrayList();
        list = new ArrayList<Albums>();

        Bitmap BitmapArt = hearAlone.getAlbumart(albumId, getApplicationContext(), getResources());
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
        for (int i = 0; i < songArrayList.size(); i++) {
            if (recivedArtist.equals(songArrayList.get(i).getArtist())) {
                list.add(new Albums(songArrayList.get(i).getTitle(), songArrayList.get(i).getAlbumID(), songArrayList.get(i).getArtist(), songArrayList.get(i).getID()));
            }
        }
        Collections.sort(list, new Comparator<Albums>() {
            @Override
            public int compare(Albums a, Albums b) {
                return a.getArtist().compareTo(b.getArtist());
            }

        });
        for (int i = 0; i < list.size(); i++) {
            list1.add(list.get(i).getAlbumTitle());
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_layout, R.id.textView, list1);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongTitle = list.get(position).getAlbumTitle();
                albumId = list.get(position).getAlbumId();
                hearAlone.Set_playing(true);
                mainActivity.Set_Playing(true);
                PlayPause.setBackgroundResource(R.drawable.pause);
                albumSongs.Set_Playing(true);
                playing = true;
                hearAlone.Set_SongDetailes(SongTitle, list.get(position).getArtist(), list.get(position).getAlbumId());
                hearAlone.Set_nowPlaying(list1);
                mainActivity.set_SongDetailes(SongTitle, list.get(position).getAlbumId());
                albumSongs.set_SongDetailes(SongTitle, list.get(position).getAlbumId());
                trackUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        list.get(position).getSongId());
                Player player = new Player(trackUri, getApplicationContext());
                player.play(list1, position);
                updateSdRight();
                onStart();
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
    }
    public void updateSdRight(){
        Song_TitleTv.setText(SongTitle);
        Bitmap BitmapArt = hearAlone.getAlbumart(albumId, getApplicationContext(), getResources());
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
    protected void onStart() {
        super.onStart();
        updateSeekbar=new Thread(){
            @Override
            public void run() {
                try {
                    int totalDur = Player.mp.getDuration();
                    int currentPos = 0;
                    seekBarARS.setMax(totalDur);
                    while (currentPos < totalDur) {

                        sleep(500);
                        currentPos = MainActivity.seekBarMA.getProgress();
                        seekBarARS.setProgress(currentPos);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        if (Player.mp.isPlaying())
            try {
                updateSeekbar.start();
            }catch (Exception e){
                e.printStackTrace();
                while(updateSeekbar.isAlive())
                updateSeekbar.interrupt();
            }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

