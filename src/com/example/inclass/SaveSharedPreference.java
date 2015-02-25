/**
 * Team C&K
 * Seongman Kim, Shao Yu, Young Soo Choi, SiCheng Xin
 * 
 * CS4500 Senior Project
 * 
 */
package com.example.inclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SaveSharedPreference {
	static final String PREF_USER_NAME= "username";
	static final String PREF_USER_UID = "0";
	
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserInfo(Context ctx, String userName, String Uid_) 
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_USER_UID, Uid_);
        editor.commit();
    }
    public static void clearPreference(Context ctx) 
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    
    public static String getUserUid(Context ctx)
    {
    	return getSharedPreferences(ctx).getString(PREF_USER_UID, "");
    }
}
