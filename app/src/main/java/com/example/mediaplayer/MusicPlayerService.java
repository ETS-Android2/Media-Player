package com.example.mediaplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


import com.bumptech.glide.Glide;
import com.example.mediaplayer.Model.Song;
import com.example.mediaplayer.Model.SongsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MusicPlayerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private MediaPlayer m_Media_Player = new MediaPlayer();
    private final int NOTIF_ID = 1;
    private int m_Current_Song = 0;
    private ArrayList<Song> m_songList;
    private RemoteViews m_RemoteViews;
    private NotificationCompat.Builder m_Notif_Builder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        m_Media_Player.setOnCompletionListener(this);
        m_Media_Player.setOnPreparedListener(this);
        m_Media_Player.reset();

        m_songList = SongsManager.getInstance().getSongList();
        setNotification();
    }

    private void setNotification() {

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String channelId = null;
        if(Build.VERSION.SDK_INT>=26) {
            channelId =  "Music chanel" ;
            CharSequence channelName =  "Music Chanel" ;
            NotificationChannel notificationChannel =  new  NotificationChannel(channelId, channelName, NotificationManager. IMPORTANCE_LOW);
            manager.createNotificationChannel(notificationChannel);
        }
        m_Notif_Builder = new NotificationCompat.Builder(this,channelId);

        m_RemoteViews = new RemoteViews(getPackageName(),R.layout.notifaication_layout);

        Intent playIntent = new Intent(this,MusicPlayerService.class);
        playIntent.putExtra("command", "play");
        PendingIntent playPendingIntent = PendingIntent.getService(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_RemoteViews.setOnClickPendingIntent(R.id.play_btn,playPendingIntent);

        Intent nextIntent = new Intent(this,MusicPlayerService.class);
        nextIntent.putExtra("command", "next");
        PendingIntent nextPendingIntent = PendingIntent.getService(this,1,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_RemoteViews.setOnClickPendingIntent(R.id.next_btn,nextPendingIntent);

        Intent prevIntent = new Intent(this,MusicPlayerService.class);
        prevIntent.putExtra("command", "prev");
        PendingIntent prevPendingIntent = PendingIntent.getService(this,2,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_RemoteViews.setOnClickPendingIntent(R.id.prev_btn,prevPendingIntent);

        Intent pauseIntent = new Intent(this,MusicPlayerService.class);
        pauseIntent.putExtra("command", "pause");
        PendingIntent pausePendingIntent = PendingIntent.getService(this,3,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_RemoteViews.setOnClickPendingIntent(R.id.pause_btn,pausePendingIntent);

        Intent closeIntent = new Intent(this,MusicPlayerService.class);
        closeIntent.putExtra("command", "close");
        PendingIntent closeIntentPendingIntent = PendingIntent.getService(this,4,closeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        m_RemoteViews.setOnClickPendingIntent(R.id.close_notif_btn,closeIntentPendingIntent);



        m_Notif_Builder.setCustomBigContentView(m_RemoteViews);
        m_Notif_Builder.setSmallIcon(R.drawable.music_logo_icon);

        startForeground(NOTIF_ID, m_Notif_Builder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String command = intent.getStringExtra("command") ;

        switch(command)
        {
            case "new instance":
                playFirstSong();
                break;
            case "play":
                playSong();
                break;
            case "play current song":
                m_Current_Song = intent.getIntExtra("song position",0) ;
                changeNotifView();
                playCurrentSong();
                break;
            case "next":
                playNextSong();
                break;
            case "prev":
                playPrevSong();
                break;
            case "pause":
                pauseSong();
                break;
            case "close":
                closeNotif();
                break;
        }



        return super.onStartCommand(intent, flags, startId);
    }

    private void changeNotifView() {
        m_RemoteViews.setTextViewText(R.id.notificatiion_title_song_name,m_songList.get(m_Current_Song).getSongName());
        m_RemoteViews.setTextViewText(R.id.notificatiion_performer_song_name,m_songList.get(m_Current_Song).getSongPerformer());
        m_RemoteViews.setImageViewUri(R.id.notif_song_photo, Uri.parse(m_songList.get(m_Current_Song).getPhotoPath()));

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID,m_Notif_Builder.build());
    }

    private void playCurrentSong()
    {
        m_Media_Player.reset();
        try {
            if (m_Media_Player.isPlaying()) {
                m_Media_Player.stop();
                m_Media_Player.reset();
            }

            if (SongsManager.getInstance().getSongListSize() > 0) {
                m_Media_Player.setDataSource(m_songList.get(m_Current_Song).getLink());
                m_Media_Player.prepareAsync();

                changeNotifView();
            }
            else
            {
                stopSelf();
                Toast.makeText(this, "Song List Is Empty", Toast.LENGTH_SHORT).show();
            }
        }
        catch (IOException e)
        {
            playNextSong();
            e.printStackTrace();
        }
    }


    private void playFirstSong() {

        m_Current_Song = 0;
        changeNotifView();
        playCurrentSong();
    }



    private void playSong() {
        if(!m_Media_Player.isPlaying())
            m_Media_Player.start();
    }

    private void playNextSong()
    {
        if(!m_Media_Player.isPlaying())
        {
            m_Media_Player.stop();
        }

        m_Current_Song++;
        m_songList = SongsManager.getInstance().getSongList();
        if(m_Current_Song ==m_songList.size() )
            m_Current_Song = 0;
        playCurrentSong();
    }

    private void playPrevSong() {
        m_Current_Song = m_Current_Song - 1;

        m_songList = SongsManager.getInstance().getSongList();
        if(m_Current_Song == -1 )
            m_Current_Song = m_songList.size()-1;
        playCurrentSong();
    }

    private void pauseSong() {
        if(m_Media_Player.isPlaying())
        {
            m_Media_Player.pause();
        }
    }

    private void closeNotif() {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(m_Media_Player!=null) {
            if(m_Media_Player.isPlaying())
                m_Media_Player.stop();
            m_Media_Player.release();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        m_Current_Song++;
        m_Current_Song = m_Current_Song % m_songList.size();
        playCurrentSong();

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        m_Media_Player.start();

    }
}
