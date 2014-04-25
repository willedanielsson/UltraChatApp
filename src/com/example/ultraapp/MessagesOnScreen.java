package com.example.ultraapp;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class MessagesOnScreen extends Activity {

	Context c;
	AppSettings settings; 

	public MessagesOnScreen(Context c){
		this.c = c;
		settings = new AppSettings(c);
	}

	/*
	 * Make a toast message if status messages are accepted.
	 */
	public void showStatus(String message){
		if (settings.getShowStatus()) show(message);
	}

	/*
	 * Makes a toast message in screen.
	 * Parameter: Message to show. 
	 */
	public void show(CharSequence text) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(c, text, duration);
		toast.show();
	}
}
