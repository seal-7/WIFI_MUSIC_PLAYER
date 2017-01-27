package com.example.rajtalekar.jogman10;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class GroupPlay extends AppCompatActivity {
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;
    ArrayList<String> peerNameList;
    EditText ed;
    public static String checkString=new String();
    public static FileServerAsyncTask fileServerAsyncTask;
    ArrayList<String> deviceAddressList;
    ArrayList<song> songArraylist;
    public static WifiP2pInfo infor;
    WifiP2pDeviceList p2pDeviceList;
    ListView lv;
    public static ListView lv1;
    public static TextView statusText;
    ArrayList<String> songStringList;
    public static Button reciveButton;
    public static Boolean isClient = true;
    public static String clientAddress = null;
    public static MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_play);
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
        statusText = (TextView) findViewById(R.id.statusText);
        clientAddress = new String();
        lv = (ListView) findViewById(R.id.listView);
        lv1= (ListView) findViewById(R.id.listView1);
        songArraylist = MainActivity.songList;
        songStringList = new ArrayList<>();
        for (int i = 0; i < songArraylist.size(); i++) {
            songStringList.add(songArraylist.get(i).getTitle());
        }
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, songStringList);
//        lv1.setAdapter(adp);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri trackUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        MainActivity.songList.get(position).getID());
                if (isClient) {
                    if (!fileServerAsyncTask.isCancelled())
                        while (!fileServerAsyncTask.isCancelled())
                            fileServerAsyncTask.cancel(true);
                    new sendString(getApplicationContext(), songArraylist.get(position).getTitle(), infor.groupOwnerAddress.getHostAddress()).execute();
                    fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                    fileServerAsyncTask.execute();
                    Log.i("LISTENIG", "TRUE");
                    //  Toast.makeText(getApplicationContext(), "Server address:" + infor.groupOwnerAddress.getHostAddress(), Toast.LENGTH_SHORT).show();
                } else {
                    if (!fileServerAsyncTask.isCancelled())
                        while (!fileServerAsyncTask.isCancelled())
                            fileServerAsyncTask.cancel(true);
                    //    Toast.makeText(getApplicationContext(), "Client Adress:" + clientAddress, Toast.LENGTH_SHORT).show();
                    new sendString(getApplicationContext(), songArraylist.get(position).getTitle(), clientAddress).execute();
                    fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                    fileServerAsyncTask.execute();
                    Log.i("LISTENIG", "TRUE");
                }

                play_song(trackUri, getApplicationContext());
            }
        });
        reciveButton = (Button) findViewById(R.id.b2);
        reciveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent serviceIntent = new Intent(getApplicationContext(), FileTransferService.class);
