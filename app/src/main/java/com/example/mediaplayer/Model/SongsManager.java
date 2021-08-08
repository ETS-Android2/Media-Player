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

            Uri uri = Uri.parse("android.resource://com.example.musicplayer/drawable/see_you_again_photo");
            m_SongList.add(new Song("See You Again ","Wiz Khalifa",uri.toString(),"https://drive.google.com/u/0/uc?id=1HFej35O30ayi75UsOkWtc5PSYP3kF-Gc&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/we_are_the_champions_photo");
            m_SongList.add(new Song("We are the champions","Queen",uri.toString(),"https://drive.google.com/u/0/uc?id=12E4UJBKoN4A3T-oSwdBlcZrtiBcmDc7b&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/we_will_rock_you_photo");
            m_SongList.add(new Song("We will rock you","Queen",uri.toString(),"https://drive.google.com/u/0/uc?id=1rsrpQQN8N_XTpxNsoOkXuDVWZ-IsAGz7&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/all_off_me_photo");
            m_SongList.add(new Song("All of Me","John Legend",uri.toString(),"https://drive.google.com/u/0/uc?id=1ntzJM_911xG3WoYAl8VVPHOi-lZbbf7B&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/haleluja_photo");
            m_SongList.add(new Song("Hallelujah","Jeff Buckley",uri.toString(),"https://drive.google.com/u/0/uc?id=11yoQrcs-3AwPi-A0L8Fb3Zd_cFBitya6&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/i_never_love_again_photo");
            m_SongList.add(new Song("I'll Never Love Again","Lady Gaga",uri.toString(),"https://drive.google.com/u/0/uc?id=1OUHWbdefacY-5Ho03oekCOujWBju7r8A&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/lady_gaga_bradley_cooper_shallow_photo");
            m_SongList.add(new Song("Shallow","Lady Gaga & Bradley Cooper",uri.toString(),"https://drive.google.com/u/0/uc?id=1v9W8yC4Sm8YGo8RzfCtcNDUyVHKNpLFP&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/remember_us_photo");
            m_SongList.add(new Song("Always Remember Us","Lady Gaga",uri.toString(),"https://drive.google.com/u/0/uc?id=1FZe1Qtr-3-G5wXnre9IYlfeKDLJGNrc5&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/someone_like_you_photo");
            m_SongList.add(new Song("Someone like you","Adele",uri.toString(),"https://drive.google.com/u/0/uc?id=1rFIyoF1qWxpZtRXaRTLahONCb2zCplnL&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/rolliing_deep_photo");
            m_SongList.add(new Song("Rolling in the Deep","Adele",uri.toString(),"https://drive.google.com/u/0/uc?id=1eRU2Q6CObFG7gfphiYruykil99rLgYCN&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/california_here_we_come_photo");
            m_SongList.add(new Song("California","Phantom Planet with Lyrics",uri.toString(),"https://drive.google.com/u/0/uc?id=1xgbm-5x5Zau-AlXYhya02p0v1p6OMYCl&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/new_devide_photo");
            m_SongList.add(new Song("New Divide","Linkin Park",uri.toString(),"https://drive.google.com/u/0/uc?id=1cbXnDO9CkRrmaRHOxxymSlT1F0jwvvqc&export=download"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/wavin_flag_photo");
            m_SongList.add(new Song("Wavin' Flag","K'NAAN",uri.toString(),"https://drive.google.com/u/0/uc?id=1br80_6ZzCxr3F11zvVP0RHtIT2tv2h1E&export=download"));


            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/the_man_in_me_photo");
            m_SongList.add(new Song("The man in me","Bob Dylen",uri.toString(),"http://www.syntax.org.il/xtra/bob2.mp3"));

            uri = Uri.parse("android.resource://com.example.musicplayer/drawable/sara_photo");
            m_SongList.add(new Song("Sara","Bob Dylen",uri.toString(),"http://www.syntax.org.il/xtra/bob1.m4a"));

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
