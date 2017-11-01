package com.example.mukeshsharma.mymusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;

import static com.example.mukeshsharma.mymusic.MainActivity.currArtist1stScreen;
import static com.example.mukeshsharma.mymusic.MainActivity.duration1stScreen;
import static com.example.mukeshsharma.mymusic.MainActivity.j;
import static com.example.mukeshsharma.mymusic.MainActivity.mediaPlayer;
import static com.example.mukeshsharma.mymusic.MainActivity.nowPlaying1stScreen;
import static com.example.mukeshsharma.mymusic.MainActivity.seekPos;


public class SecondScreen extends AppCompatActivity  {
    ArrayList<String> secondScreenPath,tittleList2,artistList2,durationList2;
    int secondScreenLoc;
    ImageView artist2;
    static int shuffFlag;
    static int shuffleposi;
    android.os.Handler handler;
    Runnable r;
    TextView tvArtist2,tvTitle2,tvDuration2;
    ImageButton prev2,play2,next2;
    Button shuffle,repeat;
    SeekBar seekBar;
    MediaMetadataRetriever mediaMetadataRetriever;
    public static final String TAG="k";
    Chronometer cm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
         secondScreenPath=getIntent().getStringArrayListExtra("Path");
        secondScreenLoc=getIntent().getIntExtra("Position",1);
        tittleList2=getIntent().getStringArrayListExtra("Title");
        artistList2=getIntent().getStringArrayListExtra("Artist");
        durationList2=getIntent().getStringArrayListExtra("Duration");
        cm=(Chronometer)findViewById(R.id.tvCurrDuration);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        handler=new android.os.Handler();
        Log.d(TAG, "!!!!!!!!!!onCreate: "+ secondScreenPath+" "+ secondScreenLoc);
        prev2=(ImageButton)findViewById(R.id.imageButton);
        play2=(ImageButton)findViewById(R.id.imageButton2);
        next2=(ImageButton)findViewById(R.id.imageButton3);
        tvTitle2=(TextView)findViewById(R.id.tvTitle2ndScreen);
        tvArtist2=(TextView)findViewById(R.id.tvArtist2ndScreen);
        tvDuration2=(TextView)findViewById(R.id.tvMaxDuration2ndScreen);
        artist2= (ImageView) findViewById(R.id.ivArtis2ndScrren);
        shuffle=(Button)findViewById(R.id.btnShuffle);
        repeat=(Button)findViewById(R.id.btnRep);
        mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(secondScreenPath.get(secondScreenLoc));
        byte[] songImg2ByteArray=mediaMetadataRetriever.getEmbeddedPicture();
        try{
            Bitmap songImg2= BitmapFactory.decodeByteArray(songImg2ByteArray,0,songImg2ByteArray.length);
            artist2.setImageBitmap(songImg2);
        }catch (NullPointerException e)
        {
            artist2.setImageResource(R.drawable.music);
        }
        tvTitle2.setText(tittleList2.get(secondScreenLoc));
        tvArtist2.setText(artistList2.get(secondScreenLoc));
        tvDuration2.setText(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(secondScreenLoc))/60000)));
        cm.setBase(SystemClock.elapsedRealtime());
        cm.start();
        seekBar.setMax(mediaPlayer.getDuration());
        playing();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    Log.d(TAG, "!!!!!!!!onProgressChanged: ");
                    mediaPlayer.seekTo(progress);
                    seekPos=progress;
                    cm.setBase(SystemClock.elapsedRealtime()+ mediaPlayer.getDuration());
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mediaPlayer.isPlaying()) {

                    play2.setBackgroundResource(android.R.drawable.ic_media_play);
                    mediaPlayer.pause();
                    seekPos = mediaPlayer.getCurrentPosition();

                } else {
                    play2.setBackgroundResource(android.R.drawable.ic_media_pause);
                    mediaPlayer.seekTo(seekPos);
                    mediaPlayer.start();
                }
            }
        });
        prev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                j--;
                mediaMetadataRetriever.setDataSource(secondScreenPath.get(j));
                byte[] songImg2ByteArray=mediaMetadataRetriever.getEmbeddedPicture();
                try{
                    Bitmap songImg2= BitmapFactory.decodeByteArray(songImg2ByteArray,0,songImg2ByteArray.length);
                    artist2.setImageBitmap(songImg2);
                    currArtist1stScreen.setImageBitmap(songImg2);
                }catch (NullPointerException e)
                {
                    artist2.setImageResource(R.drawable.music);
                }
                tvTitle2.setText(tittleList2.get(j));
                tvArtist2.setText(artistList2.get(j));
                tvDuration2.setText(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(j))/60000))+" Min");
                nowPlaying1stScreen.setText(tittleList2.get(j));
                duration1stScreen.setText(String.valueOf(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(j))/60000))+" Min"));

                if(mediaPlayer!=null)
                {
                    mediaPlayer.release();
                }
                mediaPlayer=MediaPlayer.create(SecondScreen.this,Uri.parse(secondScreenPath.get(j)));
                seekBar.setMax(mediaPlayer.getDuration());
                playing();
                mediaPlayer.start();

            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shuffFlag%2==0)j++;
                else j=shuffle();
                mediaMetadataRetriever.setDataSource(secondScreenPath.get(j));
                byte[] songImg2ByteArray=mediaMetadataRetriever.getEmbeddedPicture();
                try{
                    Bitmap songImg2= BitmapFactory.decodeByteArray(songImg2ByteArray,0,songImg2ByteArray.length);
                    artist2.setImageBitmap(songImg2);
                    currArtist1stScreen.setImageBitmap(songImg2);

                }catch (NullPointerException e)
                {
                    artist2.setImageResource(R.drawable.music);
                }
                tvTitle2.setText(tittleList2.get(j));
                tvArtist2.setText(artistList2.get(j));
                tvDuration2.setText(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(j))/60000))+" Min");
                nowPlaying1stScreen.setText(tittleList2.get(j));
                duration1stScreen.setText(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(j))/60000))+" Min");

                if(mediaPlayer!=null)
                {
                    mediaPlayer.release();
                }
                mediaPlayer=MediaPlayer.create(SecondScreen.this,Uri.parse(secondScreenPath.get(j)));
                seekBar.setMax(mediaPlayer.getDuration());
                playing();
                mediaPlayer.start();

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
             //   mediaPlayer=MainActivity.mediaPlayer;
             //   if (shuffFlag%2==0)j++;
             //   else j=shuffle();
             //   mediaMetadataRetriever.setDataSource(secondScreenPath.get(j));
             //   byte[] songImg2ByteArray=mediaMetadataRetriever.getEmbeddedPicture();
             //   try{
             //       Bitmap songImg2= BitmapFactory.decodeByteArray(songImg2ByteArray,0,songImg2ByteArray.length);
             //       artist2.setImageBitmap(songImg2);
             //       currArtist1stScreen.setImageBitmap(songImg2);
                //   }catch (NullPointerException e)
                //  {
                //   artist2.setImageResource(R.drawable.music);
                // }
                //tvTitle2.setText(tittleList2.get(j));
                //    tvArtist2.setText(artistList2.get(j));
                //    tvDuration2.setText(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(j))/60000))+" Min");
                // nowPlaying1stScreen.setText(tittleList2.get(j));
                //    duration1stScreen.setText(String.valueOf(String.format("%.2f",Float.valueOf(durationList2.get(j))/60000))+" Min");

                // if(mediaPlayer!=null)
                // {
                //  mediaPlayer.release();
                //}
                //mediaPlayer=MediaPlayer.create(SecondScreen.this,Uri.parse(secondScreenPath.get(j)));
                //mediaPlayer.start();
                //mediaPlayer.setLooping(true);
                //seekBar.setMax(mediaPlayer.getDuration());
                //playing();
                next2.performClick();
                cm.stop();
            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffFlag++;
                if(shuffFlag%2==0)
                shuffle.setBackgroundColor(0);
                else shuffle.setBackgroundColor(Color.GRAY);
                shuffleposi=shuffle();
            }
        });
    }

public static int shuffle(){
    Random shuff=new Random();
    return  shuff.nextInt(860)+1;
}
    public void playing(){
      //  Log.d(TAG, "!!!!!!!!!playing 1 : " + mediaPlayer.toString()+mediaPlayer.getCurrentPosition());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        //   if(mediaPlayer.isPlaying())
        //{
        r=new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "!!!!!!!!!!run: " + mediaPlayer.getCurrentPosition());
                playing();
            }
        };
        handler.postDelayed(r,1000);
        // }else{
        //    Log.d(TAG, "!!!!!!!!!NoRun: ");
        // }
    }
}
