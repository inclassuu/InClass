/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import java.util.ArrayList;
import java.util.Random;

import android.R.drawable;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;


public class SchedulerView extends View{

	private ClassInfo single_classinfo;
	int classInfoNumber;
	boolean clicked;
	public String day;
	public String startTime;
	public String finTime;
	public String name;
	public String sec;
	public String cid;
	public int top;
	public int bottom;
	

	ArrayList<ClassInfo> classinfo;
	Button btn;

	public SchedulerView(Context context) {
		super(context);
		clicked = false;
		single_classinfo = new ClassInfo();
		single_classinfo = null;
		classInfoNumber = 0;
		btn = new Button(context);
		day = null;
		startTime = null;
		finTime = null;
		name =null;
		sec = null;
		cid = null;
		top = 0;
		bottom = 0;
		
//		recList = new ArrayList<ArrayList<Float>>();
//		recInfo = new ArrayList<Float>();
		// TODO Auto-generated constructor stub
	}
	
	
	public void setSingleClass(ClassInfo single_classinfo2) {
		// TODO Auto-generated method stub
		single_classinfo = single_classinfo2;
	}
	
	public void addList(ArrayList<ClassInfo> _classinfo)
	{
		classinfo=_classinfo;
		
	}
	public ArrayList getClassInfos(){
		return classinfo;
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
			finalV = (time1+time2+time3+time4)+120;
		}

		return finalV;
	}

}