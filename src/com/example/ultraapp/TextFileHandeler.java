package com.example.ultraapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;

public class TextFileHandeler extends Activity {

	public static String MESSAGE_FILE = "ultraAppData34.txt";
	private MessagesOnScreen message;
	private Context c;

	public TextFileHandeler(Context c){
		message = new MessagesOnScreen(c);
		this.c = c; 
	}
	
	/*
	 * Updates chat window with chat history from local file on device.
	 */
	public String loadText() {
		String text = "";

		try {
			InputStream inputStream = c.openFileInput(MESSAGE_FILE);

			if (inputStream != null ) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString);
				}
				inputStream.close();
				text = stringBuilder.toString();
				message.showStatus("Textfile loaded.");
			}
		}
		catch (FileNotFoundException e) {message.showStatus("ERROR: Textfile NOT found.");} 
		catch (IOException e) {message.showStatus("ERROR: Can't load textfile.");} 
		return text; 
	}

	/* 
	 * Write all chat messages to file. 
	 * TODO Improve by only adding last line to file. 
	 */
	public void saveText(String output) {
		try {
			OutputStreamWriter osw = new OutputStreamWriter(c.openFileOutput(MESSAGE_FILE, Context.MODE_PRIVATE));
			osw.write(output);
			osw.close();
			message.showStatus("Local textfile updated.");
			return;
		}
		catch (IOException e) {
			message.showStatus("Error writing to file.");
		}
	}

}
