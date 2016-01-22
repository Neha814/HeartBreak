package com.example.brokenheart;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.parser.Parser;
import com.example.services.WebServices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainSplashScreen extends Activity implements Parser {

	SharedPreferences sharedpreferences;

	public static final String MyPREFERENCES = "MyPrefs";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_splash_screen);

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(MainSplashScreen.this);
		// METHOD 1

		/****** Create Thread that will sleep for 5 seconds *************/
		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 5 seconds
					sleep(5 * 1000);

					// After 5 seconds redirect to another intent

					String userStep = sharedpreferences.getString("STEP", "");
					String userUsingApp = sharedpreferences.getString(
							"isUserUsingApp", "");
					
					
					

					/******************************* SIGN UP =2 ***************************************/
					/**
					 * for signup(main activity)
					 */
					if (userStep.equals("2")) {
						Intent i = new Intent(MainSplashScreen.this,
								Validation.class);
						startActivity(i);
						finish();

					}
					/******************************** VALIDATION =3 **************************************/

					/**
					 * for validation
					 */
					else if (userStep.equals("3")) {
						
						
						
							Intent i = new Intent(MainSplashScreen.this,
									Home1.class);

							startActivity(i);
							finish();
						
					}
					
					/*else if (userStep.equals("4")) {
						
						
						
						Intent i = new Intent(MainSplashScreen.this,
								Home1.class);

						startActivity(i);
						finish();
					
				}*/
				
					
					else{
						Intent i = new Intent(MainSplashScreen.this,
								MainActivity.class);

						startActivity(i);
						finish();
					}
					
					
				} catch (Exception e) {

					Log.i("Splash Screen Exception", "" + e);

				}
			}
		};

		// start thread
		background.start();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}
}