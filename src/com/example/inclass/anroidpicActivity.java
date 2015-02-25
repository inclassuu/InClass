/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * In the ument, it lets the picture bigger. 
 */

package com.example.inclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class anroidpicActivity extends  Activity  {

	Bitmap bp=null;  
	
	Bitmap newBitmap=null;
	ImageView imageview;  
	float scaleWidth;  
	float scaleHeight;  
	String url;
	TextView textview;

	int width;
	int height; 
	int w;
	int h;
	boolean num=false;  
	
	private MyTask mTask; 
	//int width;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {  
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		
		super.onCreate(savedInstanceState);  
		
		
		setContentView(R.layout.pic); 
		
		Intent intent=getIntent(); 
		
		 
		url=intent.getStringExtra("url");
		//Display display=getWindowManager().getDefaultDisplay();  
		imageview=(ImageView)findViewById(R.id.imageView_pic); 
		textview=(TextView)findViewById(R.id.textview_pic);
		new MyTask().execute(url);

		//imageview.setImageBitmap(newBitmap);  
		//imageview.setImageBitmap(bp);  

		//loadImage(url);
		
		imageview.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();

			}
		});
	}
	
	
	
	 private class MyTask extends AsyncTask<String, Integer, String> {
		 
	        @Override  
	        protected void onPreExecute() {  
	        	
	          //  Log.i("MyTask", "onPreExecute() called");  
	            textview.setText("loading...");  
 
	        }  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			
            publishProgress(0); //会调用onProgressUpdate更新界面
            
            InputStream inputStream = null;
            //Bitmap imgBitmap = null;
            try {
                URL url = new URL(params[0]);
                if(url != null) {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();
                    if(200 == code) {
                        inputStream = connection.getInputStream();
                        bp = BitmapFactory.decodeStream(inputStream);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            publishProgress(100); 
            //这里不是UI线程，故不能直接setImage(imgBitmap),
            //return imgBitmap;
			return "a";
		}  
		
		
        @Override  
        protected void onProgressUpdate(Integer... progresses) {  
        	
        	Log.i("mytask", "onProgressUpdate(Progress... progresses) called");  
           // progressBar.setProgress(progresses[0]);  
            textview.setText("loading..." + progresses[0] + "%");  
        
        }
        
        @Override  
        protected void onPostExecute(String result) {  
        	
        	if(result==null)
        	{
        		
        	}
        	else
        	Log.i("result",result);
        	imageview.setImageBitmap(bp);  
        }
        
        @Override  
        protected void onCancelled() { 
        	
        	finish();
        
        }
	 }
	






}
