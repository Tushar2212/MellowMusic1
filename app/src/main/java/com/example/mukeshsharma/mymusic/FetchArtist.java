package com.example.mukeshsharma.mymusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mukeshsharma.mymusic.adapters.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by Mukesh Sharma on 22-08-2017.
 */

public class FetchArtist extends AsyncTask<Void,Void,ArrayList<Bitmap>>{
    ArrayList<String> locList;
     ArrayList<Bitmap> artistList;
    MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
    int i=0;
    public static final String TAG="YO";
    byte[] albumArtByteArray;
    Bitmap songImage;
    public FetchArtist(ArrayList<String> locList)
    {
        this.locList = locList;
    }

    @Override
    protected ArrayList<Bitmap> doInBackground(Void... voids) {
        artistList=new ArrayList<>();
        for( i=0;i <= locList.size() ;i++)
        {
            Log.d(TAG, "!!!!!!!!!!doInBackground: "+ locList.get(i));
            songImage=null;
            try {
                mediaMetadataRetriever.setDataSource(locList.get(i));
                Log.d(TAG, "!!!!!doInBackgroundMediaMetaData: "+ mediaMetadataRetriever);

            }catch (NullPointerException e )
            {
                ;
            }
            albumArtByteArray=mediaMetadataRetriever.getEmbeddedPicture();
            try{
                Log.d(TAG, "!!!!!doInBackgroundAlbumByteArray:"+ albumArtByteArray);
               try {
                   songImage = BitmapFactory.decodeByteArray(albumArtByteArray, 0, albumArtByteArray.length);
               }catch (java.lang.OutOfMemoryError e)
               {
                   Log.d(TAG, "????????doInBackground: "+ locList.get(i));
               }
               Log.d(TAG, "!!!!!doInBackgroundSongImage: "+ songImage);
                artistList.add(songImage);
            }catch (NullPointerException e)
            {
                artistList.add(songImage);
            }
        }
        Log.d(TAG, "!!!!!doInBackgroundArtistList: "+ artistList);
        return artistList;
    }


    @Override
    protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
        super.onPostExecute(bitmaps);
        Log.d(TAG, "!!!!!!!!!!!!!onPostExecute: "+ bitmaps);
       // RecyclerAdapter recyclerAdapter=new RecyclerAdapter(bitmaps);
    }

}

