package com.example.rajtalekar.jogman10;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class HearAlone extends AppCompatActivity implements Section_1.OnFragmentInteractionListener,Section_2.OnFragmentInteractionListener,Section_3.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public  static ListView listViewHA;
    ArrayList<song> songArrayList;
    ArrayAdapter<String> adp;
    Bitmap BitmapArt;
    ImageView AlbumArt_imageview;
    AlbumSongs albumSongs=new AlbumSongs();
    TextView song_Title;
    TextView artist_Title;
    RelativeLayout AlbumArtBlured;
    Player player=new Player();
    public static SeekBar seekBarHA;
    public static  ArrayList<String> nowPlaying;
    public static String SongTitle_HearAlone=new String();
    public static String ArtistTitle_HearAlone=new String();
    public static long AlbumId=0;
    public static boolean playing=false;
    private ArtistSongs artistSongs=new ArtistSongs();
    private Uri trackUri;
    public static ImageButton PlayPause;
    public static boolean activeHA;
    public static Thread updateSeekbar;

    public void Set_playing(boolean playing){
        this.playing=playing;
    }

    public void Set_SongDetailes(String newSongTitle,String newArtistTitle,long newAlbumId){
        SongTitle_HearAlone=newSongTitle;
        ArtistTitle_HearAlone=newArtistTitle;
        AlbumId=newAlbumId;
        Log.e("SONG SET SUCCES",SongTitle_HearAlone);
    }
    public void Set_nowPlaying(ArrayList<String> nowPlaying){
        this.nowPlaying=nowPlaying;
    }
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hear_alone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        AlbumArtBlured=(RelativeLayout)findViewById(R.id.relativeLayout3);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        final MainActivity mainActivity=new MainActivity();
        song_Title=(TextView)findViewById(R.id.song_Title);
        artist_Title=(TextView)findViewById(R.id.artist_Title);
        songArrayList=mainActivity.return_ArrayList();
        AlbumArt_imageview=(ImageView)findViewById(R.id.AlbumArt_imageview);
        nowPlaying=player.getNowPlaying();
        listViewHA = (ListView) findViewById(R.id.listViewSlideHearAlone);
        seekBarHA=(SeekBar)findViewById(R.id.seekBarHearAlone);
        if(nowPlaying!=null) {
            ArrayAdapter<String> adp1=new ArrayAdapter<String>(getApplicationContext(),R.layout.row_layout,R.id.textView,nowPlaying);
            listViewHA.setAdapter(adp1);
            ArrayAdapter<String> adp2=new ArrayAdapter<String>(MainActivity.context,R.layout.row_layout,R.id.textView,nowPlaying);
            MainActivity.listViewMA.setAdapter(adp2);
        }
        final SlidingDrawer sdLeft=(SlidingDrawer)findViewById(R.id.slidingDrawer);
        final SlidingDrawer sdRight=(SlidingDrawer)findViewById(R.id.slidingDrawer1);
        PlayPause=(ImageButton)findViewById(R.id.play);
        final ImageButton Next=(ImageButton)findViewById(R.id.next);
        final ImageButton Prev=(ImageButton)findViewById(R.id.prev);
        PlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing == true) {
                    PlayPause.setBackgroundResource(R.drawable.play);
                    albumSongs.Set_Playing(false);
                    artistSongs.Set_Playing(false);
                    mainActivity.Set_Playing(false);
                    playing = false;
                } else {
                    PlayPause.setBackgroundResource(R.drawable.pause);
                    albumSongs.Set_Playing(true);
                    artistSongs.Set_Playing(true);
                    mainActivity.Set_Playing(true);
                    playing = true;
                }
                player.playPause();

            }
        });
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
        seekBarHA.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


        sdRight.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                sdLeft.setVisibility(View.GONE);
                song_Title.setSelected(true);
                sdRight.setClickable(true);
            }
        });
        sdRight.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                sdLeft.setVisibility(View.VISIBLE);
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
        listViewHA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<songArrayList.size();i++){
                    if(nowPlaying.get(position).equals(songArrayList.get(i).getTitle())){
                        SongTitle_HearAlone= songArrayList.get(i).getTitle();
                        Set_SongDetailes(SongTitle_HearAlone, songArrayList.get(i).getArtist(), songArrayList.get(i).getAlbumID());
                        mainActivity.set_SongDetailes(SongTitle_HearAlone, songArrayList.get(i).getAlbumID());
                        albumSongs.set_SongDetailes(SongTitle_HearAlone, songArrayList.get(i).getAlbumID());
                        artistSongs.set_SongDetailes(SongTitle_HearAlone, songArrayList.get(i).getAlbumID());
                        trackUri = ContentUris.withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                songArrayList.get(i).getID());
                        Player player = new Player(trackUri, getApplicationContext());
                        player.play(nowPlaying,position);
                        Set_playing(true);
                        mainActivity.Set_Playing(true);
                        albumSongs.Set_Playing(true);
                        artistSongs.Set_Playing(true);
                        onStart();
                        break;
                    }
                }
            }
        });
        set_theam();
    }
    public void updateSdRight(){
        song_Title.setText(SongTitle_HearAlone);
        artist_Title.setText(ArtistTitle_HearAlone);
        BitmapArt = getAlbumart(AlbumId, getApplicationContext(), getResources());
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

    public void set_theam(){
        CoordinatorLayout rl = (CoordinatorLayout) findViewById(R.id.main_content);

//            bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.mpb3);
//            BlurBuilder blurBuilder=new BlurBuilder();
//            bitmap1=blurBuilder.blur(getApplicationContext(),bitmap);
//            Drawable d = new BitmapDrawable(getResources(), bitmap1);
            rl.setBackgroundResource(R.drawable.mpbackground3);
    }

    public static Bitmap getAlbumart(Long album_id,Context context,Resources resources)
    {
        Bitmap songImage=null;
        Uri sArtworkUri=Uri.parse("content://media/external/audio/albumart");
        Uri uri=ContentUris.withAppendedId(sArtworkUri, AlbumId);
        ContentResolver res=context.getContentResolver();
        InputStream in=null;
        try{
            in=res.openInputStream(uri);
            songImage=BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            songImage=BitmapFactory.decodeResource(resources,R.drawable.mpbackgrounddefault);
            e.printStackTrace();
        }
        return songImage;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hear_alone, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hear_alone, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new Section_1();
                case 1:
                    return new Section_2();
                case 2:
                    return new Section_3();
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALL SONGS";
                case 1:
                    return "ARTIST";
                case 2:
                    return "ALBUM";
            }
            return null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        activeHA=true;
        updateSeekbar=new Thread(){
            @Override
            public void run() {
                int totalDur=Player.mp.getDuration();
                int currentPos=0;
                seekBarHA.setMax(totalDur);
                while(currentPos<totalDur){
                    try {
                        sleep(500);
                        currentPos=MainActivity.seekBarMA.getProgress();
                        seekBarHA.setProgress(currentPos);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(Player.mp.isPlaying())
        updateSeekbar.start();

    }
    @Override
    protected void onStop() {
        super.onStop();
        activeHA=false;
        updateSeekbar.interrupt();
    }
}
