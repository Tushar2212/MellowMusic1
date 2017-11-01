package com.example.mukeshsharma.mymusic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.mukeshsharma.mymusic.adapters.RecyclerAdapter;

import static com.example.mukeshsharma.mymusic.MainActivity.currArtist1stScreen;
import static com.example.mukeshsharma.mymusic.MainActivity.j;
import static com.example.mukeshsharma.mymusic.MainActivity.locArray;
import static com.example.mukeshsharma.mymusic.MainActivity.nowPlaying1stScreen;
import static com.example.mukeshsharma.mymusic.MainActivity.titleArray;

/**
 * Created by Mukesh Sharma on 04-10-2017.
 */

public class NotifGenerator
{
    public static final String NOTIFY_PREV="com.example.mukeshsharma.mymusic.previous";
    public static final String NOTIFY_PLAY="com.example.mukeshsharma.mymusic.play";
    public static final String NOTIFY_PAUSE="com.example.mukeshsharma.mymusic.pause";
    public static final String NOTIFY_NEXT="com.example.mukeshsharma.mymusic.next";
    public static final String NOTIFY_CLOSE="com.example.mukeshsharma.mymusic.close";
    private static final int NOTIFICATION_ID_OPEN_ACTIVITY=1;
    public static final String TAG="123";
    static NotificationManager notifManager;
    static NotificationCompat.Builder nc;
    static RemoteViews expandedView;



    public static void openActivityNotif(Context context)
    {
        NotificationCompat.Builder nc=new NotificationCompat.Builder(context);
        NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent  pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        nc.setSmallIcon(R.mipmap.ic_launcher);
        nc.setAutoCancel(true);
        nc.setContentTitle("Hello");
        nc.setContentText("It Worked");
        notificationManager.notify(NOTIFICATION_ID_OPEN_ACTIVITY,nc.build());
    }
    public static void bigNotifs(Context context)
    {
        RecyclerAdapter recyclerAdapter;
        expandedView = new RemoteViews(context.getPackageName(), R.layout.notifs);

        nc = new NotificationCompat.Builder(context);
        notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent=new Intent(context,MainActivity.class);
      //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi=PendingIntent.getActivity(context,0,intent,0);//PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pi);
        nc.setSmallIcon(R.drawable.music);
        nc.setCustomBigContentView(expandedView);
        nc.setContentTitle("Mellow Music");
        nc.setContentText("Now Playing");
        updateContents();
        setListeners(expandedView,context);

    }
    public static void updateContents()
    {
        expandedView.setTextViewText(R.id.tvNotifs,titleArray.get(j));
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(locArray.get(j));
        byte[] songImg2ByteArray=mediaMetadataRetriever.getEmbeddedPicture();
        try{
            Bitmap songImg2= BitmapFactory.decodeByteArray(songImg2ByteArray,0,songImg2ByteArray.length);
            //  artist2.setImageBitmap(songImg2);
            expandedView.setImageViewBitmap(R.id.ivNotifs,songImg2);


        }catch (NullPointerException e)
        {
            expandedView.setImageViewResource(R.id.ivNotifs,R.drawable.music);
        }
        try{
            Bitmap songImg2= BitmapFactory.decodeByteArray(songImg2ByteArray,0,songImg2ByteArray.length);
            currArtist1stScreen.setImageBitmap(songImg2);
        }catch (NullPointerException e){
            currArtist1stScreen.setImageResource(R.drawable.music);
        }
        nowPlaying1stScreen.setText(titleArray.get(j));


        //   nc.getBigContentView().setTextViewText(R.id.tvNotifs,titleArray.get(j));
     //   nc.getBigContentView().setImageViewUri(R.id.ivNotifs, Uri.parse(locArray.get(j)));
        notifManager.notify(NOTIFICATION_ID_OPEN_ACTIVITY,nc.build());


    }
    private static void setListeners(RemoteViews view,Context context)
    {
     Intent previous=new Intent(NOTIFY_PREV);
     Intent play=new Intent(NOTIFY_PLAY);
     Intent pause=new Intent(NOTIFY_PAUSE);
     Intent next=new Intent(NOTIFY_NEXT);
     Intent close=new Intent(NOTIFY_CLOSE);

    PendingIntent piPrev=PendingIntent.getBroadcast(context,0,previous,PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ivNotifPrev,piPrev);

    PendingIntent piPlay=PendingIntent.getBroadcast(context,0,play,PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ivNotifPlay,piPlay);

    PendingIntent piPause=PendingIntent.getBroadcast(context,0,pause,PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ivNotifPause,piPause);
    PendingIntent piNext=PendingIntent.getBroadcast(context,0,next,PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ivNotifNext,piNext);
    PendingIntent piClose=PendingIntent.getBroadcast(context,0,close,PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.ivClose,piClose);
    }
}
