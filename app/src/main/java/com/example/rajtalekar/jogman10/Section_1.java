package com.example.rajtalekar.jogman10;

import android.content.ContentUris;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Section_1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Section_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section_1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MainActivity mainActivity=new MainActivity();
    HearAlone hearAlone=new HearAlone();
    AlbumSongs albumSongs=new AlbumSongs();
    ArtistSongs artistSongs=new ArtistSongs();
    String[] items;
    ArrayList<song> songArrayList;
    ListView lv;
    static String song_Title_string=new String();
    private Uri trackUri;
//    private MediaPlayer mp;
   public static ArrayList<String> items1;
    public String return_song_Title(){
        return song_Title_string;
    }
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Section_1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section_1.
     */
    // TODO: Rename and change types and number of parameters
    public static Section_1 newInstance(String param1, String param2) {
        Section_1 fragment = new Section_1();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
            songArrayList = mainActivity.return_ArrayList();
            items=new String[songArrayList.size()];
            items = mainActivity.return_SongList();
            items1 = new ArrayList<>();
            Collections.addAll(items1,items);
            View rootView = inflater.inflate(R.layout.fragment_section_1, container, false);
            lv = (ListView) rootView.findViewById(R.id.listView);
            MyAdapter myAdapter = new MyAdapter(getContext(), songArrayList);
            //final ListView hearAloneList=(ListView)getActivity().findViewById(R.id.listViewSlideHearAlone);
            lv.setAdapter(myAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        song_Title_string = songArrayList.get(position).getTitle();
                        hearAlone.Set_SongDetailes(song_Title_string, songArrayList.get(position).getArtist(), songArrayList.get(position).getAlbumID());
                        mainActivity.set_SongDetailes(song_Title_string, songArrayList.get(position).getAlbumID());
                        albumSongs.set_SongDetailes(song_Title_string, songArrayList.get(position).getAlbumID());
                        artistSongs.set_SongDetailes(song_Title_string, songArrayList.get(position).getAlbumID());
                        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, items1);
                        HearAlone.listViewHA.setAdapter(adp);
                        hearAlone.Set_nowPlaying(items1);
                        trackUri = ContentUris.withAppendedId(
                                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                songArrayList.get(position).getID());
                        Player player = new Player(trackUri, getContext());
                        player.play(items1,position);
                        hearAlone.Set_playing(true);
                        mainActivity.Set_Playing(true);
                        albumSongs.Set_Playing(true);
                        artistSongs.Set_Playing(true);
                    ((HearAlone)getActivity()).onStart();
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
