package com.example.mukeshsharma.mymusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.mukeshsharma.mymusic.MainActivity.j;
import static com.example.mukeshsharma.mymusic.MainActivity.locArray;
import static com.example.mukeshsharma.mymusic.MainActivity.mediaPlayer;
import static com.example.mukeshsharma.mymusic.MainActivity.seekPos;
import static com.example.mukeshsharma.mymusic.NotifGenerator.updateContents;

/**
 * Created by Mukesh Sharma on 05-10-2017.
 */

public class NotifBroadcasts extends BroadcastReceiver {
    public static final String TAG="Yo";
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(NotifGenerator.NOTIFY_PLAY))
        {
            if (mediaPlayer.isPlaying()) {

                mediaPlayer.pause();
                seekPos = mediaPlayer.getCurrentPosition();

            } else {
                mediaPlayer.seekTo(seekPos);
                mediaPlayer.start();
            }

            Toast.makeText(context, "PLAY", Toast.LENGTH_SHORT).show();

        }
        if(intent.getAction().equals(NotifGenerator.NOTIFY_PAUSE))
        {
           // dummy.playPause();
            Toast.makeText(context, "PAUSE", Toast.LENGTH_SHORT).show();
        }
        if(intent.getAction().equals(NotifGenerator.NOTIFY_NEXT))
        {
            j++;
            if(mediaPlayer!=null)
            {
                mediaPlayer.release();
            }
            mediaPlayer= MediaPlayer.create(context, Uri.parse(locArray.get(j)));
            mediaPlayer.start();
            Log.d(TAG, "!!!!!!!!!!!!!!!!!!!!!onReceive:onNext ");
            updateContents();
            Toast.makeText(context, "NEXT", Toast.LENGTH_SHORT).show();
        }
        if(intent.getAction().equals(NotifGenerator.NOTIFY_PREV))
        {   j--;
            if(mediaPlayer!=null)
            {
                mediaPlayer.release();
            }
            mediaPlayer=MediaPlayer.create(context,Uri.parse(locArray.get(j)));
            mediaPlayer.start();
            updateContents();
            Toast.makeText(context, "PREVIOUS", Toast.LENGTH_SHORT).show();
        }
        if(intent.getAction().equals(NotifGenerator.NOTIFY_CLOSE))
        {
            Toast.makeText(context, "CLOSE", Toast.LENGTH_SHORT).show();
        }
    }
}
