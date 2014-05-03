package com.example.ultraapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
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
	Boolean isLightAchieved = false;
	Boolean isProximityAchieved = false;
	Boolean isPhoneInCall = false;
	int updateTime = 1000;

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

		SensorManager mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Sensor lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		Sensor proximitySensor = mySensorManager
				.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		if (lightSensor != null) {

			mySensorManager.registerListener(lightSensorListener, lightSensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}

		if (proximitySensor != null) {
			mySensorManager.registerListener(proximitySensorListener,
					proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
		}

		// Auto-update text
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						// När dessa är true så kommer vi inte auto-uppdatera. När detta
						// bryts så fortsätter uppdateringen
						Log.w("test", isPhoneInCall.toString());
						isCallActive(MainActivity.this);
						if (isLightAchieved == true && isProximityAchieved == true
								&& isPhoneInCall == false) {
						} else {
							Thread.sleep(5000);
							myHandler.post(new Runnable() {
								@Override
								public void run() {
									dataBase.loadText();
								}

							});
						}
					} catch (Exception e) {

					}
				}
			}
		}).start();

	}// OnCreate

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

	// Have the listener for lightsensor
	private final SensorEventListener lightSensorListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
				if (event.values[0] <= 10) {
					isLightAchieved = true;
				} else {
					isLightAchieved = false;
				}
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	// Have the listener for proximity
	final private SensorEventListener proximitySensorListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
				if (event.values[0] == 0) {
					isProximityAchieved = true;
				} else {
					isProximityAchieved = false;
				}
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};

	public void isCallActive(Context context) {
		AudioManager manager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		if (manager.getMode() == AudioManager.MODE_IN_CALL) {
			isPhoneInCall = true;
		} else {
			isPhoneInCall = false;
		}
	}

} // END
