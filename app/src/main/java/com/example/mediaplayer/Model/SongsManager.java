package com.example.mediaplayer.Model;

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SongsManager {

    ArrayList<Song> m_SongList ;
    private static SongsManager m_Instance;

    private SongsManager() {
        m_SongList = new ArrayList<>();
    }

    public static SongsManager getInstance()
    {
        if(m_Instance == null)
            m_Instance = new SongsManager();
        return m_Instance;
    }

    public void addSong(Song song)
    {
        m_SongList.add(song);
    }
    public void deleteSong(Song song)
    {
        m_SongList.remove(song);
    }

    public ArrayList<Song> getSongList(boolean firstTime,AppCompatActivity activity) {


        if(firstTime ) {

            Uri uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/waka_waka_photo");
            m_SongList.add(new Song("Waka Waka","Shakira",uri.toString(),"https://drive.google.com/u/0/uc?id=1GkGeGV10tkd34SgNtkxszcKGawRHicVe&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/oops_i_did_it_again_photo");
            m_SongList.add(new Song("Oops! I Did It Again","Britney Spears",uri.toString(),"https://drive.google.com/u/0/uc?id=1Qrswtwzg59-FsLH-TdBkzEeAi_5yvODZ&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/shape_of_you_photo");
            m_SongList.add(new Song("Shape of You","Ed Sheeran",uri.toString(),"https://drive.google.com/u/0/uc?id=1FLqmtteZQJDx0KakA0i4t9FbUWIQJhXW&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/billie_jean_photo");
            m_SongList.add(new Song("Billie Jean","Michael Jackson",uri.toString(),"https://drive.google.com/u/0/uc?id=1Z7BJW75ubez8yogLWnBeQq1McAGszoEw&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/wrecking_ball_photo");
            m_SongList.add(new Song("Wrecking Ball","Miley Cyrus",uri.toString(),"https://drive.google.com/u/0/uc?id=1jFD8zA_Zmj6h3hNLgy5DyhRdv_zQiNsL&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/not_afraid_photo");
            m_SongList.add(new Song("Not Afraid","Eminem",uri.toString(),"https://drive.google.com/u/0/uc?id=1MpU1vKLOzqk6xcqBk-AXt9sJHy_tc8b_&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/someone_like_you_photo");
            m_SongList.add(new Song("Someone like you","Adele",uri.toString(),"https://drive.google.com/u/0/uc?id=1rFIyoF1qWxpZtRXaRTLahONCb2zCplnL&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/diamonds_photo");
            m_SongList.add(new Song("Diamonds","Rihanna",uri.toString(),"https://drive.google.com/u/0/uc?id=1R9t6N1hic0zzuIgeWQOMmOJ3ALJ2uGD4&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/chandelier_photo");
            m_SongList.add(new Song("Chandelier","Sia",uri.toString(),"https://drive.google.com/u/0/uc?id=1QoPCLGQ-9IrW9kXKgKJWk3GBWdXPxL41&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/gods_plan_photo");
            m_SongList.add(new Song("God's Plan","Drake",uri.toString(),"https://drive.google.com/u/0/uc?id=1kHVyplgFT70B-z4ouOcoQWu2qqFmUyt5&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/rockstar_photo");
            m_SongList.add(new Song("Rockstar","Post Malone",uri.toString(),"https://drive.google.com/u/0/uc?id=1h9xwSRrjjjD0Dv8GH8TYHDCSXZjJ9IvF&export=download"));

            uri = Uri.parse("android.resource://com.example.mediaplayer/drawable/memories_photo");
            m_SongList.add(new Song("David Guetta","Memories",uri.toString(),"https://drive.google.com/u/0/uc?id=1vLWBKzCrSnUGau6dPWWZ6sE7Lovt8P0-&export=download"));

            return m_SongList;
        }
        try {
            FileInputStream fis  = activity.openFileInput("songs list");
            ObjectInputStream ois  = new ObjectInputStream(fis);
            m_SongList = (ArrayList<Song>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return m_SongList;
    }

    public void setSongList(ArrayList<Song> m_SongList) {
        this.m_SongList = m_SongList;
    }

    public void saveList(AppCompatActivity activity)
    {
        try {
            FileOutputStream fos = activity.openFileOutput("songs list", Context.MODE_PRIVATE);
            ObjectOutputStream oos  = new ObjectOutputStream(fos);
            oos.writeObject(m_SongList);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Song> getSongList() {
        return m_SongList;
    }

    //TODO check
    public int getSongListSize(){return m_SongList.size();};
}
