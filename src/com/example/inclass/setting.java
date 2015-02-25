/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.ResultSet;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class setting {

	private String uid;
	private mysql ms;
	private String im;
	TextView name;
	TextView email;
	ImageView imageview;
	//private Button updata;
	
	ProgressDialog progressDialog;
	UserInfo userinfo;
	
	RadioGroup radgroup;
	ArrayList<ClassInfo> Classinfolist;
	Bitmap bp;



	//private ArrayList<ClassInfo> Classinfolist;

	private Context context;
	public  setting (String uid_,mysql ms_,Context context_,ArrayList<ClassInfo> Classinfolist_)
	{
		uid=uid_;
		ms=ms_;
		context=context_;
		im="";
		Classinfolist=Classinfolist_;
		
		 progressDialog = new ProgressDialog(context); 

	}
	public void load(TextView name_,TextView email_,RadioGroup radgroup_, ImageView _imageview)
	{
		name=name_;
		email=email_;
		radgroup=radgroup_;
		imageview=_imageview;
		new MyTaskload().execute("");
		
	}
	
	public UserInfo getuserinfo()
	{
		return userinfo;
	}
	
	
	private class MyTaskload extends AsyncTask<String, Integer, String>
	{
		
		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
//			progressDialog.setTitle("Load");
//			progressDialog.show();
		}  

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			publishProgress(0);
			try {
				ResultSet rs=null;
				String sql="SELECT * FROM senior_project.User where uid='"+uid+"'";
				rs=ms.execQuery(sql);
				while(rs.next())
				{
					String username=rs.getString("username");
					String realname=rs.getString("realname");
					String email=rs.getString("umail");
					String g=rs.getString("gender");
					String photoname=rs.getString("photo");
					
		
					
					userinfo= new UserInfo(username,realname,email,"",Classinfolist,g,photoname);
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		      //会调用onProgressUpdate更新界面
			
			
	            
	            InputStream inputStream = null;
	            //Bitmap imgBitmap = null;
	            try {
	                //URL url = new URL(params[0]);
	            	URL url=new URL("http://senior-07.eng.utah.edu/Ument/"+userinfo.getPhoto().toString());
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
		
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			
//			if(userinfo.getPhoto()!=null)
//			{
			imageview.setImageBitmap(bp);
//			ImageLoader imageLoader=new ImageLoader(context);  
//			imageLoader.DisplayImage1("http://senior-07.eng.utah.edu/Ument/"+userinfo.getPhoto().toString(), imageview);
//			}
			
			
			
			name.setText(userinfo.getRealName());
			email.setText(userinfo.getumail());
			
			if(userinfo.getGender().equals("M"))
			{
				radgroup.check(R.id.radioButton1);
			}
			else 
			{
				radgroup.check(R.id.radioButton2);
			}
				

		
		}
	}
	
	
		
	
	
	
	private class MyTaskupdata extends AsyncTask<String, Integer, String> {
		
		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			progressDialog.setTitle("Updata");
			progressDialog.show();
		}  
		
		

		@Override
		protected String doInBackground(String... params) {
			
			
			String pname=uid;


			final File ff= new File(im);

			String exten=getExtensionName(ff.getName());
			Bitmap spic=getSmallBitmap(im);

			final File file = new File(Environment.getExternalStorageDirectory()+"/"+pname);
			FileOutputStream fOut;
			try {
				fOut = new FileOutputStream(file);
				spic.compress(Bitmap.CompressFormat.PNG, 85, fOut);
				fOut.flush();
				fOut.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				Log.e(null, "Save file error!");
			}


			pname=pname+"."+exten;

			final HttpAssist ha= new HttpAssist();
			ha.uploadument();
			ha.setid(pname);
			ha.uploadFile(file);
			if(file.exists()){
			file.delete();
			Log.i("OK","delete");
					}
			
			
			String sql="UPDATE `senior_project`.`User` SET `photo`='" +pname+"' WHERE `uid`='"+uid+"'";
		

			Log.e("sql",sql);

			try 
			{
				int rows=ms.execUpdate(sql);

				Log.e("rows",rows+"");
			}
			catch (SQLException e) 
			{

			}
			
		
			return null;
		}
		@Override
		protected void onPostExecute(String result) {  
			FileCache ff= new FileCache(context);
			ff.clear();
			
			progressDialog.dismiss();

		
		}
	
	}
	


		public void setpic(String s)
		{
			im=s;
			new MyTaskupdata().execute("");
		}


		public static String getExtensionName(String filename1) { 
			//String ext="";
			String filename=filename1;
			if ((filename != null) && (filename.length() > 0)) {   
				int dot = filename.lastIndexOf('.');   
				if ((dot >-1) && (dot < (filename.length() - 1))) {   
					return filename.substring(dot + 1);   
				}   
			}
			filename = filename1.replaceAll(filename ,"");
			return filename; 
		}
		
		
	

		public static Bitmap getSmallBitmap(String filePath) {  

			final BitmapFactory.Options options = new BitmapFactory.Options();  
			options.inJustDecodeBounds = true;  
			BitmapFactory.decodeFile(filePath, options);  

			// Calculate inSampleSize  
			options.inSampleSize = calculateInSampleSize(options, 320,480);  

			// Decode bitmap with inSampleSize set  
			options.inJustDecodeBounds = false;  

			Bitmap bm = BitmapFactory.decodeFile(filePath, options);  
			if(bm == null){  
				return  null;  
			}  

			return bm ;  

		}  


		private static int calculateInSampleSize(BitmapFactory.Options options,  
				int reqWidth, int reqHeight) {  
			// Raw height and width of image  
			final int height = options.outHeight;  
			final int width = options.outWidth;  
			int inSampleSize = 1;  

			if (height > reqHeight || width > reqWidth) {  

				// Calculate ratios of height and width to requested height and  
				// width  
				final int heightRatio = Math.round((float) height  
						/ (float) reqHeight);  
				final int widthRatio = Math.round((float) width / (float) reqWidth);  

				// Choose the smallest ratio as inSampleSize value, this will  
				// guarantee  
				// a final image with both dimensions larger than or equal to the  
				// requested height and width.  
				inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;  
			}  

			return inSampleSize;  
		}  






		public void addclasslist(ListView lv,ArrayList<ClassInfo> Classinfolist)
		{
			
			
			


			final ArrayList<String> todoItems = new ArrayList<String>();  
			for (ClassInfo c: Classinfolist)
			{
				String s="";
				s=c.getClassNumber_()+": "+c.getName()+"-"+c.getSection();

				todoItems.add(s);  
			}

			ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,todoItems);
			lv.setAdapter(aa);
		}






	}
