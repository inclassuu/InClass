package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;

import com.example.inclass.Ument.MyAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mysql.jdbc.ResultSet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;



public class Reply  extends Activity {

	mysql ms;
	String umentid;
	String name_str;
	String time_str;
	String photo_str;
	String text_str;
	String pic_str;
	ArrayList<String>data;
	ImageView userpic;

	PullToRefreshListView listument;

	MyAdapter adapter;
	Context context;

	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reply_list);
		ms= new mysql();
		data=new ArrayList<String>();
		Intent intent=getIntent(); 
		umentid=intent.getStringExtra("umentid");
		name_str=intent.getStringExtra("name");
		time_str=intent.getStringExtra("time");
		text_str=intent.getStringExtra("text");
		photo_str=intent.getStringExtra("photo");
		pic_str=intent.getStringExtra("pic");
		context= this;
		
		new MyTask().execute("");




	}

	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			ms.connectDB();


			ResultSet rs=null;
			String sql="select r.*, u.username from senior_project.Ument_reply as r left join senior_project.User as u on r.uid = u.uid and mid ='"+umentid+"' order by time desc;";

			try {
				rs=ms.execQuery(sql);

				while (rs.next())
				{
					String content=rs.getString("content");
					data.add(content);
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";


		}
		
		
		@Override  
		protected void onPostExecute(String result) {  
		
			listument=(PullToRefreshListView)findViewById(R.id.reply_list);
			adapter=new MyAdapter(context);
			listument.setAdapter(adapter);
			
		}

	}


	public class MyAdapter extends BaseAdapter {  
		private LayoutInflater mInflater;// 动态布局映射 
		Context context;

		public MyAdapter(Context context) { 
			this.context=context;
			this.mInflater = LayoutInflater.from(context);  
		}  

		// 决定ListView有几行可见  
		@Override  
		public int getCount() {  
			// return mData.size();// ListView的条目数  
			return data.size()+1;
		}  

		@Override  
		public Object getItem(int arg0) {  
			return null;  
		}  

		@Override  
		public long getItemId(int arg0) {  
			return 0;  
		}  
		@Override 
		public boolean isEnabled(int position) {
			if(position==0)
			{
			return false;
			}
			return true;
		
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageLoader imageLoader=new ImageLoader(context);
			if(position==0)
			{
				if(photo_str.equals("0"))
				{
					convertView = mInflater.inflate(R.layout.replyitem1, null);
					//convertView.setEnabled(false);


					TextView username= (TextView)convertView.findViewById(R.id.reusername_ument);
					TextView text=(TextView)convertView.findViewById(R.id.retext_ument);
					TextView time=(TextView)convertView.findViewById(R.id.retime_ument);

					userpic=(ImageView)convertView.findViewById(R.id.relist_image);

					username.setText(name_str);
					time.setText(time_str);
					text.setText(text_str);
					imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+pic_str, userpic);
				}

			}
			else
			{
				convertView = mInflater.inflate(R.layout.replyitem, null);
				TextView replycontent=(TextView)convertView.findViewById(R.id.replytext);
				
				replycontent.setText(data.get(position-1));
			}
			return convertView;
		}  
	}  

}
