/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

public class ClassInformationPage extends View{
	private ClassInfo cls;
	//includes all students (only id)
	// it will also have linking
	private ArrayList<String> studentIDs;
	private ArrayList<String> students;
	private Taking_classSide taking;
	mysql ms;
	String cid;
	public ClassInformationPage(Context context, mysql _ms, String _cid) throws SQLException {
		super(context);
		// TODO Auto-generated constructor stub
		// actual code 

		ms = _ms;
		cid = _cid;
		taking = new Taking_classSide(cid);
		// test purpse.
		if(cid.equals("")){
			cls = new ClassInfo();
		}
		else
			cls = setupClassInfo();
	}
	private ClassInfo setupClassInfo() throws SQLException {
		// TODO Auto-generated method stub
		
		return taking.setClass(ms);
	}
	
	public ClassInfo returnclassinfo()
	{
		return cls;
	}
	
	public ArrayList<String> getstudents()
	{
		students = taking.getStudents();
		return students;
	}
	

	





}
