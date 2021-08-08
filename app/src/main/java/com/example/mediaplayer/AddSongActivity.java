package com.example.mediaplayer;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import java.io.File;

public class AddSongActivity extends AppCompatActivity {

    private ImageButton m_Take_Pic_Btn,m_Pick_Photo_From_Gallery_Btn, m_Add_Song_Btn, m_Cancel_Btn;
    private File m_File;
    private String m_PhotoPath;
    private SharedPreferences m_SharedPreferences;
    private EditText m_Song_Name_ET, m_Song_Performer_ET, m_Song_Link_ET;
    private int m_CounterPhotos;
    private ImageView m_Song_Photo;
    private Boolean isPhoto = false ;
    private Uri m_TempImageUri;


    ActivityResultLauncher<String> requestPermissionLauncher;
    ActivityResultLauncher<Uri> cameraResultLauncher;
    ActivityResultLauncher<String> pickContentResultLauncher;
    //ActivityResultLauncher<Intent> pickImageResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        initLaunchers();



        if (Build.VERSION.SDK_INT >= 23) {
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        m_SharedPreferences = getSharedPreferences("song_number",MODE_PRIVATE);
        m_CounterPhotos = m_SharedPreferences.getInt("song_number",0);

        m_Song_Name_ET = findViewById(R.id.song_name_matirial_editText);
        m_Song_Performer_ET = findViewById(R.id.song_name_performer_matirial_editText);
        m_Song_Link_ET = findViewById(R.id.song_link_matirial_editText);
        m_Song_Photo = findViewById(R.id.song_photo_add_screen);
        setAddSongBtn();
        setChoosePicBtn();
        setPicSongBtn();
        setCancelBtn();

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

                Toast.makeText(AddSongActivity.this, getResources().getString(R.string.song_added_successfully), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void setPicSongBtn() {

        m_Take_Pic_Btn = findViewById(R.id.take_pic_btn);
        m_Take_Pic_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {

                    if(ActivityCompat.checkSelfPermission(AddSongActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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



    private void setChoosePicBtn() {
        m_Pick_Photo_From_Gallery_Btn = findViewById(R.id.choose_gall_photo_btn);
        m_Pick_Photo_From_Gallery_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    if(ActivityCompat.checkSelfPermission(AddSongActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
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

    private void setCancelBtn() {

        m_Cancel_Btn = findViewById(R.id.cancel_btn);
        m_Cancel_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void choosePic() {


        pickContentResultLauncher.launch("Image/*");

//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        intent.setType("Image/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        pickImageResultLauncher.launch(intent);
    }

    private void takePic() {
        m_File = new File(getExternalFilesDir(null),"song_pic"+m_CounterPhotos+".jpg");

        Uri imageUri = FileProvider.getUriForFile(
                AddSongActivity.this,
                "com.example.mediaplayer.provider", //(use your app signature + ".provider" )
                m_File);

        m_TempImageUri = imageUri;
        cameraResultLauncher.launch(imageUri);
    }



    @Override
    protected void onResume() {
        super.onResume();

        m_CounterPhotos = m_SharedPreferences.getInt("song_number",0);
    }

    private void initLaunchers() {

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(!result)
                {
                    Toast.makeText(AddSongActivity.this, "No Permissions", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result)
                        {
                            m_PhotoPath = m_TempImageUri.toString();
                            Glide.with(AddSongActivity.this).load(m_PhotoPath).into(m_Song_Photo);

                            isPhoto = true;
                        }

                    }
                });


                pickContentResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            m_PhotoPath = result.toString();
                            Glide.with(AddSongActivity.this).load(m_PhotoPath).into(m_Song_Photo);
                            isPhoto = true;
                        }
                    }
                });

//        pickImageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if(result.getResultCode() == Activity.RESULT_OK){
//
//                    Context context = AddSongActivity.this;
//                    Uri pic = result.getData().getData();
//                    if(pic != null)
//                    {
//                        Glide.with(context).load(m_PhotoPath).into(m_Song_Photo);
//                        context.getContentResolver().takePersistableUriPermission(pic, result.getData().getFlags()
//                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION + Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
//                        m_PhotoPath = pic.toString();
//                        isPhoto = true;
//                    }
//
//                }
//            }
//        });
    }



}