package com.pythonanywhere.cozinfinitybehind.lecture_notes;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pythonanywhere.cozinfinitybehind.lecture_notes.models.PDFModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ListofPages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_pages);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        PDFModel m=null;
        // Create imageDir
        File mypath=new File(directory,"notesFile_2.txt");
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(mypath.getAbsolutePath());
            ObjectInputStream ois = new ObjectInputStream(fis);
            m = (PDFModel)ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("DOWNALODED CLASS", m.getName());




    }
}
