/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class classmates extends Activity  {

	String uid;
	String cid;
	mysql ms;
	ProgressDialog progressDialog;
	ListView lv;
	Button back;
	MyAdapter myadpter;
	ArrayList<HashMap<String, Object>>mData;
	ImageView photo;
	Button send;
	Context contet;
	
	ChatListWriter chatListWriter;
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ms=new mysql();

		setContentView(R.layout.classmates);
		contet=this;
		progressDialog = new ProgressDialog(this); 

		Intent intent=getIntent(); 



		myadpter= new MyAdapter(this);
		cid=intent.getStringExtra("cid").toString();

		uid=intent.getStringExtra("uid").toString();
		mData= new ArrayList<HashMap<String, Object>>() ;
		lv=(ListView)findViewById(R.id.classmatesview);

		back =(Button)findViewById(R.id.classmatesback);

		new MyTask().execute("");

		back.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				finish();

			}

		});



	}


	public class MyAdapter extends BaseAdapter
	{

		private LayoutInflater mInflater;
		public MyAdapter(Context context) {  
			this.mInflater = LayoutInflater.from(context);  
		}  

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {



			convertView = mInflater.inflate(R.layout.classmateitem , null);
			TextView username= (TextView)convertView.findViewById(R.id.usernameclassmates);
			send =(Button)convertView.findViewById(R.id.sendmessage);
			photo=(ImageView)convertView.findViewById(R.id.list_image4);
			String username1=(String) mData.get(position).get("username");
			username.setText(username1);

			if(mData.get(position).get("photo")!=null)
			{
				ImageLoader imageLoader=new ImageLoader(contet);
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("photo").toString(),photo);
			}

			send.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {

					//String a =mData.get(position).get("username").toString();
					Toast.makeText(getApplicationContext(),mData.get(position).get("uid_").toString(),Toast.LENGTH_SHORT).show();
					chatListWriter = new ChatListWriter(uid, mData.get(position).get("uid_").toString());
					Intent intent = new Intent(contet, ChatClient.class);
					//Intent intent1 = new Intent();
					intent.putExtra("un", uid);
					intent.putExtra("toWhom", mData.get(position).get("uid_").toString());
					intent.putExtra("name", mData.get(position).get("username").toString());
					//mData.get(position).get("uid_").toString()
					//intent.getStringExtra("toWhom");
					
					startActivity(intent);

				}
			});



			return convertView;
		}
	}






	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			progressDialog.setTitle("Load");
			progressDialog.show();
		}  

		@Override
		protected String doInBackground(String... params) {

			//progressDialog.setTitle("Load");

			ms.connectDB();


			publishProgress(0);
			String sql="select uid, username, photo from senior_project.User where uid in (SELECT uid FROM senior_project.Taking where cid='"+cid+"')";
			//ResultSet rs=null;
			try {
				ResultSet rs=null;
				rs=ms.execQuery(sql);
				while(rs.next())
				{

					HashMap<String, Object> map = null;  
					map = new HashMap<String, Object>();
					String username=rs.getString("username");
					map.put("username", username);
					String content=rs.getString("uid");
					map.put("uid_", content);
					map.put("photo", rs.getString("photo"));
					//map.put("name", rs.getString("realname"));

					mData.add(map);


				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			publishProgress(100);
			// TODO Auto-generated method stub
			return null;
		}


		@Override  
		protected void onProgressUpdate(Integer... progresses) { 
			progressDialog.setMessage(progresses[0] + "%");


		}  

		//onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
		@Override  
		protected void onPostExecute(String result) {  

			lv.setAdapter(myadpter);
			progressDialog.dismiss();

		}  
	}








}
