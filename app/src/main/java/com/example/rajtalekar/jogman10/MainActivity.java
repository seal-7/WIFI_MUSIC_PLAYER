package com.example.rajtalekar.jogman10;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity{
    ListView songView;
    AlbumSongs albumSongs=new AlbumSongs();
   static ArrayList<song> songList;
    public static ArrayList<String> nowPlaying;
    Player player;
    public static Context context;
    static String[] stringsonglist;
    HearAlone hearAlone=new HearAlone();
    public static long AlbumId;
    ImageView AlbumArt_imageview;
    public static Boolean playing=false;
    private ArtistSongs artistSongs=new ArtistSongs();
    public static ListView listViewMA;
    private Uri trackUri;
    RelativeLayout AlbumArtBlured;
    public static SeekBar seekBarMA;
    TextView Song_TitleTv;
    public void Set_Playing(Boolean playing){
        this.playing=playing;
    }

    public ArrayList<song> return_ArrayList(){
        return songList;
    }
    public String[] return_SongList(){
        return stringsonglist;
    }
    public static String SongTitle=new String();
    public void set_SongDetailes(String newSongTitle,long newAlbumId){
        SongTitle=newSongTitle;
        AlbumId=newAlbumId;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(android.R.color.transparent);
        setSupportActionBar(toolbar);
        context=getApplicationContext();
        songView = (ListView) findViewById(R.id.listView);
        songList = new ArrayList<song>();
        player=new Player();
        AlbumArtBlured=(RelativeLayout)findViewById(R.id.relativeLayout3);
        final ImageButton PlayPause=(ImageButton)findViewById(R.id.play);
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
        AlbumArt_imageview=(ImageView)findViewById(R.id.AlbumArt_imageview);
        getSongList();
        Collections.sort(songList, new Comparator<song>() {
            public int compare(song a, song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing == true) {
                    PlayPause.setBackgroundResource(R.drawable.play);
                    albumSongs.Set_Playing(false);
                    artistSongs.Set_Playing(false);
                    hearAlone.Set_playing(false);
                    playing = false;
                } else {
                    PlayPause.setBackgroundResource(R.drawable.pause);
                    albumSongs.Set_Playing(true);
                    artistSongs.Set_Playing(true);
                    hearAlone.Set_playing(true);
                    playing = true;
                }
                player.playPause();

            }
        });
        listViewMA = (ListView) findViewById(R.id.listViewSlideMainActivity);
        seekBarMA=(SeekBar)findViewById(R.id.seekBarMA);
        Song_TitleTv=(TextView)findViewById(R.id.song_Title);
        nowPlaying=player.getNowPlaying();
        seekBarMA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        //************Sliding Drawer Visibility*****************

        final SlidingDrawer sdLeft=(SlidingDrawer)findViewById(R.id.slidingDrawer);
        final SlidingDrawer sdRight=(SlidingDrawer)findViewById(R.id.slidingDrawer1);
        sdRight.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                sdLeft.setVisibility(View.GONE);
                Song_TitleTv.setSelected(true);
                sdRight.setClickable(true);
            }
        });
        sdRight.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                sdRight.setClickable(false);
            }
        });

        sdLeft.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                sdRight.setVisibility(View.GONE);
                sdLeft.setClickable(true);
            }
        });
        sdLeft.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                sdRight.setVisibility(View.VISIBLE);
                sdLeft.setClickable(false);
            }
        });
        sdLeft.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollStarted() {
                sdRight.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollEnded() {
                if(sdLeft.isOpened())
                sdRight.setVisibility(View.GONE);


            }
        });
        sdRight.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollStarted() {
                sdLeft.setVisibility(View.VISIBLE);
                Song_TitleTv.setText(SongTitle);


//                BlurBuilder blurBuilder=new BlurBuilder();
//                Bitmap   bitmap1=blurBuilder.blur(getApplicationContext(),BitmapArt);

                if(playing){
                    PlayPause.setBackgroundResource(R.drawable.pause);
                }
                else{
                    PlayPause.setBackgroundResource(R.drawable.play);
                }
                updateSdRight();
            }

            @Override
            public void onScrollEnded() {

            }
        });

        //************Sliding Drawer Visibility*****************

        stringsonglist=new String[songList.size()];
            for (int i = 0; i < songList.size(); i++) {
                stringsonglist[i]=songList.get(i).getTitle();
            }


