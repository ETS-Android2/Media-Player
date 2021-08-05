package com.example.mediaplayer;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mediaplayer.Model.Song;
import com.example.mediaplayer.Model.SongsManager;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class AddSongActivity extends AppCompatActivity {

    private final int WRITE_PERMISSION_REQUEST = 1;
    private final int PICK_FROM_GALLERY = 2;
    private final int CAMERA_REQUEST = 3;
    private ImageButton m_Take_Pic_Btn,m_Pick_Photo_From_Gallery_Btn, m_Add_Song_Btn;
    private File m_File;
    private String m_PhotoPath;
    private SharedPreferences m_SharedPreferences;
    private EditText m_Song_Name_ET, m_Song_Performer_ET, m_Song_Link_ET;
    private int m_CounterPhotos;
    private ImageView m_Song_Photo;
    private Boolean isPhoto = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        if (Build.VERSION.SDK_INT >= 23) {
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            }
        }

        m_SharedPreferences = getSharedPreferences("song_number",MODE_PRIVATE);
        m_CounterPhotos = m_SharedPreferences.getInt("song_number",0);

        m_Song_Name_ET = findViewById(R.id.song_name_matirial_editText);
        m_Song_Performer_ET = findViewById(R.id.song_name_performer_matirial_editText);
        m_Song_Link_ET = findViewById(R.id.song_link_matirial_editText);
        m_Song_Photo = findViewById(R.id.song_photo_add_screen);
        setAddSongBtn();
        setChoosePicBtn();
        setPickSongBtn();
        addSongDialog = new AddSongDialog(this,new AddDialogLisiner());

    }


    private void setAddSongBtn() {
        m_Add_Song_Btn = findViewById(R.id.add_song_btn);
        m_Add_Song_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String songName = m_Song_Name_ET.getText().toString();
                String songPerfomer = m_Song_Performer_ET.getText().toString();
                String songLink = m_Song_Link_ET.getText().toString();

                if(songLink.equals("")|| songName.equals("")|| songPerfomer.equals(""))
                {
                    Toast.makeText(AddSongActivity.this, getString(R.string.fill_all_fileds_string), Toast.LENGTH_LONG).show();
                    return;
                }

                if(!isPhoto)
                {
                    Toast.makeText(AddSongActivity.this, getString(R.string.please_add_photo_string), Toast.LENGTH_LONG).show();
                    return;
                }

                SongsManager.getInstance().addSong(new Song(songName,songPerfomer,m_PhotoPath,songLink));
                SharedPreferences.Editor editor = m_SharedPreferences.edit();
                editor.putInt("song_number",(m_CounterPhotos+1));
                editor.commit();

                addSongDialog.show();
            }
        });
    }

    private void setChoosePicBtn() {

        m_Take_Pic_Btn = findViewById(R.id.take_pic_btn);
        m_Take_Pic_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {
                    int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                    }
                    else
                    {
                        takePic();
                    }
                }
                else
                {
                    takePic();
                }

            }
        });

    }



    private void setPickSongBtn() {
        m_Pick_Photo_From_Gallery_Btn = findViewById(R.id.choose_gall_photo_btn);
        m_Pick_Photo_From_Gallery_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                    }
                    else
                    {
                        choosePic();
                    }
                }
                else
                {
                    choosePic();
                }
            }
        });
    }
    private void choosePic() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
    }

    private void takePic() {
        m_File = new File(getExternalFilesDir(null),"song_pic"+m_CounterPhotos+".jpg");

        Uri imageUri = FileProvider.getUriForFile(
                AddSongActivity.this,
                "com.example.musicplayer.provider", //(use your app signature + ".provider" )
                m_File);


        // Toast.makeText(AddSongActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,CAMERA_REQUEST);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "No Permissions", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = null;

        if(requestCode == PICK_FROM_GALLERY && resultCode == AppCompatActivity.RESULT_OK)
        {
            if (data != null) {
                selectedImageUri = data.getData();
                //m_Song_Photo.setImageURI(selectedImageUri);
                m_PhotoPath = selectedImageUri.toString();
                Glide.with(this).load(m_PhotoPath).into(m_Song_Photo);

                isPhoto = true;
            }
        }

        else {
            if (requestCode == CAMERA_REQUEST && resultCode == AppCompatActivity.RESULT_OK)
            {

                m_PhotoPath = m_File.getAbsolutePath();
                Glide.with(this).load(m_PhotoPath).into(m_Song_Photo);

                isPhoto = true;
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CounterPhotos = m_SharedPreferences.getInt("song_number",0);
    }

    private class AddDialogLisiner implements AddSongDialog.MyAddDialogListener
    {
        @Override
        public void onKeepAddBtnClicked(View view) {
            m_Song_Link_ET.setText("");
            m_Song_Name_ET.setText("");
            m_Song_Performer_ET.setText("");
            m_Song_Photo.setImageResource(R.drawable.ic_add_song_screen_phtos);
            m_PhotoPath = "";
            isPhoto = false;

        }

        @Override
        public void onFinishAddBtnClicked(View view) {
            AddSongActivity.this.onBackPressed();
        }
    }


}