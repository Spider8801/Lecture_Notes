package com.pythonanywhere.cozinfinitybehind.lecture_notes;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pythonanywhere.cozinfinitybehind.lecture_notes.models.PDFModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Button b,showBtn;
    String api = "https://api.myjson.com/bins/1btyuw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetImageFromUrl task = new GetImageFromUrl();
                task.execute(api);
            }
        });

        showBtn= findViewById(R.id.button2);
        showBtn.setActivated(false);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showBtn.isActivated())
                    startActivity(new Intent(MainActivity.this, ListofPages.class));
            }
        });
    }

    //INTIALISING DATABSE

//    private void InitializeDataBase(){
//        try{
//            db= openOrCreateDatabase("lectureNotesDB",Context.MODE_PRIVATE,null);
//
//            String createNoteTableSQL = "create table if not exists notes(" +
//                    "materialId int(5) primary key," +
//                    "name varchar(30) not null," +
//                    "author_name varchar(20)," +
//                    "author_institute varchar(20)"+
//                    ");";
//
//            db.execSQL(createNoteTableSQL);
//
//            String createPageTable = "create table if not exists pages("+
//                    "page_no int(5) not null,"+
//                    "page_image_url varchar(256) not null,"+
//                    "FOREIGN KEY (materialId) REFERENCES Notes(materialId)"+
//                    ");";
//            // "FOREIGN KEY (materialId) REFERENCES Notes(materialId)
//
//
//            db.execSQL(createPageTable);
//
//
//        }
//        catch (SQLException ex){
//            ex.printStackTrace();
//        }
//
//    }

    public class GetImageFromUrl extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... strings) {

            String jsonApiUrl=strings[0];
            Bitmap imageBitmap=null;
            URL url = null;
            //HttpURLConnection httpURLConnection = null;
            StringBuilder jsonData = new StringBuilder();

            try{
                url = new URL(jsonApiUrl);
                URLConnection httpURLConnection = url.openConnection();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int c=reader.read();
                while(c!=-1){
                    jsonData.append((char) c);
                    c=reader.read();
                }
                Log.e("JSON ", jsonData.toString());
                //int i = jsonData.length();

                JSONObject jsonObject = new JSONObject(jsonData.toString());
                String name = jsonObject.optString("name");
                Integer materialId = jsonObject.optInt("materialId");
                JSONObject author = jsonObject.getJSONObject("author");
                String author_name = author.optString("name");
                String author_institute = author.optString("institute");

                //Inserting Credentials

                HashMap<Integer, String> pagesHashmap = new HashMap<>();
                ArrayList<String> pagesArray = new ArrayList<>();

                JSONArray pages = jsonObject.optJSONArray("pages");
                for(int i=0; i<pages.length();i++){
                    JSONObject page = pages.getJSONObject(i);
                    String pageUrl = page.optString("url");
                    try{
                        InputStream srt = new URL(pageUrl).openStream();
                        imageBitmap = BitmapFactory.decodeStream(srt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    String pagePath =  saveToInternalStorage(imageBitmap, materialId, i+1);
                    Log.e("Page", (i+1)+" "+pagePath);
                    pagesHashmap.put(i+i, pagePath);
                }

                PDFModel model = new PDFModel(name,materialId,author_name,author_institute,pagesHashmap);
                Log.e("MODEL OBJECT", name+","+author_institute);
                saveFileToInternalStorage(model,materialId);


            } catch (Exception e){
                e.printStackTrace();
            }

//            Log.e("From ASYNC TASK", imageBitmap.toString());
            return "ok";
        }

        private void saveFileToInternalStorage(PDFModel model,int materialId){
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File mypath=new File(directory,"notesFile_"+materialId+".txt");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(model);
                Log.e("WRITE FILE","DONE" );
                if(mypath.exists()){
                    Log.e("EXISTS", "TRUE");
                }
                else{
                    Log.e("Exists", "FALSE");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        private String saveToInternalStorage(Bitmap bitmapImage, int materialId, int page){
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            PDFModel m=null;
            // Create imageDir
            File mypath=new File(directory,"notes_"+materialId+"_"+page+".jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mypath.getAbsolutePath();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this,"Note SAVED", Toast.LENGTH_LONG).show();
            showBtn.setActivated(true);

        }
    }

}
