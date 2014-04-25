package com.example.ultraapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSettings {
	
	private SharedPreferences sharedPref;
	
	public AppSettings(Context c){
		sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
	}

	public String getColor() {
		String choosenColor = sharedPref.getString("colors", "");
		if (choosenColor == "") choosenColor = "black";
		return choosenColor;
	}

	public String getUserName(){
		String username = sharedPref.getString("username", "");
		if (username == "") username = "Unknown";
		return username;
	}

	public boolean getShowStatus(){
		boolean showStatus = sharedPref.getBoolean("status_messages", true);
		return showStatus;
	}

}
