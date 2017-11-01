package com.example.mukeshsharma.mymusic;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukeshsharma.mymusic.adapters.RecyclerAdapter;


import java.io.File;
import java.util.ArrayList;
import java.util.jar.Manifest;

import static com.example.mukeshsharma.mymusic.NotifGenerator.updateContents;
import static com.example.mukeshsharma.mymusic.SecondScreen.shuffFlag;
import static com.example.mukeshsharma.mymusic.SecondScreen.shuffle;
import static com.example.mukeshsharma.mymusic.SecondScreen.shuffleposi;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        Log.d(TAG, "!!!!!!!!onQueryTextChange: "+ newText);
        ArrayList<Song> newList=new ArrayList<>();
        ArrayList<String> newPosList=new ArrayList<>();
        for(Song sng:playlist)
        {
            String name=sng.getTitle().toLowerCase();
            if(name.contains(newText))
            {

                Log.d(TAG, "!!!!!!0!0!0!onQueryTextChange: ");
                newList.add(sng);
                newPosList.add(sng.getLocation());
                Log.d(TAG, "11111111111111111onQueryTextChange: "+ newPosList);
            }
        }
        Log.d(TAG, "!!!!!!!!onQueryTextChange: "+ newList);
        ArrayList<String> newLocArray=recyclerAdapter.filterSearch(newList,newPosList);
        return true;
    }
    SearchView searchView ;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Song> playlist;
    RecyclerView rvPlaylist;
    public static ImageView ivArtist,currArtist1stScreen;
    ImageButton prev,play,next;
   public static TextView nowPlaying1stScreen,duration1stScreen;
    static ArrayList<String> locArray,idArray;
    static MediaPlayer mediaPlayer=null;
    static int seekPos;
    static int j;
    int i;
    LinearLayoutManager mLM;
    static  ArrayList<String> titleArray;
    ArrayList<String> artistArray,durationArray;
    DividerItemDecoration mDividerItemDecoration;

