package com.example.mediaplayer.Model;

import android.app.Application;
import android.content.Context;

/**
 * Will automatically hold the applications context on creation.
 * In order to use add android:name="com.*pathname*.*projectname*.ApplicationContext"
 * to "application" on the app's manifest.
 */
public class ApplicationContext extends Application
{
    private static Context m_ApplicationContext;

    public void onCreate()
    {
        super.onCreate();
        m_ApplicationContext = getApplicationContext();
    }

    public static Context getContext()
    {
        return m_ApplicationContext;
    }
}
