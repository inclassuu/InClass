/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Chat List Adapter that takes inputs from the text file written by the other classes.
 * 
 */
package com.example.inclass;

import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.ResultSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatList_Adapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private ArrayList<String> chats;
	
	private Context context;
	private String me;
	
	private ChatList chat_list;
	mysql ms;
	

	//ArrayList<HashMap<String, Object>>mData ;

	public ChatList_Adapter(Context context, String me,ChatList chat_list) {  
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.me = me;
		this.chat_list=chat_list;	
		chats = chat_list.getPeople();
		
		
	
		
		
	}


	
	  public void refresh(ArrayList<String>  list) {  
		  
		  	chats = list;  
	        notifyDataSetChanged();  
	    }  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return chats.size();
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
		final String userId = chats.get(position);
		final Context newContext = context;
		convertView = new View(newContext);
		convertView = mInflater.inflate(R.layout.individual_chat_room, null);
		TextView text = (TextView)convertView.findViewById(R.id.individual_user_context);
		ImageView pic=(ImageView)convertView.findViewById(R.id.individual_user_photo);
		final String name=chat_list.getinfo().get(position).get("name").toString();
		text.setText(chat_list.getinfo().get(position).get("name").toString());
		
		
		ImageLoader imageLoader=new ImageLoader(context); 
		
		if(chat_list.getinfo().get(position).get("pic")!=null)
		{
		imageLoader.DisplayImage("http://senior-07.eng.utah.edu/Ument/"+chat_list.getinfo().get(position).get("pic").toString(), pic);
		}
		
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(newContext, ChatClient.class);
				intent.putExtra("un", me);
				intent.putExtra("toWhom", userId);
				intent.putExtra("name", name);

				
				context.startActivity(intent);
				
			}
		});
		Button deleteButton = (Button) convertView.findViewById(R.id.individual_erase);
		deleteButton.setBackgroundResource(R.drawable.individual_chat_erase_selector);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				
				// TODO Auto-generated method stub
				
				//Log.i("chatsize", chats_map.size()+"");
				chat_list.delete(userId);
				Chathistory ch= new Chathistory(me);
				ch.delete(userId);
				Log.e("posittoon",position+"");
				Log.e("chats",chats.size()+"");
				chats.remove(position);
				
				refresh(chats);

			}
		});
		
		return convertView;  
	}
	
	
	
	


}
	
	