public static final String TAG="YO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locArray=new ArrayList<>();
        idArray=new ArrayList<>();
        titleArray=new ArrayList<>();
        artistArray=new ArrayList<>();
        durationArray=new ArrayList<>();
        ivArtist=(ImageView)findViewById(R.id.ivArtist);
        prev=(ImageButton)findViewById(R.id.btnPrevious);
        play=(ImageButton)findViewById(R.id.btnPlayPause);
        next=(ImageButton)findViewById(R.id.btnNext);
        currArtist1stScreen=(ImageView)findViewById(R.id.ivCurrArtist1stSreen);
        nowPlaying1stScreen=(TextView)findViewById(R.id.tvCurrSong1stScreen);
        duration1stScreen=(TextView)findViewById(R.id.tvCurrDuration1stScreen);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        rvPlaylist = (RecyclerView) findViewById(R.id.songList);
        playlist = new ArrayList<Song>();
        ActivityCompat.requestPermissions(this,new String[]{
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                345);

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getMusic();
        }else
        {
            Log.d(TAG, "!!!!!!!onCreate:Denied");
        }
        mLM=new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerAdapter(playlist,locArray, this);
        rvPlaylist.setLayoutManager(mLM);
        rvPlaylist.setAdapter(recyclerAdapter);
        registerForContextMenu(rvPlaylist);
        mDividerItemDecoration = new DividerItemDecoration(rvPlaylist.getContext(),
                mLM.getOrientation());
        rvPlaylist.addItemDecoration(mDividerItemDecoration);

        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
             //   NotifGenerator.openActivityNotif(MainActivity.this);
                NotifGenerator.bigNotifs(MainActivity.this);
                Log.d(TAG, "!!!!!!!!!!onItemClick: "+ " " + playlist.get(pos).getTitle().toString()+ " "+String.valueOf(Float.valueOf(playlist.get(pos).getDuration())/6000) );
                nowPlaying1stScreen.setText(playlist.get(pos).getTitle().toString());
                duration1stScreen.setText(String.valueOf(String.format("%.2f",Float.valueOf(playlist.get(pos).getDuration())/60000))+" Min");
                if(mediaPlayer!=null){
                    mediaPlayer.release();
                }
                Uri uri=Uri.parse(locArray.get(pos));
                Log.d(TAG, "!!!!!!!onItemClick: "+ locArray.get(pos));
                mediaPlayer=MediaPlayer.create(MainActivity.this,uri);
                play.setBackgroundResource(0);
                mediaPlayer.start();
                play.setBackgroundResource(android.R.drawable.ic_media_pause);
                j=pos;
               if(recyclerAdapter.getArtist(locArray.get(pos))!=null)
                currArtist1stScreen.setImageBitmap(recyclerAdapter.getArtist(locArray.get(pos)));
                else
                    currArtist1stScreen.setImageResource(R.drawable.music);

            }
        });

        currArtist1stScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,SecondScreen.class);

                intent.putExtra("Path",locArray);
                intent.putExtra("Position",j);
                intent.putExtra("Title",titleArray);
                intent.putExtra("Artist",artistArray);
                intent.putExtra("Duration",durationArray);

                startActivity(intent);

            }
        });
       play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
           playPause();
           }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (j == 0) {
                    ;
                } else {
                    if (shuffFlag % 2 == 0)
                        j++;
                    else
                        j = shuffle();
                    playNext(j);
                    updateContents();

                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(j==0)
                {
                    ;
                }
                else {
                    j--;
                    updateContents();
                    playPrev(j);
                }

            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Share") {
            Toast.makeText(this, "WhatUp!!", Toast.LENGTH_SHORT).show();
            String sharePath=locArray.get(j);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/mp3");
            share.putExtra(Intent.EXTRA_STREAM,Uri.parse(sharePath));
            startActivity(Intent.createChooser(share,"Share Mp3 File"));
        }
        if(item.getTitle()=="Details")
        {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Details");
            builder.setMessage("Song Name :"+ titleArray.get(j) + "\nArtist :"+ artistArray.get(j)+"\nDuration :"+ String.valueOf(String.format("%.2f",Float.valueOf(playlist.get(j).getDuration())/60000))+" Min");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                ;
                }
            });
            builder.create().show();
        }
        if(item.getTitle()=="Delete")
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Delete");
            builder.setMessage("Are You Sure You Want To Delete?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d(TAG, "!!!!!!!!!!!onClickDelete: "+locArray.get(j));
                    File file=new File(locArray.get(j));
                    boolean del= file.delete();
                    Log.d(TAG, "!!!!!!!!!!!onClickDelete: "+locArray.get(j)+ del);
                    recyclerAdapter.updateSongList(playlist);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ;
                }
            });
            builder.create().show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
   protected void onPostResume() {
       super.onPostResume();
        nowPlaying1stScreen.setText(playlist.get(j).getTitle().toString());
       duration1stScreen.setText(String.valueOf(String.format("%.2f",Float.valueOf(playlist.get(j).getDuration())/60000))+" Min");
       if(recyclerAdapter.getArtist(locArray.get(j))!=null)
            currArtist1stScreen.setImageBitmap(recyclerAdapter.getArtist(locArray.get(j)));
       else
          currArtist1stScreen.setImageResource(R.drawable.music);

   }


    void getMusic() {
        Log.d(TAG, "!!!!!!!getMusic: ");

        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null,null);



        if (songCursor != null  && songCursor.moveToFirst()) {
            Log.d(TAG, "!!!!!!!!!AddingMusicHere: "+ songCursor.getCount());
            int songId= songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String currentLocation = songCursor.getString(songLocation);
                String currentDuration = songCursor.getString(songDuration);
                String currentId= songCursor.getString(songId);

                playlist.add(new Song(currentTitle,currentArtist,currentDuration,currentId,currentLocation));
                locArray.add(currentLocation);
                idArray.add(currentId);
            }while (songCursor.moveToNext());
        }
        for(i=0;i<playlist.size();i++)
        {
            Log.d(TAG, "!!!!!!getMusic: "+ playlist.size()+playlist.get(i).getTitle());
            titleArray.add(playlist.get(i).getTitle());
            artistArray.add(playlist.get(i).getArtist());
            durationArray.add(playlist.get(i).getDuration());
        }

    }
    public void playPause() {
        if(mediaPlayer==null)
        {
            ;
        }
        else
        {
            if (mediaPlayer.isPlaying()) {

                play.setBackgroundResource(android.R.drawable.ic_media_play);
                mediaPlayer.pause();
                seekPos = mediaPlayer.getCurrentPosition();
            } else {
                play.setBackgroundResource(android.R.drawable.ic_media_pause);
                mediaPlayer.seekTo(seekPos);
                mediaPlayer.start();
            }
        }
    }
    public void playNext(int j){
        if(mediaPlayer!=null)
        {
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(MainActivity.this,Uri.parse(locArray.get(j)));
        mediaPlayer.start();
        if(recyclerAdapter.getArtist(locArray.get(j))!=null)
            currArtist1stScreen.setImageBitmap(recyclerAdapter.getArtist(locArray.get(j)));
        else
            currArtist1stScreen.setImageResource(R.drawable.music);
        nowPlaying1stScreen.setText(playlist.get(j).getTitle().toString());
        duration1stScreen.setText(String.valueOf(String.format("%.2f",Float.valueOf(playlist.get(j).getDuration())/60000))+" Min");

    }
    public void playPrev(int j){
        if (mediaPlayer!=null)
        {
            mediaPlayer.release();
        }
        mediaPlayer=MediaPlayer.create(MainActivity.this,Uri.parse(locArray.get(j)));
        mediaPlayer.start();
        if(recyclerAdapter.getArtist(locArray.get(j))!=null)
            currArtist1stScreen.setImageBitmap(recyclerAdapter.getArtist(locArray.get(j)));
        else
            currArtist1stScreen.setImageResource(R.drawable.music);
        nowPlaying1stScreen.setText(playlist.get(j).getTitle().toString());
        duration1stScreen.setText(String.valueOf(String.format("%.2f",Float.valueOf(playlist.get(j).getDuration())/60000))+" Min");

    }


}