//        ArrayAdapter<String> adp=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,stringsonglist);
//        listView.setAdapter(adp);
        listViewMA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nowPlaying=HearAlone.nowPlaying;
                for(int i=0;i<songList.size();i++){
                    if(nowPlaying.get(position).equals(songList.get(i).getTitle())) {
                        SongTitle= songList.get(i).getTitle();
                        hearAlone.Set_SongDetailes(SongTitle, songList.get(i).getArtist(), songList.get(i).getAlbumID());
                        set_SongDetailes(SongTitle, songList.get(i).getAlbumID());
                        albumSongs.set_SongDetailes(SongTitle, songList.get(i).getAlbumID());
                        artistSongs.set_SongDetailes(SongTitle, songList.get(i).getAlbumID());
                        trackUri = ContentUris.withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                songList.get(i).getID());
                        Player player = new Player(trackUri, getApplicationContext());
                        player.play(nowPlaying,position);
                        hearAlone.Set_playing(true);
                        Set_Playing(true);
                        albumSongs.Set_Playing(true);
                        artistSongs.Set_Playing(true);
                        break;
                    }
                }
            }
        });
      set_theme();
    }
    public void updateSdRight(){
        Song_TitleTv.setText(SongTitle);
        Bitmap BitmapArt = hearAlone.getAlbumart(AlbumId,getApplicationContext(),getResources());

        if(BitmapArt==null){
            AlbumArtBlured.setBackgroundResource(R.drawable.mpbackgrounddefault);
        }
        else {
            BlurBuilder blurBuilder=new BlurBuilder();
            Bitmap bitmap1=blurBuilder.blur(getApplicationContext(),BitmapArt);
            Drawable d = new BitmapDrawable(getResources(), bitmap1);
            Drawable d1=new BitmapDrawable(getResources(),BitmapArt);
            AlbumArtBlured.setBackground(d);
            AlbumArt_imageview.setBackground(d1);
        }


    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        set_theme();
    }

    void set_theme(){
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
//        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mpb3);
//        BlurBuilder blurBuilder=new BlurBuilder();
//        bitmap1=blurBuilder.blur(getApplicationContext(),bitmap);
//        Drawable d = new BitmapDrawable(getResources(), bitmap1);
        rl.setBackgroundResource(R.drawable.mpbackground3);
        }

//    public Bitmap getAlbumart(Long album_id)
//    {
//        Bitmap songImage=null;
//        Uri sArtworkUri=Uri.parse("content://media/external/audio/albumart");
//        Uri uri= ContentUris.withAppendedId(sArtworkUri, AlbumId);
//        ContentResolver res=getApplicationContext().getContentResolver();
//        InputStream in=null;
//        try{
//            in=res.openInputStream(uri);
//            songImage=BitmapFactory.decodeStream(in);
//        } catch (FileNotFoundException e) {
//            songImage=BitmapFactory.decodeResource(getResources(),R.drawable.my_drawer);
//            e.printStackTrace();
//        }
//        return songImage;
//    }
    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        try {
            if (musicCursor != null && musicCursor.moveToFirst()) {
                //get columns
                int titleColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.TITLE);
                int idColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media._ID);
                int artistColumn = musicCursor.getColumnIndex
                        (android.provider.MediaStore.Audio.Media.ARTIST);
                int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int albumIdColumn=musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                //add songs to list
                do {
                    long thisId = musicCursor.getLong(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    String thisAlbum= musicCursor.getString(albumColumn);
                    long thisAlbumId=musicCursor.getLong(albumIdColumn);
                    songList.add(new song(thisId,thisTitle,thisArtist,thisAlbum,thisAlbumId));
                }
                while (musicCursor.moveToNext());
            }
        }catch (Exception e){}
    }

    @Nullable
    public void hearAlone(View v){
        Intent in=new Intent(this,HearAlone.class);
        startActivity(in);
//        overridePendingTransition(R.anim.rightin,R.anim.rightout);
    }

    public void groupPlay(View v){
        Intent intent=new Intent(this,GroupPlay.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0,1,0,"Themes");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

}