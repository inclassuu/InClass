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

import com.mysql.jdbc.ResultSet;

public class Taking_classSide {

	// class id
	private String cid;
	// all students
	private ClassInfo cls;
	private ArrayList<String> students;
	public Taking_classSide(String _cid){
		cid = _cid;
		cls = new ClassInfo();
		students = new ArrayList<String>();
	}

	public ClassInfo setClass(final mysql ms){
		Thread temp =new Thread() 
		{ 
			public void run()
			{
				try {
					ResultSet results=null;
					String query="select * from senior_project.Course where cid='" + cid + "'";
					results=ms.execQuery(query);
					ClassInfo returnCls = new ClassInfo();	
					while(results.next()){
						returnCls.setCid(""+results.getInt("cid"));
						returnCls.setName(results.getString("cname"));
						returnCls.setClassNumber(""+results.getString("number"));
						returnCls.setDay(results.getString("date"));
						returnCls.setSection(""+results.getInt("section"));
						returnCls.setTime(results.getString("stime"), results.getString("etime"));
						returnCls.setProfessor(results.getString("professor"));
						returnCls.setLocation(results.getString("location"));
						returnCls.setDepartment(results.getString("department"));
					}
					cls = returnCls;
					query = "select realname from senior_project.User where uid in (select uid from senior_project.Taking where cid='"+cid+"')";
					results = ms.execQuery(query);
					while(results.next()){
						students.add(results.getString("realname"));
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
		return cls;
	}

	public ClassInfo getClassInfo(){
		return cls;
	}
	
	public ArrayList<String> getStudents(){
		return students;
	}

}
