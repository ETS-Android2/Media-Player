package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mediaplayer.Model.Song;
import com.example.mediaplayer.Model.SongsManager;

public class SongDeatailsActivity extends AppCompatActivity {

    private Button m_PlaySongBtn;
    private int m_Position;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_deatails);

        m_Position = getIntent().getIntExtra("position",0);
        Song currentSong = SongsManager.getInstance().getSongList().get(m_Position);

        ImageView song_photo = findViewById(R.id.song_deatails_photo_image_view);
        TextView song_name = findViewById(R.id.song_name_deatails_text_view);
        TextView song_performer = findViewById(R.id.song_name_performer_text_view);

        song_name.setText(currentSong.getSongName());
        song_performer.setText(currentSong.getSongPerformer());


        Glide.with(this).load(currentSong.getPhotoPath()).into(song_photo);

        setPlayBtn();
    }

    private void setPlayBtn() {
        m_PlaySongBtn = findViewById(R.id.play_from_deatails_btn);

        m_PlaySongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SongDeatailsActivity.this,MusicPlayerService.class);
                intent.putExtra("command", "play current song");
                intent.putExtra("song position",m_Position);
                startService(intent);
            }
        });

    }
}