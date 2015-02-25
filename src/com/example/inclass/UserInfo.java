/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.util.ArrayList;

public class UserInfo {
	
	private String uMail;
	private String userName;
	private String realName;
	private String birthDay;
	private String gender;
	private String photo;
	
	private ArrayList<ClassInfo> taking;
	
	public UserInfo(String _userName, String _realName, String umail,String _birthDay,
			ArrayList<ClassInfo> _taking, String _gender,String _photo){
		// There is a key called uId
		
		uMail=umail;
		userName = _userName;
		realName = _realName;
		birthDay = _birthDay;
		taking = _taking;
		gender = _gender;
		photo=_photo;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getRealName(){
		return realName;
	}
	
	public String getumail()
	{
		return uMail;
	}
	
	public String getBirthDay(){
		return birthDay;
	}
	
	public ArrayList<ClassInfo> getClasses(){
		return taking;
	}
	public String getGender(){
		return gender;
	}
	public String getPhoto()
	{
		return photo;
	}
	
	
	

}