//
//                serviceIntent.setAction(FileTransferService.ACTION_SEND_FILE);
//                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_ADDRESS,
//                        infor.groupOwnerAddress.getHostAddress());
//                serviceIntent.putExtra(FileTransferService.EXTRAS_GROUP_OWNER_PORT, 8988);
//                startService(serviceIntent);
//
//                while(fileServerAsyncTask.isCancelled()==false){
//                    fileServerAsyncTask.cancel(true);
//                }
//                try {
//                    if (fileServerAsyncTask.isCancelled()) {
//                        Toast.makeText(getApplicationContext(), "Recive Cancled", Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception e){
//                    Log.e("FileServerAsync","Is Null");
//                }
                if (isClient) {
                    if (!fileServerAsyncTask.isCancelled())
                        while (!fileServerAsyncTask.isCancelled())
                            fileServerAsyncTask.cancel(true);
                    new sendString(getApplicationContext(), Integer.toString(mediaPlayer.getCurrentPosition()), infor.groupOwnerAddress.getHostAddress()).execute();
                    fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                    fileServerAsyncTask.execute();
                    Log.i("LISTENIG", "TRUE");
                    //  Toast.makeText(getApplicationContext(), "Server address:" + infor.groupOwnerAddress.getHostAddress(), Toast.LENGTH_SHORT).show();
                } else {
                    if (!fileServerAsyncTask.isCancelled())
                        while (!fileServerAsyncTask.isCancelled())
                            fileServerAsyncTask.cancel(true);
                    //  Toast.makeText(getApplicationContext(), "Client Adress:" + clientAddress, Toast.LENGTH_SHORT).show();
                    new sendString(getApplicationContext(), Integer.toString(mediaPlayer.getCurrentPosition()), clientAddress).execute();
                    Log.i("LISTENIG", "TRUE");
                    fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                    fileServerAsyncTask.execute();
                }
            }
        });
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                for (WifiP2pDevice device : p2pDeviceList.getDeviceList()) {
                    if (peerNameList.get(position).equals(device.deviceName)) {
                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                //success logic
                                //  Toast.makeText(getApplicationContext(), "Connected to :" + peerNameList.get(position), Toast.LENGTH_SHORT).show();
                                mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {
                                    @Override
                                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
//                                        Toast.makeText(getApplicationContext(), "Connection info available 2", Toast.LENGTH_SHORT).show();
                                        infor = info;
                                        if (info.groupFormed && info.isGroupOwner) {
                                            fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                                            fileServerAsyncTask.execute();
                                            Log.i("LISTENIG", "TRUE");
                                            isClient = false;
                                            //   Toast.makeText(getApplicationContext(), "This device Group Owner", Toast.LENGTH_SHORT).show();
                                            lv.setVisibility(View.INVISIBLE);
                                            lv1.setAdapter(adp);
                                        } else if (info.groupFormed) {
                                            //   Toast.makeText(getApplicationContext(), "This device Client", Toast.LENGTH_SHORT).show();
                                            new sendString(getApplicationContext(), "Device Connected", infor.groupOwnerAddress.getHostAddress()).execute();
                                            fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                                            fileServerAsyncTask.execute();
                                            Log.i("LISTENIG", "TRUE");
                                            isClient = true;
                                            lv.setVisibility(View.INVISIBLE);
                                            lv1.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });

                            }

                            @Override
                            public void onFailure(int reason) {
                                //failure logic
                                Toast.makeText(getApplicationContext(), "Connection failed due to reason :" + reason, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                }
            }
        });

        reciveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < MainActivity.songList.size(); i++) {
                    if (isClient) {
                        if (!fileServerAsyncTask.isCancelled())
                            while (!fileServerAsyncTask.isCancelled())
                                fileServerAsyncTask.cancel(true);
                        new sendString(getApplicationContext(), songArraylist.get(i).getTitle(), infor.groupOwnerAddress.getHostAddress()).execute();
                        fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                        fileServerAsyncTask.execute();
                        Log.i("LISTENIG", "TRUE");
                        // Toast.makeText(getApplicationContext(), "Server address:" + infor.groupOwnerAddress.getHostAddress(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!fileServerAsyncTask.isCancelled())
                            while (!fileServerAsyncTask.isCancelled())
                                fileServerAsyncTask.cancel(true);
                        //   Toast.makeText(getApplicationContext(), "Client Adress:" + clientAddress, Toast.LENGTH_SHORT).show();
                        new sendString(getApplicationContext(), songArraylist.get(i).getTitle(), clientAddress).execute();
                        fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                        fileServerAsyncTask.execute();
                        Log.i("LISTENIG", "TRUE");
                    }
                    while (!checkString.equals("got")) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("Waiting", "....");
                    }
                }
                for (int i = 0; i < 3; i++) {
                    if (isClient) {
                        if (!fileServerAsyncTask.isCancelled())
                            while (!fileServerAsyncTask.isCancelled())
                                fileServerAsyncTask.cancel(true);
                        new sendString(getApplicationContext(), "done", infor.groupOwnerAddress.getHostAddress()).execute();
                        fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                        fileServerAsyncTask.execute();
                        Log.i("LISTENIG", "TRUE");
                        //Toast.makeText(getApplicationContext(), "Server address:" + infor.groupOwnerAddress.getHostAddress(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!fileServerAsyncTask.isCancelled())
                            while (!fileServerAsyncTask.isCancelled())
                                fileServerAsyncTask.cancel(true);
                        //Toast.makeText(getApplicationContext(), "Client Adress:" + clientAddress, Toast.LENGTH_SHORT).show();
                        new sendString(getApplicationContext(), "done", clientAddress).execute();
                        fileServerAsyncTask = new FileServerAsyncTask(getApplicationContext());
                        fileServerAsyncTask.execute();
                        Log.i("LISTENIG", "TRUE");
                    }
                }
            }
        });

    }
    public static void acknoledgment_send(Context context,String str){
        if (isClient) {
            if (!fileServerAsyncTask.isCancelled())
                while (!fileServerAsyncTask.isCancelled())
                    fileServerAsyncTask.cancel(true);
            new sendString(context,str, infor.groupOwnerAddress.getHostAddress()).execute();
            fileServerAsyncTask = new FileServerAsyncTask(context);
            fileServerAsyncTask.execute();
            Log.i("LISTENIG", "TRUE");
            //Toast.makeText(context, "Server address:" + infor.groupOwnerAddress.getHostAddress(), Toast.LENGTH_SHORT).show();
        } else {
            if (!fileServerAsyncTask.isCancelled())
                while (!fileServerAsyncTask.isCancelled())
                    fileServerAsyncTask.cancel(true);
            //Toast.makeText(context, "Client Adress:" + clientAddress, Toast.LENGTH_SHORT).show();
            new sendString(context,str, clientAddress).execute();
            Log.i("LISTENIG", "TRUE");
            fileServerAsyncTask = new FileServerAsyncTask(context);
            fileServerAsyncTask.execute();
        }
    }
    public static void play_song(Uri trackUri,Context context){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer= MediaPlayer.create(context, trackUri);
        mediaPlayer.start();
    }
    public static void seek_song(int pos,Context c){
    try {
        mediaPlayer.seekTo(pos);
        Toast.makeText(c, ""+mediaPlayer.getCurrentPosition(), Toast.LENGTH_SHORT).show();
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    public void discover(View v) {
        lv.setVisibility(View.VISIBLE);
        peerNameList = new ArrayList<String>();
        deviceAddressList = new ArrayList<String>();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(getApplicationContext(), "Discovered Peers", Toast.LENGTH_SHORT).show();
                Log.i("DISCOVERED", "PEERS");
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), "Failed due to reason :" + reasonCode, Toast.LENGTH_SHORT).show();
            }
        });
        if (mManager != null) {
            mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {
                    Toast.makeText(getApplicationContext(), "Peers available", Toast.LENGTH_SHORT).show();
                    Log.d("Peers","Availabel");
                    for (WifiP2pDevice device : peers.getDeviceList()) {
                        peerNameList.add(device.deviceName);
                        Toast.makeText(getApplicationContext(), device.deviceName, Toast.LENGTH_SHORT).show();
                        deviceAddressList.add(device.deviceAddress);
                        Log.d("device name", device.deviceName);
                        Log.d("device address", device.deviceAddress);
                    }
                    p2pDeviceList = peers;
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, peerNameList);
                    lv.setAdapter(adp);
                }
            });
        }
    }

    public static class sendString extends AsyncTask {
        Context context;
        String string;
        static String host = infor.groupOwnerAddress.getHostAddress();

        public sendString(Context c, String s, String recieverAddress) {
            this.context = c;
            string = s;
            host = recieverAddress;
        }
        public sendString(Context c,String s){
            this.context = c;
            string = s;
        }
        @Override
        protected Object doInBackground(Object[] params) {

            Socket socket = new Socket();

            int port = 8988;
            DataOutputStream stream = null;
            try {
                Log.d("SendString to ", host);
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), 5000);
                Log.d("Connected to client:", " " + socket.isConnected());
                stream = new DataOutputStream(socket.getOutputStream());
                stream.writeUTF(string);
            } catch (IOException e) {
                Log.e("FileTransferService", e.getMessage());
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    if (socket.isConnected()) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return null;
        }

        protected void onPostExecute(String result) {

        }
    }

    public static class FileServerAsyncTask extends AsyncTask<Void, Void, String> {

        private Context context;
        public static final String ExtraString = new String();
        public  static MediaPlayer mediaPlayer = new MediaPlayer();
        public static FileServerAsyncTask fileServerAsyncTask=new FileServerAsyncTask();
        public static ArrayList<String> recivedList=new ArrayList<>();
        private boolean done=false;

        public FileServerAsyncTask(Context context) {
            this.context = context;
        }
        public FileServerAsyncTask(){

        }

        @Override
        protected void onPreExecute() {
            //Toast.makeText(context, "PRE EXECUTE", Toast.LENGTH_SHORT).show();
            Log.i("PRE EXECUTE", "TRUE");
        }


        @Override
        protected String doInBackground(Void... params) {
            ServerSocket serverSocket = null;
            Socket client = null;

            DataInputStream inputstream = null;

            try {
                serverSocket = new ServerSocket(8988);
                client = serverSocket.accept();
                Log.e("Reciving", ">>>>>>>>");
                clientAddress = client.getInetAddress().getHostAddress();
                inputstream = new DataInputStream(client.getInputStream());
                String str = inputstream.readUTF();
                serverSocket.close();
                return str;
            } catch (IOException e) {
                Log.e("FileServerAsyncTask", e.getMessage());
                return null;
            } finally {
                if (inputstream != null) {
                    try {
                        inputstream.close();
                    } catch (IOException e) {
                        Log.e("FileServerAsyncTask", e.getMessage());
                    }
                }
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        Log.e("FileServerAsyncTask", e.getMessage());
                    }
                }
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        Log.e("FileServerAsyncTask", e.getMessage());
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                GroupPlay.checkString=result;

                if (result.equals("Device Connected")){
                    Toast.makeText(context,"Device Connected",Toast.LENGTH_SHORT).show();
                }
                if(result.equals("done")){
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, recivedList);
                    GroupPlay.lv1.setAdapter(adp);
                    GroupPlay.acknoledgment_send(context,"done");
                    done=true;
                }
                if(!done)
                for(int i=0;i<MainActivity.songList.size();i++) {
                    if (result.equals(MainActivity.songList.get(i).getTitle())) {
                        GroupPlay.statusText.setText(result);
                        recivedList.add(result);
                        GroupPlay.acknoledgment_send(context,result);
                        Uri trackUri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                MainActivity.songList.get(i).getID());
                        GroupPlay.play_song(trackUri,context);
                        break;
                    }
                }
                if(!result.equals("got")&&!result.equals("Device Connected"))
                    GroupPlay.acknoledgment_send(context,"got");

                try {
                    int x=Integer.parseInt(result);
                    GroupPlay.seek_song(x,context);
                }catch (Exception e){
                    e.printStackTrace();
                }

//                 if (!fileServerAsyncTask.isCancelled())
//                    while (!fileServerAsyncTask.isCancelled())
//                        fileServerAsyncTask.cancel(true);
//                new sendString(context,"Recived").execute();
//                fileServerAsyncTask = new FileServerAsyncTask(context);
//                fileServerAsyncTask.execute();
            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

}
