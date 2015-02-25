package com.example.inclass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class Chathistory {
	static String uid="";




	public Chathistory(String Uid)
	{
		uid=Uid;


	}

	public boolean fileIsExists(String s){
		try{
			File f=new File(s);
			if(!f.exists()){
				return false;
			}

		}catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
		return true;
	}




	public void writemessgae(String toid ,String message,String type)
	{
		if(type.equals("1"))
		{
			message="0"+message;
		}
		else 
		{
			message="1"+message;
		}


		String path="/sdcard/ChatHistory/"+uid;
		File file = new File(path);  
		if(file.exists()==false)
		{
			file.mkdirs();  
		}
		String p = path+File.separator+toid+".txt";
		if(fileIsExists(p)==true)
		{

			try {
				writeTxtFile(message,toid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//rewrite(p,message);
		}
		else
		{
			newwrite(p,message);
		}
	}


	public Boolean  newwrite( String path ,String message){  


		FileOutputStream outputStream = null;  
		try {  
			//创建文件，并写入内容  
			outputStream = new FileOutputStream(new File(path));  
			String msg = new String(message);  
			outputStream.write(msg.getBytes("UTF-8"));  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
			return false;  
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}finally{  
			if(outputStream!=null){  
				try {  
					outputStream.flush();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
				try {  
					outputStream.close();  
				} catch (IOException e) {  
					e.printStackTrace();  
				}  
			}  
		}  
		return true;

	} 

	public static  ArrayList<String> readTxtFile(String toid){

		ArrayList<String>data;
		data= new ArrayList<String>();
		String read;
		BufferedReader bufread;
		//String readStr ="";
		String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		FileReader fileread;
		try {
			fileread = new FileReader(filename);
			bufread = new BufferedReader(fileread);
			try {
				while ((read = bufread.readLine()) != null) {
					data.add(read);
					//readStr = readStr + read;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("文件内容是:"+ "\r\n" + readStr);
		return data;
	}



	public static void delete(String toid){
		String path="/sdcard/ChatHistory/"+uid;
		String p = path+File.separator+toid+".txt";
		File file = new File(p);     
		if(!file.exists())
		{     
			//System.out.println("删除文件失败："+fileName+"文件不存在");     
			//return false;     
		}else
		{     
			if(file.isFile())
			{     
				file.delete();
			}



		}     
	}



	public static String  readTxtFile1(String toid){

		String output="";
		String read;
		BufferedReader bufread;
		//String readStr ="";
		String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		FileReader fileread;
		try {
			fileread = new FileReader(filename);
			bufread = new BufferedReader(fileread);
			try {
				while ((read = bufread.readLine()) != null) {
					if(read=="")
					{

					}
					else
					{
						output=output+read+"\r\n";;
					}
					//readStr = readStr + read;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println("文件内容是:"+ "\r\n" + readStr);
		return output;
	}
	
	public void removeonemessage (ArrayList<String> result,String toid)
	{
		BufferedWriter bw = null;
		String newoutput="";

		for (int i=0;i<result.size();i++)
		{

				newoutput=newoutput+result.get(i)+"\r\n";
		}
		
		RandomAccessFile mm = null;
		//String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		try {
			bw = BufferedWriterFactory.create("/sdcard/ChatHistory/"+uid, toid+".txt");
			bw.write(newoutput);
		}
		catch (IOException e) {
			String exceptionMessage = toid+".txt" + " cannot be re-written.";
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

	public static void writeTxtFile(String newStr,String toid) throws IOException{
		//先读取原有文件内容，然后进行写入操作

		String readStr=readTxtFile1(toid);
		String filein = readStr+newStr+"\r\n";
		RandomAccessFile mm = null;
		String filename="/sdcard/ChatHistory/"+uid+"/"+toid+".txt";
		try {
			mm = new RandomAccessFile(filename, "rw");
			mm.writeBytes(filein);
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		} finally {
			if (mm != null) {
				try {
					mm.close();
				} catch (IOException e2) {
					// TODO 自动生成 catch 块
					e2.printStackTrace();
				}
			}
		}
	}

}
