/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;







import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class addument extends Activity 
{
	boolean pic=false;

	String uid;
	mysql ms;
	private static int RESULT_LOAD_IMAGE = 1;
	public final static int REQUEST_CODE_TAKE_PICTURE = 2;
	Uri outPutFile;
	ImageView im;

	String picturePath;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.addment);
		Intent intent=getIntent(); 
		uid=intent.getStringExtra("un");
		
		im=(ImageView)findViewById(R.id.imageView_addument);

		ms=new mysql();

		final EditText  textument=(EditText)findViewById(R.id.textView_addument);
		Button send =(Button)findViewById(R.id.send_addument);
		Button pickpic=(Button)findViewById(R.id.picturechoose_addument);
		Button takepic=(Button)findViewById(R.id.picturetake_addument);
		Button back=(Button)findViewById(R.id.back_addument);
		
		
		back.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v) {
				finish();
				
			}
			
		});
		

		pickpic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				pic=true;
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, RESULT_LOAD_IMAGE);

			}
		});


		takepic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pic=true;

				isExist(Environment.getExternalStorageDirectory()+"/inclass");
				Calendar c = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				String currenttime= df.format(c.getTime());
				//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
				File file = new File(Environment.getExternalStorageDirectory()+"/inclass",
						uid+currenttime+"."+"jpg");
				outPutFile = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutFile);
				//startActivityForResult(intent, takeCode);

				// TODO Auto-generated method stub
				//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  

				//startActivityForResult(intent, 1);  

				startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);  

			}
		});

		new Thread() { 
			public void run() {
				ms.connectDB();
				//latch.countDown();
			} 
		}.start();

		send.setOnClickListener(new View.OnClickListener() 
		{
			String pname="0";

			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub

				if(pic==true)
				{
					Calendar c = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String currenttime= df.format(c.getTime());
					pname=uid+currenttime;



					final File ff= new File(picturePath);

					String exten=getExtensionName(ff.getName());
					Bitmap spic=getSmallBitmap(picturePath);
					
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
					//HttpAssist ha= new HttpAssist();
					//Log.i("ok", "pass");
					new Thread() { 
						public void run() {
							HttpAssist ha= new HttpAssist();
							ha.uploadument();
							ha.setid(pname);
							
							ha.uploadFile(file);
							if(file.exists()){
								file.delete();
								Log.i("OK","delete");
							}
						}
					}.start();

				}
				
				Thread temp =new Thread() 
				{
					public void run()
					{

						String sql="insert into senior_project.Ument (uid, content, picture) values"+ "('"+uid+"','"+textument.getText()+"'," + "'"+pname+"')";		

						Log.e("sql",sql);

						try 
						{
							int rows=ms.execUpdate(sql);

							Log.e("rows",rows+"");
						}
						catch (SQLException e) 
						{

						}
					}
				};
				temp.start();
				try  
				{  
					temp.join();  
				}  
				catch (InterruptedException e)  
				{  
					e.printStackTrace();  
				}  


				finish();
				
				
			}
		});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			//ImageView imageView = (ImageView) findViewById(R.id.imgView);
			im.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			//final File ff= new File(picturePath);
			//HttpAssist ha= new HttpAssist();
			//Log.i("ok", "pass");


		}


		if (resultCode == RESULT_OK)
		{  
			if(requestCode == REQUEST_CODE_TAKE_PICTURE)
			{


				BitmapFactory.Options opts = new BitmapFactory.Options();


				picturePath=outPutFile.getPath();
				Bitmap bitmap = BitmapFactory.decodeFile(outPutFile.getPath(),
						opts);

				im.setImageBitmap(bitmap);

			}
		}

	}


	public static void isExist(String path) {
		File file = new File(path);

		if (!file.exists()) {
			file.mkdir();
		}
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







}