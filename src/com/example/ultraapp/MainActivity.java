package com.example.ultraapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText mtextInput;
	public static TextView mtextOutput;
	View.OnClickListener l;
	TextFileHandeler textFile;
	DatabaseHandeler dataBase;
	MessagesOnScreen message;
	AppSettings settings;
	Context c;
	Handler myHandler = new Handler();

	protected static final String USERNAME = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Settings icon onclick
		if (item.getItemId() == R.id.settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		} else if (item.getItemId() == R.id.update) {
			dataBase.loadText();
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupHelpClasses();
		setupListners();
		setupWidgets();

		// Print local chat history while database text is loaded.
		setChatText(textFile.loadText());

		// Load latest database text. Sets chat text and saves text file.
		dataBase.loadText();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(1000);
						myHandler.post(new Runnable() {

							@Override
							public void run() {
								dataBase.loadText();
							}

						});
					} catch (Exception e) {

					}
				}

			}
		}).start();

	}

	private void setupHelpClasses() {
		c = getApplicationContext();
		message = new MessagesOnScreen(c);
		textFile = new TextFileHandeler(c);
		dataBase = new DatabaseHandeler(c);
		settings = new AppSettings(c);
	}

	private void setupListners() {
		l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// Save button
				if (v.getId() == R.id.save) {
					String input = mtextInput.getText().toString();
					if (!input.equals("")) {
						addToOutput(input);
						clearInput();
						textFile.saveText(getChatText());
						dataBase.saveText(input);
					}
				}
			}
		};
	}

	private void setupWidgets() {
		mtextInput = (EditText) findViewById(R.id.inputTextLine);
		mtextOutput = (TextView) findViewById(R.id.mainText1);

		Button saveButton = (Button) findViewById(R.id.save);

		// Add listeners
		saveButton.setOnClickListener(l);
	}

	/*
	 * Adds the text from the input line on top of the text from output window.
	 * DoesOT save input to file or database.
	 */
	private void addToOutput(String input) {
		String output = getChatText();
		input = getFormatedLine(input, settings.getUserName(), settings.getColor());
		String finalChat = input + output;
		finalChat = finalChat.replaceAll("<p dir=\"ltr\">", "<br />");
		setChatText(finalChat);
		message.showStatus("Added to output.");
	}

	public static String getFormatedLine(String input, String username,
			String color) {
		String formattedString = "<font color=\"" + color + "\">" + username
				+ " >> " + input + "</font>";
		return formattedString;
	}

	private void clearInput() {
		mtextInput.setText("");
	}

	public static void setChatText(String text) {
		mtextOutput.setText(Html.fromHtml(text));
	}

	public String getChatText() {
		return Html.toHtml((Spanned) mtextOutput.getText());
	}

}
