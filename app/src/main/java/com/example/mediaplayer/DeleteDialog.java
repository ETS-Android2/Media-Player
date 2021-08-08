package com.example.mediaplayer;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediaplayer.Model.ApplicationContext;

public class DeleteDialog {

    Context m_context;

    private AlertDialog.Builder m_DeleteDialog_Builder;
    private AlertDialog m_DeleteDialog;
    private View m_DeleteDialogView;
    private Button m_YesBtn, m_NoBtn;
    private int m_Position;
    private MyDeleteListener m_listener;

    interface MyDeleteListener {
        void onYesBtnClicked(int position, View view);
        void onNoBtnClicked(int position, View view);
    }

    public DeleteDialog(Context m_context, MyDeleteListener listener) {
        this.m_context = m_context;
        this.m_listener = listener;
        m_Position = 0;
        m_DeleteDialogView = ((AppCompatActivity)m_context).getLayoutInflater().inflate(R.layout.delete_song_dialog_layout,null, false);
        m_YesBtn = m_DeleteDialogView.findViewById(R.id.yes_btn);
        m_NoBtn = m_DeleteDialogView.findViewById(R.id.no_btn);
        setEndLevelDialog();
    }

    private void setEndLevelDialog()
    {
        m_DeleteDialog_Builder = new AlertDialog.Builder(m_context);
        m_DeleteDialog_Builder.setView(m_DeleteDialogView);
        m_DeleteDialog = m_DeleteDialog_Builder.create();

        m_DeleteDialog.setCancelable(false);

        m_YesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m_DeleteDialog.dismiss();
                if(m_listener!=null)
                {
                    m_listener.onYesBtnClicked(m_Position,view);
                }
            }
        });

        m_NoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                m_DeleteDialog.dismiss();
                if(m_listener!=null)
                {
                    m_listener.onNoBtnClicked(m_Position, view);
                }
            }
        });
    }

    public void show()
    {
        m_DeleteDialog.show();
    }

    public void setPosition(int m_Position) {
        this.m_Position = m_Position;
    }
}
