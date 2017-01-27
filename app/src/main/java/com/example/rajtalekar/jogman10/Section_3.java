package com.example.rajtalekar.jogman10;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Section_3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Section_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section_3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lv;
    // TODO: Rename and change types of parameters
    public final static String passalbum=new String();
    private String mParam1;
    private String mParam2;
    ArrayList<String> albumList;
    ArrayList<song> songArrayList;
    MainActivity mainActivity;
    private OnFragmentInteractionListener mListener;

    public Section_3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section_3.
     */
    // TODO: Rename and change types and number of parameters
    public static Section_3 newInstance(String param1, String param2) {
        Section_3 fragment = new Section_3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        albumList=new ArrayList<String>();
        mainActivity=new MainActivity();
        songArrayList=mainActivity.return_ArrayList();
        for (int i = 0; i < songArrayList.size(); i++) {
            if (!albumList.contains(songArrayList.get(i).getAlbumArt())) {
                albumList.add(songArrayList.get(i).getAlbumArt());
            }
        }
        Collections.sort(albumList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_section_3, container, false);
        lv=(ListView)rootView.findViewById(R.id.listView3);
        //AlbumArtAdapter myAdapter=new AlbumArtAdapter(getContext(),albumList);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(getContext(),R.layout.row_layout_album,R.id.textView3,albumList);
        lv.setAdapter(myAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AlbumSongs.class);
                intent.putExtra(passalbum, albumList.get(position));
                startActivity(intent);
               // getActivity().overridePendingTransition(R.anim.rightin, R.anim.rightout);
            }
        });
        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
