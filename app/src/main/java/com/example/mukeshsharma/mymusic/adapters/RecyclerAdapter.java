package com.example.mukeshsharma.mymusic.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukeshsharma.mymusic.FetchArtist;
import com.example.mukeshsharma.mymusic.R;
import com.example.mukeshsharma.mymusic.Song;

import java.util.ArrayList;

/**
 * Created by Mukesh Sharma on 29-07-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PlaylistViewHolder>{
    private ArrayList<Song> songArrayList;
    private ArrayList<String> songLocArray;
    //ArrayList<Song> newArrayList;
    private Context context;
    MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();;
    byte[] albumArtByteArray;
    int count=0
            ;
    ArrayList<Bitmap> bmps;
    public static final String TAG="YUS";
     OnItemClickListener onItemClickListener;

    public RecyclerAdapter(ArrayList<Song> songArrayList,ArrayList<String> songLocArray, Context context) {
        this.songArrayList = songArrayList;
        this.songLocArray=songLocArray;
        this.context = context;
        }
   // public RecyclerAdapter(ArrayList<Bitmap> bmps)
  //  {
  //      this.bmps=bmps;
  //  }
    public interface OnItemClickListener{
         void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        Log.d(TAG, "!!!!!!!!!!!!!!!!!setOnItemClickListener: ");
              this.onItemClickListener = onItemClickListener;
    }
    public void updateSongList(ArrayList<Song> songArrayList)
    {
        Log.d(TAG, "!!!!!!!!!!!updateSongList: ");
     this.songArrayList=songArrayList;
        notifyDataSetChanged();
    }
    @Override
        public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = li.inflate(R.layout.list_item,parent,false);
      //  FetchArtist fetchArtist=new FetchArtist(songLocArray);
     //   fetchArtist.execute();
        return new PlaylistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, final int position) {
        Log.d(TAG, "!!!!!!!!!!onBindViewHolder: ");
        Song thisSong = songArrayList.get(position);
        holder.title.setText(thisSong.getTitle());
        holder.artist.setText(thisSong.getArtist());
        holder.duration.setText(String.valueOf(String.format("%.2f", Float.valueOf(thisSong.getDuration()) / 60000)) + " Min");

        holder.rootVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "!!!!!!!!!!!!!!onClick: " + position);
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position);
            }
        });
        try {
            holder.img.setImageBitmap(bmps.get(position));

        } catch (NullPointerException e) {
            holder.img.setImageResource(R.drawable.music);


            if (getArtist(songLocArray.get(position)) != null) {
                Log.d(TAG, "!!!!!onBindViewHolder: " + position);
                Log.d(TAG, "!!!!!!!!!WTF: " + getArtist(songLocArray.get(position)));
                holder.img.setImageBitmap(getArtist(songLocArray.get(position)));
            } else {
                holder.img.setImageResource(R.drawable.music);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return songArrayList.size();
    }

    public  class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView img;
       TextView title,artist,duration;
        View rootVIew;

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle("Select");
            menu.add(0,v.getId(),0,"Details");
            menu.add(0,v.getId(),0,"Delete");
            menu.add(0,v.getId(),0,"Share");
        }


        public PlaylistViewHolder(View itemView) {
           super(itemView);
           img=(ImageView)itemView.findViewById(R.id.ivArtist);
           title=(TextView)itemView.findViewById(R.id.tvTitle);
           artist=(TextView)itemView.findViewById(R.id.tvArtist);
           duration=(TextView)itemView.findViewById(R.id.tvDuration);
           itemView.setOnCreateContextMenuListener(this);
            rootVIew=itemView;
       }
   }
   public Bitmap getArtist(String path){
      Bitmap songImage = null;
       Log.d(TAG, "!!!!!!!!!!getArtist: "+ path);
      try {
          mediaMetadataRetriever.setDataSource(path);

     }catch (java.lang.IllegalArgumentException e)
      {
          ;
      }
      albumArtByteArray=mediaMetadataRetriever.getEmbeddedPicture();
       try{
           songImage = BitmapFactory.decodeByteArray(albumArtByteArray, 0, albumArtByteArray.length);
           return  songImage;
       }catch (NullPointerException e)
       {
           return  songImage;
      }

   }
   public ArrayList<String> filterSearch(ArrayList<Song> newList,ArrayList<String> newPosList)
   {
       Log.d(TAG, "!!!!!!!!!filterSearch: "+ newList);
       songArrayList=new ArrayList<>();
       songLocArray=new ArrayList<>();
       songArrayList.addAll(newList);
       songLocArray.addAll(newPosList);
       notifyDataSetChanged();
       return songLocArray;
   }
}
