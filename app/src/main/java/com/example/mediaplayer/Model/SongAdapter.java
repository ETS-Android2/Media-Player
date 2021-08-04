package com.example.mediaplayer.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediaplayer.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> m_SongsList;
    private MySongListener listener;

    public interface MySongListener {
        void onSongClicked(int position, View view);
    }

    public void setListener(MySongListener listener) {
        this.listener = listener;
    }

    public SongAdapter(List<Song> Songs) {
        this.m_SongsList = Songs;
    }


    public class SongViewHolder extends RecyclerView.ViewHolder {

        TextView Song_Name_TextView;
        TextView Performer_Song_Name_TextView;
        ImageView song_photo;

        public SongViewHolder(View itemView) {
            super(itemView);

            Song_Name_TextView = itemView.findViewById(R.id.song_name_text_view);
            Performer_Song_Name_TextView = itemView.findViewById(R.id.performer_song_name_text_view);
            song_photo = itemView.findViewById(R.id.song_photo);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(listener!=null)
                        listener.onSongClicked(getAdapterPosition(),view);
                }
            });

        }
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_layout,parent,false);
        SongViewHolder songViewHolder = new SongViewHolder(view);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song current_Song = m_SongsList.get(position);
        holder.Song_Name_TextView.setText(current_Song.getSongName());
        holder.Performer_Song_Name_TextView.setText(current_Song.getSongPerformer());

        Glide.with(ApplicationContext.getContext()).load(current_Song.getPhotoPath()).into(holder.song_photo);
    }

    @Override
    public int getItemCount() {
        return m_SongsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

