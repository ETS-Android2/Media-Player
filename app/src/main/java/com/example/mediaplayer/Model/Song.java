package com.example.mediaplayer.Model;

import java.io.Serializable;

public class Song implements Serializable
{
    String m_SongName, m_SongPerformer;
    String m_PhotoPath;
    String m_Link;

    public Song() {
    }

    public Song(String m_SongName, String m_SongPerformer, String m_PhotoPath, String m_Link) {
        this.m_SongName = m_SongName;
        this.m_SongPerformer = m_SongPerformer;
        this.m_PhotoPath = m_PhotoPath;
        this.m_Link = m_Link;
    }

    public String getSongName() {
        return m_SongName;
    }

    public String getSongPerformer() {
        return m_SongPerformer;
    }

    public String getPhotoPath() {
        return m_PhotoPath;
    }

    public String getLink() {
        return m_Link;
    }
}
