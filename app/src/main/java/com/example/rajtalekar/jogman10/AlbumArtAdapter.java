package com.example.rajtalekar.jogman10;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Raj Talekar on 4/3/2016.
 */
public class AlbumArtAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    ArrayList<String> mySongList;
    Context context;
    TextView tv1;
    ImageView imageView;
    MainActivity mainActivity=new MainActivity();
    ArrayList<song> songArrayList=mainActivity.return_ArrayList();
    long AlbumId;
    public  AlbumArtAdapter(Context context, ArrayList<String> mySongList){
        layoutInflater=layoutInflater.from(context);
        this.context=context;
        this.mySongList=mySongList;
    }
    @Override
    public int getCount() {
        return mySongList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout=(LinearLayout)layoutInflater.inflate(R.layout.row_layout_album, parent, false);
        tv1=(TextView)linearLayout.findViewById(R.id.textView3);
        imageView=(ImageView)linearLayout.findViewById(R.id.AlbumArt_imageview);
        for (int i=0;i<songArrayList.size();i++){
            if(songArrayList.get(i).getAlbumArt().equals(mySongList.get(position))){
                AlbumId=songArrayList.get(i).getAlbumID();
                break;
            }
        Bitmap    BitmapArt=getAlbumart(AlbumId);
//                BlurBuilder blurBuilder=new BlurBuilder();
//                Bitmap   bitmap1=blurBuilder.blur(getApplicationContext(),BitmapArt);
            if(BitmapArt==null){
                //AlbumArtBlured.setBackgroundResource(R.drawable.mpbackground3);
                Log.e("NO ALBUM ART", String.valueOf(AlbumId));
            }
            else {

                Drawable d = new BitmapDrawable(Resources.getSystem(), BitmapArt);
                imageView.setBackground(d);

            }

        }

        return null;
    }
    public Bitmap getAlbumart(Long album_id)
    {
        Bitmap songImage=null;
        Uri sArtworkUri=Uri.parse("content://media/external/audio/albumart");
        Uri uri= ContentUris.withAppendedId(sArtworkUri, album_id);
        ContentResolver res=context.getContentResolver();
        InputStream in=null;
        try{
            in=res.openInputStream(uri);
            songImage= BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            //songImage=BitmapFactory.decodeResource(getResources(),R.drawable.my_drawer);
            e.printStackTrace();
        }
        return songImage;
    }
}
