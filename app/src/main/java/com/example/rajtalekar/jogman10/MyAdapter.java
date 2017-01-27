package com.example.rajtalekar.jogman10;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Raj Talekar on 2/11/2016.
 */
public class MyAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    ArrayList<song> mySongList;
    Context context;
    TextView tv1,tv2;
  public  MyAdapter(Context context, ArrayList<song> mySongList){
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
        LinearLayout linearLayout=(LinearLayout)layoutInflater.inflate(R.layout.row_layout, parent, false);
        tv1=(TextView)linearLayout.findViewById(R.id.textView);
        tv2=(TextView)linearLayout.findViewById(R.id.textView2);
        tv1.setText(position+1+". "+" "+mySongList.get(position).getTitle().toString());
       if(mySongList.get(position).getArtist().toString().equals("<unknown>")){
           tv2.setText("Unknown Artist");
       }
        else {
           tv2.setText("" + mySongList.get(position).getArtist().toString());
       }
       linearLayout.setTag(position);
        return linearLayout;
    }
}

