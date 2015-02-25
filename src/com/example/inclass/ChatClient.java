/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Chatting Client Side.
 */

package com.example.inclass;

import java.io.*;
import java.util.ArrayList;


import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemLongClickListener;

public class ChatClient extends Activity {
	//View loginView, logoutView;
	public EditText chat_id,chat_txt;
	public ListView chatList;

	private MyServiceConnection conn = null;  

	// Receives history of chatting
	ArrayList<String> chats;
	// helps dynamical change
	MyAdapter adapter;
	Button back;


	// information of chatting.
	String login_id;
	String toWhom_id;
	private Context context;
	String r_msg;
	String name;
	
	



	int mcnt = 0;
	int i = 0;

	private MsgReceiver msgReceiver; 

	private MsgService.MyBinder myBinder;
	Chathistory ch;






	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		
		
		adapter = new MyAdapter(context);
		chats = new ArrayList<String>();

		setContentView(R.layout.message);
		Intent intent=getIntent();
		// grabs sender id and receiver id from another activity
		login_id=intent.getStringExtra("un");
		toWhom_id=intent.getStringExtra("toWhom");
		name=intent.getStringExtra("name");
		
		
		TextView tv=(TextView)findViewById(R.id.who_talking_with);
		tv.setText(name);
		
		ch= new Chathistory(login_id);
		
		//String[] get = ch.readTxtFile1(toWhom_id).split("\n");
		
		chats=ch.readTxtFile(toWhom_id);
		

		//chatting text
		chat_txt = (EditText)findViewById(R.id.chat_txt);
		back = (Button)findViewById(R.id.chatting_back_button);

		chatList =(ListView) findViewById(R.id.chat_list);
		chatList.setAdapter(adapter);

		msgReceiver = new MsgReceiver();  
		IntentFilter intentFilter = new IntentFilter();  
		intentFilter.addAction("com.example.inclass.RECEIVER");  
		registerReceiver(msgReceiver, intentFilter);

		Intent service = new Intent(this,
				MsgService.class);
		conn = new MyServiceConnection();


		this.bindService(service, conn, BIND_AUTO_CREATE); 
		
		
		
		


		Toast.makeText(this, login_id+" send message to "+toWhom_id, Toast.LENGTH_SHORT).show();
		chatList.setOnItemLongClickListener( new OnItemLongClickListener()
				{

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						// TODO Auto-generated method stub
						//Log.e("message", chats.get(position));
						
						AlertDialog.Builder builder = new AlertDialog.Builder(context);  
				         
				        builder.setTitle("Delete Message");  
				        builder.setMessage("Do you want to delete this message?");  
				        builder.setPositiveButton("Yes",  
				                new DialogInterface.OnClickListener() {  
				                    public void onClick(DialogInterface dialog, int whichButton) {  
				                    	chats.remove(position);
				                    	adapter.notifyDataSetChanged();
				                    	
				                    	ch.removeonemessage(chats,toWhom_id);
				                    }  
				                });  
				        builder.setNeutralButton("No",  
				                new DialogInterface.OnClickListener() {  
				                    public void onClick(DialogInterface dialog, int whichButton) {  
				                         
				                    }  
				                });  
				        
				        builder.show();  
						return true;
					}
			
				});


		back.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				unbindService(conn);
				finish();
			}

		});
	}
	
	
	



	/*
	 * sends message to server with protocol
	 * MESSAGE: userId Hello
	 */
	public void mSendClick(View view) throws IOException {
		r_msg = chat_txt.getText().toString();
		myBinder.SendMessage(toWhom_id, r_msg);
		
	}
	
	

	
	











	public void getmessage(String message) {
		//System.out.println("##"+r_msg);
		if(message.equals("ERROR")){
			Log.i("Message","MESSAGE : NOT SENT");
			chats.add("0"+"Error do not send: "+message);
		}
		else if (message.equals("LOGGED_IN")){
			//chats.add(message);
			Log.i("Logged in","MESSGE : LOG IN");
		}

		else if (message.equals("OK")){
			Log.i("Message","MESSAGE : SENT");
			chats.add("0"+r_msg);
			ch.writemessgae(toWhom_id, r_msg,"1");
			adapter.notifyDataSetChanged();
			chat_txt.setText("");
		}
		else if (message.equals("ERROR_CLOSED")){
			Log.i("Message", "MESSAGE : ERROR");
		}
		else{
			message = retreivingMsg(message);
			chats.add("1"+message);
			adapter.notifyDataSetChanged();
		}

	}
	private String retreivingMsg(String msg){
		String _msg = "";
		String[] temp = msg.split(" ", 3);
		toWhom_id = temp[1];
		_msg = temp[2];
		return _msg;
	}
	
	private boolean ismessage(String msg){
		
		if(msg.equals("OK"))
		{
			return true;
		}
		String[] temp = msg.split(" ", 3);
		String id=temp[1];
		String id2=toWhom_id;
		
		if(id.equals(id2))
		{
			return true;
		}
		else
		{

			return false;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			String returnMessage = chats.get(position);
			if(returnMessage.charAt(0)=='0'){ // Me sending
				
				convertView = mInflater.inflate(R.layout.send_message_box, null);
				TextView text = (TextView)convertView.findViewById(R.id.right_user_chat);
				text.setText(returnMessage.substring(1));
				
				

			}
			else{
				convertView = mInflater.inflate(R.layout.receive_message_box, null);
				TextView text = (TextView)convertView.findViewById(R.id.left_user_chat);
				text.setText(returnMessage.substring(1));
			}


			return convertView;  
		}

	}


	public class MyServiceConnection implements ServiceConnection
	{
		//当绑定服务成功的时候会调用此方法
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			myBinder = (MsgService.MyBinder) service;
		}
		public void onServiceDisconnected(ComponentName name)
		{
		}
	}



	public class MsgReceiver extends BroadcastReceiver{  

		@Override  
		public void onReceive(Context context, Intent intent) {  
			//拿到进度，更新UI  
			String msg = intent.getStringExtra("msg"); 
			
			if(ismessage (msg)==true)
			{
				getmessage(msg);
			}


		}  

	}  


}