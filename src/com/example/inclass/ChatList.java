/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Creates the chat list.
 */
package com.example.inclass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.example.inclass.Ument.MyThread;
import com.mysql.jdbc.ResultSet;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ChatList {
	// to prevent from duplication when writes file.
	ArrayList<String> uids;
	FileReader reader;
	File chatFile;
	String filename;
	String path;
	boolean has;
	ChatList_Adapter adapter;
	String userid;
	mysql ms;

	ArrayList<HashMap<String, Object>> mData;






	public ChatList(String user_id,mysql ms) throws FileNotFoundException
	{
		has = true;
		this.ms=ms;
		userid=user_id;
		mData= new ArrayList<HashMap<String, Object>> ();
		path = "/sdcard/ChatHistory";
		filename = user_id + "_chat_list.txt";

		uids = new ArrayList<String>();
		chatFile = new File(path+"/"+filename);
		if(chatFile.exists()){
			StringBuilder text = new StringBuilder();
			try {
				BufferedReader buffereader = new BufferedReader(new FileReader(path+"/"+filename));
				String line;
				while ((line = buffereader.readLine()) != null) {
					if(!uids.contains(line)){
						uids.add(line);
					}
				}
			}
			catch (IOException e) {
				Log.i("No such file", "No chat list");
			}
			for(int j = 0 ; j < uids.size(); j ++ ){
				Log.i("test", "UIDS? " + uids.get(j));
			}
		}
		else{
			//fileWriter(filename);
			has = false;
		}
		
		Thread temp=new Thread(new MyThread());
		temp.start();
		try {
			temp.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
	
	
	public class MyThread implements Runnable
	{
		@Override
		public void run()
		{

			for (int i=0;i<uids.size();i++)
			{
				String id=uids.get(i);

				String sql="SELECT * FROM senior_project.User where uid='"+id+"'";
				ResultSet rs=null;
				try {
					rs=ms.execQuery(sql);

					//Message msg = Message.obtain();
					while(rs.next())
					{
						HashMap<String, Object> map = new HashMap<String, Object>() ;  
						String name=rs.getString("username");
						String pic=rs.getString("photo");
						map.put("name", name);
						map.put("pic", pic);
						mData.add(map);
					}
				}



				catch (SQLException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


	public ArrayList<HashMap<String, Object>> getinfo()
	{
		


		
		return mData;
	}
	public boolean hasChat(){
		return has;
	}

	public ArrayList<String> getPeople(){
		return uids;
	}

	public void delete(String toid)
	{
		String newoutput="";
		for(String s: uids ){
			if(s==toid)
			{
				newoutput=newoutput+"";
			}
			else 
			{
				newoutput=newoutput+s+"\r\n";
			}


		}


		filename = userid + "_chat_list.txt";
		filereWriter(filename,newoutput);

	}

	public void filereWriter(String filename,String content){
		String textFileName = filename;
		//String content = "user1";
		BufferedWriter bw = null;

		//-----------------------------------------------------
		// "/sdcard/WritingTextIntoSDCard" ������ textFileName ������ ����������.

		try {
			bw = BufferedWriterFactory.create("/sdcard/ChatHistory", textFileName);
			bw.write(content);
		}
		catch (IOException e) {
			String exceptionMessage = textFileName + " cannot be re-written.";
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (null != bw)
				bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fileWriter(String filename){
		String textFileName = filename;
		String content = "user1";
		BufferedWriter bw = null;

		//-----------------------------------------------------
		// "/sdcard/WritingTextIntoSDCard" ������ textFileName ������ ����������.

		try {
			bw = BufferedWriterFactory.create("/sdcard/ChatHistory", textFileName);
			bw.write(content);
		}
		catch (IOException e) {
			String exceptionMessage = textFileName + " cannot be re-written.";
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (null != bw)
				bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}






}
