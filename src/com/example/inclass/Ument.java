/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mysql.jdbc.ResultSet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Ument {

	private Context context;

	LinearLayout content_layout;

	PullToRefreshListView listument;

	mysql ms;

	TextView textviewument;

	MyAdapter adapter;

	private ListView actualListView;
	private ImageView photo;


	Thread temp;
	InClass in;
	//Button add;

	List<HashMap<String, Object>> mData; 
	public Ument (Context context1, PullToRefreshListView ls,mysql ms1,InClass in1, TextView textviewument1)
	{
		this.context=context1;
		listument=ls;
		ms=ms1;
		in=in1;
		textviewument=textviewument1;
		mData= new ArrayList<HashMap<String, Object>>() ;
		adapter = new MyAdapter(context);
		actualListView = listument.getRefreshableView(); 



		//actualListView.setAdapter(adapter);


		listument.setOnRefreshListener(new OnRefreshListener<ListView>() 
				{  
			@Override  
			public void onRefresh(PullToRefreshBase<ListView> refreshView) 
			{  
				String label = DateUtils.formatDateTime(context, System.currentTimeMillis(),  
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);  

				// Update the LastUpdatedLabel  
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);  

				// Do work to refresh the list here.  
				//new MyTask().execute("");

				if (PullToRefreshBase.Mode.PULL_FROM_START == listument.getCurrentMode()) 
				{
					new MyTask().execute("pull");
				} else if (PullToRefreshBase.Mode.PULL_FROM_END == listument.getCurrentMode())
				{
					new MyTask().execute("end");
					//onLoad
				}
			}
				});  

		listument.setMode(Mode.BOTH);//设置底部下拉刷新模式

		listument.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) 
			{

				
				Intent intent = new Intent(in, Reply.class);
				intent.putExtra("umentid",mData.get(position-1).get("umentid").toString());
				intent.putExtra("name",mData.get(position-1).get("username").toString());
				intent.putExtra("time",mData.get(position-1).get("time").toString());
				intent.putExtra("photo",mData.get(position-1).get("imgbb").toString());
				intent.putExtra("text",mData.get(position-1).get("content").toString());
				intent.putExtra("pic",mData.get(position-1).get("photo").toString());
				
				in.startActivity(intent);  
				
				
				//Toast.makeText(context,mData.get(position-1).get("content").toString(),Toast.LENGTH_SHORT).show();

			}
		});



	}


	public void jumpument(List<HashMap<String, Object>> mData1)
	{
		mData=mData1;
		textviewument.setVisibility(View.GONE);
		actualListView.setAdapter(adapter);
	}

	public ArrayList<HashMap<String, Object>> indata()
	{
		mData=new ArrayList<HashMap<String, Object>>();
		temp=new Thread(new MyThread());
		temp.start();

		try {
			temp.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (ArrayList<HashMap<String, Object>>) mData; 
	}




	public class MyThread implements Runnable
	{
		@Override
		public void run()
		{
			try {
				//mData= new ArrayList<HashMap<String, Object>>() ;

				ResultSet rs=null;
				String sql="select u.username, u.photo,c.* from senior_project.Ument as c left join senior_project.User as u on c.uid = u.uid order by time desc limit 0, 5;";
				rs=ms.execQuery(sql);
				//Message msg = Message.obtain();
				while(rs.next())
				{

					HashMap<String, Object> map = null;  
					map = new HashMap<String, Object>(); 
					//String uid=rs.getString("uid");
					map.put("username", rs.getString("username"));
					String time =rs.getString("time");
					time=time.substring(0,time.toCharArray().length-2);
					map.put("time", time);
					String content=rs.getString("content");
					map.put("content", content);
					//String image =rs.getString("picture");
					map.put("imgbb", rs.getString("picture"));
					map.put("photo",rs.getString("photo"));
					map.put("umentid", rs.getString("mid"));


					mData.add(map);
				}
				//msg.what =1;
				//handler.sendMessage(msg);
			}
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

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
			// TODO Auto-generated method stub


			//View item=mInflater.inflate(R.layout.item1, null);
			ImageLoader imageLoader=new ImageLoader(context); 
			if(mData.get(position).get("imgbb").toString().equals("0"))
			{
				convertView = mInflater.inflate(R.layout.item1, null);


				TextView username= (TextView)convertView.findViewById(R.id.username_ument);
				TextView text=(TextView)convertView.findViewById(R.id.text_ument);
				TextView time=(TextView)convertView.findViewById(R.id.time_ument);

				photo=(ImageView)convertView.findViewById(R.id.list_image);

				username.setText(mData.get(position).get("username").toString());
				time.setText(mData.get(position).get("time").toString());
				text.setText(mData.get(position).get("content").toString());
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("photo").toString(), photo);


			}
			else
			{
				convertView = mInflater.inflate(R.layout.item2, null);

				photo=(ImageView)convertView.findViewById(R.id.list_image2);




				TextView username= (TextView)convertView.findViewById(R.id.username_ument2);
				TextView text=(TextView)convertView.findViewById(R.id.text_ument2);
				TextView time=(TextView)convertView.findViewById(R.id.time_ument2);
				final ImageView im=(ImageView)convertView.findViewById(R.id.imageview_event2);


				username.setText(mData.get(position).get("username").toString());
				time.setText(mData.get(position).get("time").toString());
				text.setText(mData.get(position).get("content").toString());
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("photo").toString(), photo);
				imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("imgbb").toString(), im);


				im.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						//jumpregisterScreen();
						Intent intent = new Intent(in, anroidpicActivity.class);
						intent.putExtra("url","http://senior-07.eng.utah.edu/Ument/"+mData.get(position).get("imgbb").toString() );
						in.startActivity(intent);  

					}
				});
			}




			return convertView;  
		}

	}

	private class MyTask extends AsyncTask<String, Integer, String> {

		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			textviewument.setVisibility(View.VISIBLE);
			textviewument.setText("loading...");  
		}  

		@Override
		protected String doInBackground(String... params) {
			try {
				String sql="";
				if(params[0]=="end")
				{
					sql="select u.username, u.photo,c.* from senior_project.Ument as c left join senior_project.User as u on c.uid = u.uid  where time<'"+mData.get(mData.size()-1).get("time") +  " ' order by time desc limit 0, 5";
				}
				//mData.clear();
				//ArrayList<HashMap<String, Object>>  = new ArrayList<HashMap<String, Object>>(); 
				//
				//mData.remove(0);
				else

				{
					//mData= new ArrayList<HashMap<String, Object>>() ;
					sql="select u.username, u.photo,c.* from senior_project.Ument as c left join senior_project.User as u on c.uid = u.uid  where time>'"+mData.get(0).get("time") +  " ' order by time desc limit 0, 5";
				}
				ResultSet rs=null;
				int i =0;

				rs=ms.execQuery(sql);
				int rowCount =0;
				double ii;

				if (rs.last()) {
					rowCount= rs.getRow();
					rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
				}



				while(rs.next())
				{
					HashMap<String, Object> map = null;  
					map = new HashMap<String, Object>(); 
					//String uid=rs.getString("uid");
					map.put("username", rs.getString("username"));
					String time =rs.getString("time");
					time=time.substring(0,time.toCharArray().length-2);
					map.put("time", time);
					String content=rs.getString("content");
					map.put("content", content);
					//String image =rs.getString("picture");
					map.put("imgbb", rs.getString("picture"));
					if(rs.getString("photo")==null)
					{

						map.put("photo",rs.getString(""));
					}
					else 
					{
						map.put("photo",rs.getString("photo"));
					}

					if(params[0]=="pull")
					{
						mData.add(0,map);
					}
					else
					{
						mData.add(map);
					}
					//rs.getRow();
					i++;
					ii=(float)i/(rowCount+0.0)*100;
					//int rowCount = rs.getRow();
					publishProgress((int)ii);
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				return null;
			}


			return "a";
		} 

		//onProgressUpdate方法用于更新进度信息  
		@Override  
		protected void onProgressUpdate(Integer... progresses) { 

			textviewument.setText("loading..." + progresses[0] + "%");

		}  

		//onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
		@Override  
		protected void onPostExecute(String result) {  
			textviewument.setVisibility(View.GONE);

			if(result==null)
			{

			}

			else 
			{

				//
				adapter.notifyDataSetChanged();    
				listument.onRefreshComplete();  
			}




		}  

		//onCancelled方法用于在取消执行中的任务时更改UI  
		@Override  
		protected void onCancelled() {  

		}  

	}

}




