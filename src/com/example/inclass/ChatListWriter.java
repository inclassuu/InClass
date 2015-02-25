/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 * Chat List Writer as text file.
 */
package com.example.inclass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class ChatListWriter {
	String user_Id;
	String toWhom_Id;
	ArrayList<String> previous_Whoms;
	
	String path;
	String filename;
	String fullFileName;
	File file;
	
	public ChatListWriter(String user_Id, String toWhom_Id){
		previous_Whoms= new ArrayList<String>();
		path = "/sdcard/ChatHistory";
		filename = user_Id + "_chat_list.txt";
		fullFileName = path + "/"+filename;
		
		this.user_Id = user_Id;
		this.toWhom_Id = toWhom_Id;
		
		file = new File(fullFileName);
		if(file.exists()){
			renewFile(toWhom_Id);
		}
		else{
			makeNewFile(filename, toWhom_Id);
		}
	}
	
	private void renewFile(String toWhom_Id2) {
		// TODO Auto-generated method stub
		String textFileName = filename;
		String content = toWhom_Id2;
		BufferedWriter bw = null;
		
		previous_Whoms = readWhoms();
		if(previous_Whoms.contains(toWhom_Id2))
			previous_Whoms.remove(toWhom_Id2);
		else{
			previous_Whoms.add(toWhom_Id2);
		}
		
		try {
			bw = BufferedWriterFactory.create(path, textFileName);
			String tempWriter="";
			for(int i = previous_Whoms.size()-1; i >=0; i --){
				tempWriter = previous_Whoms.get(i) +"\r\n"+tempWriter;
			}
			bw.write(content + "\r\n"+ tempWriter);
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

	private ArrayList<String> readWhoms() {
		// TODO Auto-generated method stub
		ArrayList<String> return_ = new ArrayList<String>();
		
		StringBuilder text = new StringBuilder();
		try {
			BufferedReader buffereader = new BufferedReader(new FileReader(fullFileName));
			String line;
			while ((line = buffereader.readLine()) != null) {
				if(!return_.contains(line)){
					return_.add(line);
				}
			}
		}
		// shoudlnt happen
		catch (IOException e) {
			Log.i("No such file", "No chat list");
		}
		// test purpose
//		for(int j = 0 ; j < .size(); j ++ ){
//			Log.i("test", "UIDS? " + uids.get(j));
//		}
		return return_;
	}

	public void makeNewFile(String filename, String toWhom){
		String textFileName = filename;
		String content = toWhom;
		BufferedWriter bw = null;

		//-----------------------------------------------------
		// "/sdcard/WritingTextIntoSDCard" ������ textFileName ������ ����������.

		try {
			bw = BufferedWriterFactory.create(path, textFileName);
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


}
