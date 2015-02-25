/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageView extends View{
	String msg;
	boolean who;
	View wholeBox;
	TextView msgBox;
	
	public MessageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		msg = "";
		who =false;
		wholeBox = new View(context);
		msgBox = new TextView(context);
	}
	public MessageView(Context context, boolean me, String message){
		super(context);
		msg = message;
		if(me){
			wholeBox = (LinearLayout)findViewById(R.layout.receive_message_box);
			msgBox = (TextView) findViewById(R.id.right_user_chat);
			//msgBox.setText(msg);
		}
	}
}
