package com.example.ultraapp;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DatabaseHandeler {

	MessagesOnScreen message;
	AppSettings settings;
	TextFileHandeler textFile;

	public DatabaseHandeler(Context c) {
		message = new MessagesOnScreen(c);
		settings = new AppSettings(c);
		textFile = new TextFileHandeler(c);
	}

	/*
	 * Download all chat messages from MySQL database through JSON php-file.
	 * Setting chat text to loaded datbase text. Updating textfile.
	 */
	public void loadText() {
		Connector.get("getData.php", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray messages) {
				String currentLine = "";
				String currentColor = "";
				String currentUsername = "";
				String chatHistory = "";
				try {
					for (int i = 0; i < messages.length(); i++) {
						currentLine = messages.getJSONObject(i).getString("message");
						currentUsername = messages.getJSONObject(i).getString("author");
						currentColor = messages.getJSONObject(i).getString("color");
						chatHistory = MainActivity.getFormatedLine(currentLine,
								currentUsername, currentColor) + "<br />" + chatHistory;

						// Sets chat window text
						MainActivity.setChatText(chatHistory);

					}
					// Save text to file
					textFile.saveText(chatHistory);

					// message.showStatus("DB-load success!");
				} catch (JSONException e) {
					message.showStatus("ERROR: DB-load 1!");
				}
			}

			@Override
			public void onFailure(Throwable e) {
				message.showStatus("ERROR: DB-load 2!");
			}
		});
	}

	/*
	 * Uploads text to database. Update through async client from Connector class.
	 * Parameter: Message added to MySQL database.
	 */
	public void saveText(String text) {
		RequestParams params = new RequestParams();
		params.put("message", text);
		params.put("username", settings.getUserName());
		params.put("color", settings.getColor());
		Connector.post("insert.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				message.showStatus("Saved to DB.");
			}
		});
	}

}
