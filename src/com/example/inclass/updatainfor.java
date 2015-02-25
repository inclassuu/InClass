/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class updatainfor extends Activity {

	String type;
	String input;
	String uid;
	
	EditText inputdata;
	Button save;
	Button back;
	ProgressDialog progressDialog;
	public void onCreate(Bundle savedInstanceState) 
	{

		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Intent intent=getIntent(); 
		type=intent.getStringExtra("type");
		uid=intent.getStringExtra("uid");
		
		progressDialog = new ProgressDialog(this);

		if((type.equals("name")||(type.equals("email"))))
		{
			
			input=intent.getStringExtra("input");
			setContentView(R.layout.updatainfo);
			inputdata=(EditText)findViewById(R.id.inputdata);
			save=(Button)findViewById(R.id.updatasave);
			back=(Button)findViewById(R.id.updataback);
			inputdata.setText(input);
			
			save.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					if(!input.equals(inputdata.getText()))
					{
						new MyTaskupdata().execute(type);

					}
					
				}
				
			});
			
			back.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					finish();
					
				}
				
			});
			
			
		
			

		}

		
		else if(type.equals("g"))
		{
			input=intent.getStringExtra("input");
			setContentView(R.layout.updatainfo1);
		
			//inputdata=(EditText)findViewById(R.id.inputdata1);
			//save=(Button)findViewById(R.id.updatasave1);
			save=(Button)findViewById(R.id.updatasave1);
			back=(Button)findViewById(R.id.updataback1);
			
			
			save.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					if(!input.equals(inputdata.getText()))
					{
						new MyTaskupdata().execute(type);

					}
					
				}
				
			});
			
			back.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v) {
					finish();
					
				}
				
			});
			
			
		}
		
		
		

		


	}
	
	private class MyTaskupdata extends AsyncTask<String, Integer, String>
	{
		
		@Override  
		protected void onPreExecute() {  
			//Log.i(TAG, "onPreExecute() called");  
			progressDialog.setTitle("Updata");
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) 
		{
			String sql="";
			if(params[0]=="name")
			{
				sql="UPDATE `senior_project`.`User` SET `realname`='"+input+"' WHERE `uid`='"+uid+"';";
			}
			else if (params[0]=="email")
			{
				sql="UPDATE `senior_project`.`User` SET `umail`='"+input+"' WHERE `uid`='"+uid+"';";
			}
			else if(params[0]=="g")
			{
				sql="UPDATE `senior_project`.`User` SET `gender`='"+input+"' WHERE `uid`='"+uid+"';";
			}
			
			
			// TODO Auto-generated method stub
			return null;
		}  
		
		@Override  
		protected void onPostExecute(String result) {  
			progressDialog.dismiss();
			finish();
		}
	}
	
	
	

}
