/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Main Activity, In Class
 */

package com.example.inclass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.example.inclass.Ument.MyThread;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

import android.R.color;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class InClass extends Activity {

	ListView listView = null;
	//private static final ListView lvcomment = null;

	//Views for xmls.
	View settingViewForXML;
	View informationPageForXML;
	View chattingForXML;
	View schedulerForXML;
	View umentview;

	// Scheduler information.
	SchedulerView schedulerView;

	//class information Page
	ClassInformationPage classInformationPage;

	//content_layout
	LinearLayout content_layout;

	Intent i;


	CountDownLatch latch;
	Taking_studentSide ts;
	int classInformationNumber;
	// going to class add page
	Button addb;
	Button logoutb;

	boolean addClicked;
	private static int RESULT_LOAD_IMAGE = 1;
	public final static int REQUEST_CODE_TAKE_PICTURE = 2;


	ImageView im_;

	setting s;


	//AddClassActivity addclass;
	String Uid;
	String cid;
	String Password;
	String username;
	int casenum=0;
	int addcasenm=0;
	boolean first=true;

	int activated;
	boolean close = false;
	Ument um;


	ChatList chat_list;

	//Connection conn;
	mysql ms;

	int ii;
	TextView tv;
	Context context;

	String picturePath ="";

	List<HashMap<String, Object>> mData; 
	Uri outPutFile;

	TextView setting_name;
	TextView setting_email;

	RadioGroup radgroup;
	
	private MsgReceiver msgReceiver; 
	
	










	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;


		
		settingViewForXML = new View(this);
		//chattingForXML = new View(this);
		classInformationNumber = -1;
		Uid="";
		Password="";
		cid="";
		tv = new TextView(this);
		ii=1;
		schedulerView = new SchedulerView (this);
		addClicked = false;
		
		latch = new CountDownLatch(1);
		informationPageForXML = View.inflate(this, R.layout.classinfor, null);
		schedulerForXML = new View(this);

		i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage( getBaseContext().getPackageName() );
		//conn=null;
		ms= new mysql();

		mData = new ArrayList<HashMap<String, Object>>();
		
		   msgReceiver = new MsgReceiver();  
	       IntentFilter intentFilter = new IntentFilter();  
	       intentFilter.addAction("com.example.inclass.RECEIVER");  
	       registerReceiver(msgReceiver, intentFilter);



		if(SaveSharedPreference.getUserName(this).length() == 0)
		{
			// call Login Activity
			Log.i("AutoLogin", "Not Yet");
			setContentView(R.layout.login);
			//Uid="";

			Button login_button=(Button)findViewById(R.id.btnLogin);
			TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

			new Thread() { 
				public void run() {
					ms.connectDB();
					latch.countDown();
				} 
			}.start();

			try {
				latch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			login_button.setOnClickListener(new Button.OnClickListener()
			{

				public void onClick(View v)
				{



					Thread tthread = new checkThread(); 
					EditText ut=(EditText)findViewById(R.id.uid);
					EditText pt=(EditText)findViewById(R.id.password);
					username=ut.getText().toString();

					Password=pt.getText().toString();

					tthread.start();
					try
					{
						tthread.join();  
					}  
					catch (InterruptedException e)  
					{  
						e.printStackTrace();  
					}  
					if(((checkThread) tthread).rcheck()==true)
					{
						setUsername(username);
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
						jumpScheduler();
						
					}
					else 
					{
						Toast.makeText(getApplicationContext(),"please check uid or password",Toast.LENGTH_SHORT).show();
					}

				}
			});

			registerScreen.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					jumpregisterScreen();
				}
			});
		}
		else
		{


			new Thread() { 
				public void run() {
					ms.connectDB();
					latch.countDown();
				} 
			}.start();

			try {
				latch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			Log.i("AutoLogin", "Auto_login with name  " 
					+ SaveSharedPreference.getUserUid(this));
			username = SaveSharedPreference.getUserName(this);
			Uid = SaveSharedPreference.getUserUid(this);
			jumpScheduler();

		}
		

	}



	 public class MsgReceiver extends BroadcastReceiver{  
		  
	        @Override  
	        public void onReceive(Context context, Intent intent) {  
	            String msg = intent.getStringExtra("msg"); 
	            
	            Chathistory ch = new Chathistory(Uid);
	            ChatListWriter chatListWriter;
	            
	            if(msg.startsWith("D")||msg.startsWith("MESSAGE"))
	            {
	            if(msg.startsWith("D"))
	            {
	            	String toid="";
	            	String _msg = "";
	    			String[] temp = msg.split(" ", 4);
	    			toid = temp[2];
	    			_msg = temp[3];
	            
	    			ch.writemessgae(toid, _msg,"2");
	    			chatListWriter = new ChatListWriter(Uid,toid );
	    			Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	    			
	            }
	            
	            
	            if(msg.startsWith("MESSAGE"))
	            {
	            	String toid="";
	            	String _msg = "";
	    			String[] temp = msg.split(" ", 3);
	    			toid = temp[1];
	    			_msg = temp[2];
	            
	    			ch.writemessgae(toid, _msg,"2");
	    			chatListWriter = new ChatListWriter(Uid,toid );
	            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	            }
	            if(activated==2)
	            {
	            try {
					chat_list = new ChatList(Uid,ms);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            setChatList(chat_list);
	            }
	            }
	            else
	            {
	            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	            }
	            
	        }  
	          
	    }  
	 
	 
	 
	 
	  


	private void setUsername(String _username) {
		// TODO Auto-generated method stub
		username = _username;
	}

	public class checkThread extends Thread  
	{
		boolean check=false;
		public void run()  
		{  
			try  
			{  
				try {
					ResultSet rs=null;
					String sql="SELECT uid, password FROM senior_project.User where username='"+username+"'";
					rs=ms.execQuery(sql);
					while(rs.next())
					{
						if(Password.equals(rs.getObject("password")))
						{
							//int uuu;
							Uid=""+((Integer) rs.getObject("uid"));
							//uuu=Uid;
							check=true;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}  
			catch (Exception e)  
			{  
				e.printStackTrace();  
			}  
		}  

		public boolean rcheck()
		{
			return check;
		}
	}  

	public void jumpregisterScreen()
	{
		
		

		setContentView(R.layout.register);
		TextView logins = (TextView) findViewById(R.id.link_to_login);
		final TextView username=(TextView) findViewById(R.id.username);
		final TextView password=(TextView) findViewById(R.id.reg_password);
		final TextView email=(TextView)findViewById(R.id.Email);
		final TextView fullname=(TextView)findViewById(R.id.reg_fullname);
		Button re= (Button)findViewById(R.id.btnRegister);
		re.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 

				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
				if(username.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the username")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show();   
				}
				else if(password.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the password")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						public void onClick(DialogInterface dialoginterface, int i){     
						}   
					}).show(); 

				}
				else if(email.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the E-mail")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show(); 

				}
				else if(fullname.getText().toString().equals(""))
				{
					new AlertDialog.Builder(InClass.this)  
					.setTitle("Caution")  
					.setMessage("Please enter the Fullname")   
					.setPositiveButton("Ok",   
							new DialogInterface.OnClickListener(){  
						public void onClick(DialogInterface dialoginterface, int i){   

						}   
					}).show(); 

				}



				else
				{
					casenum=0;
					//final int casenum=0;
					Thread temp =new Thread() 
					{ 

						String sql="INSERT INTO `senior_project`.`User` (`username`, `password`, `realname`, `gender`, `birthday`, `umail`) VALUES ('"+username.getText().toString()+"', '"+password.getText().toString()+"','"+fullname.getText().toString()+"','M','19920101','"+email.getText()+"')";
						//ResultSet rs=null;

						//rs=ms.execQuery(sql);



						public void run()
						{

							try 
							{
								//Log.i("sql",sql);
								ms.execUpdate(sql);


								sql="SELECT LAST_INSERT_ID();";
								ResultSet rs=ms.execQuery(sql);

								while(rs.next())
								{
									String s=rs.getString("LAST_INSERT_ID()");
									Uid=s;
								}



							}
							catch (SQLException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								Log.i("infor",e.toString());
								if(e.toString().indexOf("umail")!=-1)
								{

									casenum=1;
								}
								else 
								{
									casenum=2;
								}

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
					if(casenum==0)
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("congratulation")  
						.setMessage("congratulation! "+username.getText().toString()+" is register successful")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							public void onClick(DialogInterface dialoginterface, int i){   
							}   
						}).show();   
						jumpScheduler();
					}
					else if(casenum==1)
					{

						new AlertDialog.Builder(InClass.this)  
						.setTitle("Caution")  
						.setMessage("The E-mail has been register")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							public void onClick(DialogInterface dialoginterface, int i){   
								//按钮事件   
								//Toast.makeText(InClass.this, "确定",Toast.LENGTH_LONG).show();  
							}   
						}).show();   
					}
					else if(casenum==2)
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("Caution")  
						.setMessage("The Username has been register")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							public void onClick(DialogInterface dialoginterface, int i){   

							}   
						}).show();  
					}
				}

			}
			


		});


		logins.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) 
			{
				jumplogin();
			}
		});
		
		


	}


	public void jumplogin()
	{
		setContentView(R.layout.login);

		 
		Button login_button=(Button)findViewById(R.id.btnLogin);
		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		login_button.setOnClickListener(new Button.OnClickListener()
		{




			public void onClick(View v)

			{



				Thread tthread = new checkThread(); 

				EditText ut=(EditText)findViewById(R.id.uid);
				EditText pt=(EditText)findViewById(R.id.password);
				username=ut.getText().toString();
				Password=pt.getText().toString();

				tthread.start();
				try  
				{  
					tthread.join();  
				}  
				catch (InterruptedException e)  
				{  
					e.printStackTrace();  
				}  

				if(((checkThread) tthread).rcheck()==true)
				{
					jumpScheduler();
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 

					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
				}
				else 
				{
					Toast.makeText(getApplicationContext(),"please check uid or password",Toast.LENGTH_SHORT).show();

				}

			}

		});

		registerScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				jumpregisterScreen();
			}
		});
	}



	public void jumpAddclass(){
		//final String cid="";
		addClicked = false;
		tv.setText("Add"
				+ " Class");
		ii=2;
		View addClassViewForXML = View.inflate(this, R.layout.addclass, null);
		content_layout.removeAllViews();
		content_layout.addView(addClassViewForXML);
		//
		//setContentView(R.layout.addclass);
		addb.setVisibility(View.GONE);

		Button addButton = (Button)findViewById(R.id.add);
		Button searchButton=(Button)findViewById(R.id.searchbutton);
		//i = getBaseContext().getPackageManager()
		//		.getLaunchIntentForPackage( getBaseContext().getPackageName() );
		final ListView listview = (ListView) findViewById(R.id.listclass);
		listview.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HashMap<String,String> map=(HashMap<String,String>)listview.getItemAtPosition(arg2);

				cid=map.get("cid");
			}
		});

		addButton.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				//Log.i("SQL ERROR" , "SQL ERROR : get in here?");
				addClicked = true;
				if(addClicked){
					final String sql="INSERT INTO senior_project.Taking (`cid`, `uid`) VALUES ('"+cid+"','"+Uid+"')";

					Thread temp =new Thread() 
					{ 
						public void run()
						{
							try 
							{
								ms.execUpdate(sql);
							}
							catch (SQLException e) 
							{
								e.printStackTrace();
								Log.i("SQL ERROR" , "SQL ERROR : " + e.toString());
								//if(e.)
								addcasenm=1;

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

					if(addcasenm==1)
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("Caution")  
						.setMessage("You have already added this class")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							public void onClick(DialogInterface dialoginterface, int i){   
							}   
						}).show();   
						jumpScheduler();
					}
					else 
					{
						new AlertDialog.Builder(InClass.this)  
						.setTitle("")  
						.setMessage("Add class sucessful")   
						.setPositiveButton("Ok",   
								new DialogInterface.OnClickListener(){  
							public void onClick(DialogInterface dialoginterface, int i){   
							}   
						}).show();   
						jumpScheduler();
					}
				}
				jumpScheduler();



			}           

		});



		searchButton.setOnClickListener(new Button.OnClickListener(){


			public void onClick(View v) {
				//				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
				//
				//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  

				final ArrayList<HashMap<String,String>> myArrayList=new ArrayList<HashMap<String,String>>();   


				///   test;;;;


				final EditText ecn= (EditText)findViewById(R.id.classnumber);
				final EditText esbj= (EditText)findViewById(R.id.subject);
				//String sql="SELECT * FROM developer01_test.class where dep=+\""+esbj+ "\"and class_num="+ecn+"\"";

				Thread temp =new Thread()  
				{ 
					public void run()
					{
						try {
							ResultSet rs=null;
							String sql="SELECT * FROM senior_project.Course where department=\""+esbj.getText().toString().toUpperCase()+ "\"and number="+"\""+ecn.getText()+"\"";
							rs=ms.execQuery(sql);
							while(rs.next())
							{
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("cid", (String)(rs.getString("cid")+""));

								map.put("name", (String) rs.getObject("cname"));
								int sec=(Integer) rs.getObject("section");
								String secs="";
								if(sec>=10)
								{
									secs="0"+sec+"";
								}
								else 
								{
									secs="00"+sec+"";
								}



								map.put("pro", "Instructor:	"+(String) rs.getObject("professor"));
								map.put("location", "Location:	"+(String) rs.getObject("location"));
								map.put("time", "Time:			"+(String)rs.getObject("stime")+"--"+(String)rs.getObject("etime")+" "+(String)(rs.getObject("date")+""));;

								map.put("subject", rs.getString("department")+""+(String)(rs.getString("number")+"")+" - "+secs);

								myArrayList.add(map); 

							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
				listclass(myArrayList);
			}           
		});
	}
	public void listclass(ArrayList<HashMap<String,String>> myArrayList)
	{
		final ListView listview = (ListView) findViewById(R.id.listclass); 
		SimpleAdapter mySimpleAdapter=new SimpleAdapter(this,   
				myArrayList,  
				R.layout.list_item,
				new String[]{"subject","name","pro","location","time"},  
				new int[]{R.id.name,R.id.subject,R.id.teacher,R.id.location,R.id.time});   

		listview.setAdapter(mySimpleAdapter); 	
	}
	public void jumpScheduler()
	{
		if(SaveSharedPreference.getUserName(this).length()==0){
			SaveSharedPreference.setUserInfo(this, username, Uid);
		}
		
		if(first==true)
		{
		Intent serviceIntent = new Intent(this, MsgService.class);
		serviceIntent.putExtra("Uid", Uid);
		startService(serviceIntent);
		first=false;
		}

		/**
		 * TEST
		 */

		Log.i("AutoLogin", "Auto_going to scheduler");
		Log.i("My username", "auto " +username);
		Log.i("My Uid", "auto " + Uid);
		ts=new Taking_studentSide(Uid,username);
		ts.getClasses(ms);

		//logoutb=(Button) findViewById(R.id.logOut);
		schedulerView = new SchedulerView(this);
		schedulerView.addList(ts.getClasses());

		ii=3;
		i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage( getBaseContext().getPackageName() );
		setContentView(R.layout.activity_in_class);

		activated = 0; 
		tv= (TextView) findViewById(R.id.tt1);
		addb=(Button)findViewById(R.id.addbutton_activity);
		//logoutb=(Button)findViewById(R.id.logOut);
		//tv.setBackgroundColor(Color.RED);
		//tv.setTextColor(Color.WHITE);

		content_layout = (LinearLayout) findViewById(R.id.cont_layer);

		content_layout.removeAllViews();
		content_layout = (LinearLayout) findViewById(R.id.cont_layer);

		schedulerForXML = View.inflate(context, R.layout.calendar,  null);
		content_layout.addView(schedulerForXML);
		//content_layout.addView(schedulerView);
		DrawScheduler(schedulerView);
		//schedulerView.setOnTouchListener(this);
		//schedulerView.setSingleClass(classinfo_);

		final Button button_scheduler = (Button) findViewById(R.id.scheduler_button);
		final Button button_ument = (Button) findViewById(R.id.ument_button);
		final Button button_message = (Button) findViewById(R.id.message_button);
		final ImageButton button_setting = (ImageButton) findViewById(R.id.setting_button);
		addb.setVisibility(View.VISIBLE);
		schedulerForXML = View.inflate(this,  R.layout.calendar, null);
		schedulerForXML.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		button_scheduler.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				activated=0;
				addb.setBackgroundResource(R.drawable.title_background);
				addb.setText("+");
				content_layout.removeAllViews();
				content_layout.addView(schedulerForXML);
				DrawScheduler(schedulerView);
				tv.setText("Scheduler");
				addb.setVisibility(View.VISIBLE);
				button_scheduler.setBackgroundResource(R.drawable.scheduler_clicked);
				button_message.setBackgroundResource(R.drawable.message_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_unclicked);
				button_ument.setBackgroundResource(R.drawable.ument_unclicked);
			}
		});
		button_ument.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				FileCache ff= new FileCache(context);
				ff.clear();
				// TODO Auto-generated method stub
				activated=1;
				addb.setBackgroundResource(R.drawable.title_background);
				addb.setText("+");
				addb.setVisibility(View.VISIBLE);
				content_layout.removeAllViews();
				//content_layout.addView(textument);
				umentview = View.inflate(context, R.layout.umentview, null);
				content_layout.addView(umentview);
				PullToRefreshListView lv =(PullToRefreshListView)findViewById(R.id.umentlistview);
				TextView textument=(TextView)findViewById(R.id.textument);

				//um= new Ument(context,lv,ms,InClass.this,textument);
				um= new Ument(context,lv,ms,InClass.this,textument);

				if(mData.size()==0)
				{
					mData=um.indata();
				}


				um.jumpument(mData);
				tv.setText("Ument");

				//content_layout.removev
				//addb.setVisibility(View.GONE);


				button_scheduler.setBackgroundResource(R.drawable.scheduler_unclicked);
				button_message.setBackgroundResource(R.drawable.message_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_unclicked);
				button_ument.setBackgroundResource(R.drawable.ument_clicked);


			}
		});

		chattingForXML = View.inflate(this, R.layout.chat_list, null);
		button_message.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activated=2;
				addb.setBackgroundResource(R.drawable.title_background);
				tv.setText("Message");
				//addb.setVisibility(View.GONE);
				content_layout.removeAllViews();
				content_layout.addView(chattingForXML);


				try {
					chat_list = new ChatList(Uid,ms);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!chat_list.hasChat()){
					Toast.makeText(getApplicationContext(),"You do not have history",Toast.LENGTH_SHORT).show();
				}
				else{
					setChatList(chat_list);
				}


				button_scheduler.setBackgroundResource(R.drawable.scheduler_unclicked);
				button_message.setBackgroundResource(R.drawable.message_clicked);
				button_ument.setBackgroundResource(R.drawable.ument_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_unclicked);
			}

		});
		settingViewForXML = View.inflate(this, R.layout.setting, null);



		button_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				activated=3;
				//content_layout.removeAllViewsInLayout();
				tv.setText("Setting");
				content_layout.removeAllViews();
				//setContentView(R.layout.setting);
				content_layout.addView(settingViewForXML);
				//content_layout.addView(tempor);
				//				addb.setBackgroundDrawable(R.drawable)
				addb.setBackgroundResource(R.drawable.logout);
				addb.setText("");

				addb.setVisibility(View.VISIBLE);

				button_scheduler.setBackgroundResource(R.drawable.scheduler_unclicked);
				button_message.setBackgroundResource(R.drawable.message_unclicked);
				button_ument.setBackgroundResource(R.drawable.ument_unclicked);
				button_setting.setBackgroundResource(R.drawable.setting_clicked);
				ListView listclass=(ListView) findViewById(R.id.classlist);

				setting_name=(TextView)findViewById(R.id.settingname);
				setting_email=(TextView)findViewById(R.id.settingEmail);

				radgroup = (RadioGroup) findViewById(R.id.radioGroup);








				s= new setting(Uid, ms, context,ts.getClasses());

				s.addclasslist(listclass, ts.getClasses());
				im_=(ImageView) findViewById(R.id.setting_image);

				s.load(setting_name,setting_email,radgroup,im_);


				RelativeLayout piclayout = (RelativeLayout) findViewById(R.id.settingpic_);



				piclayout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {


						AlertDialog.Builder builder = new Builder(context);


						builder.setTitle("Choose");

						builder.setPositiveButton("Gallery", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								Intent i = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

								startActivityForResult(i, RESULT_LOAD_IMAGE);
								dialog.dismiss();

								//Main.this.finish();
							}
						});

						builder.setNegativeButton("Camera", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								isExist(Environment.getExternalStorageDirectory()+"/inclass");
								//Calendar c = Calendar.getInstance();
								File file = new File(Environment.getExternalStorageDirectory()+"/inclass",
										Uid+"."+"jpg");
								outPutFile = Uri.fromFile(file);

								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutFile);
								//startActivityForResult(intent, takeCode);

								// TODO Auto-generated method stub
								//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  

								//startActivityForResult(intent, 1);  

								startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);  


								dialog.dismiss();
							}
						});

						builder.create().show();

					}

				});



			}
		});

		//Button updata=(Button)findViewById(R.id.updata);







		addb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(activated==0)
				{
					jumpAddclass();
				}
				else if(activated==1)
				{
					//Toast.makeText(getApplicationContext(),"this is ument add",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(InClass.this, addument.class);

					intent.putExtra("un", Uid);

					startActivity(intent);


				}
				else if(activated==3){
					SaveSharedPreference.clearPreference(context);
					try {
						exit_program();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	protected void setChatList(ChatList chat_list) {
		// TODO Auto-generated method stub

		ListView chat_list_view = (ListView) findViewById(R.id.chatting_list);
		ChatList_Adapter adapter = new ChatList_Adapter(context, Uid, chat_list);

		
		chat_list_view.setAdapter(adapter);

	}


	public static void isExist(String path) {
		File file = new File(path);

		if (!file.exists()) {
			file.mkdir();
		}
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
			im_.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			s.setpic(picturePath);




		}

		if (resultCode == RESULT_OK)
		{  
			if(requestCode == REQUEST_CODE_TAKE_PICTURE)
			{


				BitmapFactory.Options opts = new BitmapFactory.Options();


				picturePath=outPutFile.getPath();
				Bitmap bitmap = BitmapFactory.decodeFile(outPutFile.getPath(),
						opts);

				im_.setImageBitmap(bitmap);

				s.setpic(picturePath);

			}
		}
	}
	
	
	



	@Override
	public void onBackPressed(){
		close = false;
		setContentView(R.layout.exit_dialog);
		Button yes = (Button) findViewById(R.id.btn_yes);
		Button no = (Button) findViewById(R.id.btn_no);
		yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//				FileCache f= new FileCache(context);
				//				f.clear();
				try {
					exit_program();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ii==2)
				{
					jumpAddclass();
				}
				else if(ii==3)
				{
					jumpScheduler();
				}
				else if(ii==1)
				{
					startActivity(i);
					System.exit(0);
				}
			}
		});
	}

	private void exit_program() throws SQLException{

		FileCache f= new FileCache(context);
		f.clear();
		
        Intent stopIntent = new Intent(this, MsgService.class);  
        stopService(stopIntent);  
        ms.closeConn();
        
		finish();
	}

	TextView classcomment;
	TextView lvcomment;





	private class MyTaskcomment extends AsyncTask<String, Integer, String> {

		List<String> listcomment = new ArrayList<String>();

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String sql="select u.username, c.* from senior_project.Course_comment as c left join senior_project.User as u on c.uid = u.uid where cid='"+classInformationNumber+"'order by time desc;";
			ResultSet rs=null;
			try {
				rs=ms.execQuery(sql);
				while(rs.next())
				{


					String username=rs.getString("username");
					String content=rs.getString("content");
					listcomment.add(username+": "+content);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(String result) {  
			String d="";
			for(String s:listcomment)
			{
				d=d+s+"\n";
			}
			lvcomment.setText(d);

		}

	}



	public class sendcomment implements Runnable
	{
		@Override
		public void run()
		{
			try {
				//mData= new ArrayList<HashMap<String, Object>>() ;

				//ResultSet rs=null;

				//INSERT INTO `senior_project`.`Course_comment` (`cid`, `uid`, `content`) VALUES ('1', '1', '1');

				String sql="insert into senior_project.Course_comment(cid, uid, content) VALUES ('"+classInformationNumber+"','"+Uid+"','"+classcomment.getText()+"')";


				int rows=ms.execUpdate(sql);
				//rs=ms.execQuery(sql);
				//Message msg = Message.obtain();

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
	private void DrawScheduler(SchedulerView schedulerView2) {
		// TODO Auto-generated method stub
		ArrayList<ClassInfo> temp = schedulerView2.getClassInfos();

		ArrayList<String> dayList = new ArrayList<String>();
		int count = 0;

		RelativeLayout ll = new RelativeLayout(context);
		String days = null;
		boolean online = false;
		String day;
		String startTime;
		String finTime;
		String name;
		String name2;
		String sec;
		String cid2;
		int top, bottom;

		for(int classIndex = 0; classIndex < temp.size(); classIndex++){

			temp.get(classIndex);

			day = temp.get(classIndex).getDay();

			startTime = temp.get(classIndex).getStartTime();
			finTime = temp.get(classIndex).getEndTime();
			name = temp.get(classIndex).getDepartment();
			name2 = temp.get(classIndex).getClassNumber();
			sec = temp.get(classIndex).getSection();

			cid2 = temp.get(classIndex).getCid();
			final int cid3 = Integer.parseInt(cid2);

			//float cid2= Float.parseFloat(cid);
			top = time(startTime);
			bottom = time(finTime);

			if(day.equals("TBA"))
			{
				day = "X";
			}

			for(int i = 0; i<day.length(); i++)
			{
				dayList.add(i, Character.toString(day.charAt(i)));
			}
			Random rand = new Random();
			int r = rand.nextInt();
			int g = rand.nextInt();
			int b = rand.nextInt();
			for(int i = 0; i<day.length(); i++)
			{

				if(dayList.get(i).equals("X"))
				{
					online = true;
				}
				else if(dayList.get(i).equals("S"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout4);
					online = false;
					count++;
				}
				else if(dayList.get(i).equals("M"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout5);
					online = false;
					count++;
				}
				else if(dayList.get(i).equals("T"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout6);
					online = false;
					count++;
				}
				else if(dayList.get(i).equals("W"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout7);
					online = false;
					count++;
				}
				else if(dayList.get(i).equals("H"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout8);
					online = false;
					count++;
				}
				else if(dayList.get(i).equals("F"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout9);
					online = false;
					count++;
				}
				else if(dayList.get(i).equals("A"))
				{
					ll = (RelativeLayout) findViewById(R.id.relativeLayout10);
					online = false;
					count++;
				}

				Button btn = new Button(this);
				btn.setLayoutParams(new LayoutParams(300,bottom-top));
				btn.setY(top);
				btn.setId(i);

				btn.setTextSize(9);

				btn.setText(name+"\n"+name2+"\n"+"00"+sec);

				btn.setBackgroundColor(Color.rgb(r, g, b));

				btn.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						int action = event.getAction();
						if(action == MotionEvent.ACTION_DOWN)
						{


							classInformationNumber = cid3;
							Log.i("Touch Event", ""+classInformationNumber);
							try {
								classInformationPage= new ClassInformationPage(context, ms, ""+classInformationNumber);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}


							addb.setVisibility(View.GONE);

							content_layout = (LinearLayout) findViewById(R.id.cont_layer);


							content_layout.removeAllViews();


							informationPageForXML=View.inflate(context, R.layout.classinfor, null);

							content_layout.addView(informationPageForXML);
							//jumpDeleteclass();
							TextView classname = (TextView)findViewById(R.id.classname);
							classname.setText(classInformationPage.returnclassinfo().getName());
							TextView classnumber=(TextView)findViewById(R.id.classid);
							classnumber.setText(classInformationPage.returnclassinfo().getClassNumber_()+"-"+classInformationPage.returnclassinfo().getSection());
							TextView profeesor=(TextView)findViewById(R.id.pro);
							profeesor.setText("Professor: "+classInformationPage.returnclassinfo().getProfessor());
							TextView location=(TextView)findViewById(R.id.location);
							location.setText("Location: "+classInformationPage.returnclassinfo().getLocation());
							TextView time=(TextView)findViewById(R.id.time);
							time.setText("Time: "+classInformationPage.returnclassinfo().getDay()+" "+classInformationPage.returnclassinfo().getStartTime()+"-"+classInformationPage.returnclassinfo().getEndTime());
							tv.setText("Class Information");



							Button classmates= (Button)findViewById(R.id.classmates);
							classcomment =(TextView)findViewById(R.id.commenttext);
							lvcomment= (TextView)findViewById(R.id.commentlistview);
							TextView morecomment=(TextView)findViewById(R.id.morecomment);
							//			

							Button addcomment=(Button)findViewById(R.id.commentbutton);
							new MyTaskcomment().execute("");


							Button button_deleteClass;


							button_deleteClass = (Button)findViewById(R.id.delete);
							Log.i("","Come here?");
							button_deleteClass.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									new MyTaskdelet().execute("");
									//jumpScheduler();
								}});



							classmates.setOnClickListener(new View.OnClickListener()
							{

								@Override
								public void onClick(View v) {

									Intent intent = new Intent(InClass.this, classmates.class);

									intent.putExtra("uid", Uid);
									intent.putExtra("cid", classInformationPage.returnclassinfo().getCid());


									startActivity(intent);


								}

							});

							morecomment.setOnClickListener(new View.OnClickListener()
							{

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(InClass.this, moreclasscomment.class);



									intent.putExtra("cid", classInformationNumber+"");

									startActivity(intent);

								}

							});
							addcomment.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Thread temp=new Thread(new sendcomment());


									temp.start();


									try {
										temp.join();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									Toast.makeText(getApplicationContext(),"you add comment '"+classcomment.getText()+"' for this class",Toast.LENGTH_SHORT).show();
									classcomment.setText("");

									new MyTaskcomment().execute("");

								}
							});





						}

						return false;
					}
				});

				Log.i("TEST", "Where does the error...?");

				ll.addView(btn);

			}
		}
	}

	private int time(String time)
	{

		char hour = ' ';
		char hour2 = ' ';
		char min = ' ';
		char min2 = ' ';
		char ampm = ' ';
		if(time.equals("TBA"))
		{
			return 0;
		}
		hour = time.charAt(0);
		hour2 = time.charAt(1);
		min = time.charAt(3);
		min2 = time.charAt(4);
		ampm = time.charAt(5);

		String s = Character.toString(hour);
		String s1 = Character.toString(hour2);
		String s2 = Character.toString(min);
		String s3 = Character.toString(min2);
		String s4 = Character.toString(ampm);

		int x = Integer.parseInt(s);
		int x1 = Integer.parseInt(s1);
		int x2 = Integer.parseInt(s2);
		int x3 = Integer.parseInt(s3);

		int time1 = x*1200;
		int time2 = x1*120;
		int time3 = x2*(120/6);
		int time4 = x3*(12/6);
		int finalV = 0;
		if(x == 1 && x1 == 2 && s4.equals("P"))
		{
			s4 = "A";
		}
		if(s4.equals("A"))
		{
			finalV = (time1+time2+time3+time4)-840;
		}
		else
		{
			finalV = (time1+time2+time3+time4)+600;
		}
		return finalV;
	}


	private class MyTaskdelet extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) 
		{
			try 
			{

				String sql="DELETE FROM senior_project.Taking where uid='" + Uid + "' and cid='"+classInformationPage.returnclassinfo().getCid()+"'";
				ms.execUpdate(sql);
				return "good";

			} catch (SQLException e) 
			{
				e.printStackTrace();
				return null;
			}

			//return null;
		}


		@Override  
		protected void onPostExecute(String result) 
		{  
			if(result =="good")
			{
				jumpScheduler();
			}

		}
		
		
		

	}
	
	
	


}
