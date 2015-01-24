/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.androidhive;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.androidhive.library.DatabaseHandler;
import com.example.androidhive.library.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestPush extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	TextView registerErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_ID = "id";
	private static String KEY_DISTANCE = "distance";
	private static String KEY_TIME = "time";
	private static String KEY_STEPS = "steps";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.updateDist);
		inputEmail = (EditText) findViewById(R.id.updateTime);
		inputPassword = (EditText) findViewById(R.id.updateSteps);
		btnRegister = (Button) findViewById(R.id.btnUpdate);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				String distance = inputFullName.getText().toString();
				String time = inputEmail.getText().toString();
				String steps = inputPassword.getText().toString();
				UserFunctions userFunction = new UserFunctions();
				//String id = Integer.toString(1);
				//Need to figure way out to get current users id
				String id = userFunction.getUID(getApplicationContext());
				JSONObject json = userFunction.postNew(id, distance, time, steps);

				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						//loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){

							// user successfully registred
							// Store user details in SQLite Database
							DatabaseHandler db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");

							// Clear all previous data in database
							//userFunction.logoutUser(getApplicationContext());

							db.addData(json_user.getString(KEY_ID), json_user.getString(KEY_DISTANCE), 
									json.getString(KEY_TIME), json.getString(KEY_STEPS), 
									json_user.getString(KEY_CREATED_AT));						
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}
}
