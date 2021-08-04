package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.mediaplayer.Model.Song;
import com.example.mediaplayer.Model.SongAdapter;
import com.example.mediaplayer.Model.SongsManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> songList = new ArrayList<>();
    private SongAdapter songAdapter;
    private SharedPreferences m_SharedPreferences;
    private DeleteDialog m_DeleteDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_SharedPreferences = getSharedPreferences("details",MODE_PRIVATE);

        m_DeleteDialog = new DeleteDialog(this,new DeleteListener());
        setSongsRecyclerView();
    }

    private void setSongsRecyclerView()
    {
        RecyclerView songs_RecyclerView = findViewById(R.id.main_activity_songs_recycler_view);
        songs_RecyclerView.setHasFixedSize(true);
        songs_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSongList();

        songAdapter = new SongAdapter(songList);

        songAdapter.setListener(new SongAdapter.MySongListener() {
            @Override
            public void onSongClicked(int position, View view) {
                Intent intent = new Intent(MainActivity.this, SongDeatailsActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        ItemTouchHelper.SimpleCallback callback = getCallback();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(songs_RecyclerView);
        songs_RecyclerView.setAdapter(songAdapter);
    }

    private void getSongList() {

        if(m_SharedPreferences.getBoolean("is first time",true))
        {
            songList = SongsManager.getInstance().getSongList(true,this);
        }
        else
        {
            songList = SongsManager.getInstance().getSongList(false,this);
        }

        SharedPreferences.Editor editor = m_SharedPreferences.edit();
        editor.putBoolean("is first time",false);
        editor.commit();
    }

    private ItemTouchHelper.SimpleCallback getCallback()
    {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                int pos1 = viewHolder.getAdapterPosition();
                int pos2 = target.getAdapterPosition();
                if(pos1!= pos2) {
                    Song country1 = songList.get(pos1);
                    Song country2 = songList.get(pos2);
                    songList.set(pos1, country2);
                    songList.set(pos2, country1);
                    songAdapter.notifyItemMoved(pos1, pos2);
                    SongsManager.getInstance().setSongList(songList);
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                m_DeleteDialog.setPosition(viewHolder.getAdapterPosition());
                m_DeleteDialog.show();
            }
        };
    }



    public class DeleteListener implements DeleteDialog.MyDeleteListener{
        @Override
        public void onYesBtnClicked(int position, View view) {
            songList.remove(position);
            SongsManager.getInstance().setSongList(songList);
            songAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onNoBtnClicked(int position,View view) {
            songList.set(position, songList.get(position));
            songAdapter.notifyItemChanged(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        songAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SongsManager.getInstance().saveList(this);
    }

}