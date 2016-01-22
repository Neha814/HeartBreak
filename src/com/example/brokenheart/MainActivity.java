package com.example.brokenheart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.brokenheart.Validation.MyTask;
import com.example.parser.Parser;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements Parser {

	TextView createAccount, phoneNumberText, trueText, lies;
	EditText phoneNumber, countryCode;
	ImageView image_brokenHeart;
	Button submit;
	Boolean isConnected;
	String web_service_status_for_signup,verify_code_for_signup;
	 SharedPreferences sharedpreferences;
	 

	String  country_codeNo, PHONE_NUMBER , COUNTRY_CODE;
	//String phoneNo;
	int random;

	public static final String MyPREFERENCES = "MyPrefs";

	MyTask objTask;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		setContentView(R.layout.activity_main1);
		createAccount = (TextView) findViewById(R.id.createAccount);
		
		trueText = (TextView) findViewById(R.id.trueText);
		lies = (TextView) findViewById(R.id.lies);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
	//	countryCode = (EditText) findViewById(R.id.countryCode);

		image_brokenHeart = (ImageView) findViewById(R.id.image_brokenHeart);
		submit = (Button) findViewById(R.id.submit);
		
		
	

		

		

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("", "clicked..");

			//	country_codeNo = countryCode.getText().toString();
				WebServices.phoneNo = phoneNumber.getText().toString();
				
				
				

				PHONE_NUMBER =  WebServices.phoneNo;
			//	COUNTRY_CODE = country_codeNo;
				Log.i("main activity phn no",""+PHONE_NUMBER);
			//	Log.i("main activity code no",""+COUNTRY_CODE);
				
				
			

			

				 if (WebServices.phoneNo.equalsIgnoreCase("")) {

					new AlertsUtils(MainActivity.this,
							AlertsUtils.ENTER_PHONE_NUMBER_WARNING);
					Log.i("", "phone number field is empty");
					return;

				}

				else if ((WebServices.phoneNo.length() < 10) || (WebServices.phoneNo.length() > 10)) {

					new AlertsUtils(MainActivity.this,
							AlertsUtils.INVALID_PHONE_NUMBER_WARNING);
					Log.i("", "enter valid phone number");
					return;
				}
				
			
				else {

					
					objTask = new MyTask();

					/**
					 * Checking if network connection is there or not
					 */
					isConnected = NetConnection
							.checkInternetConnectionn(MainActivity.this);
					if (isConnected == true) {

						objTask.execute();
					}
						
					
					else {

						// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
						new AlertsUtils(MainActivity.this,
								AlertsUtils.NO_INTERNET_CONNECTION);

					}
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyTask extends AsyncTask<Void, Integer, String> {
		
		Dialog dialog;
		ProgressBar progressBar;
		TextView tvLoading;
		Button btnCancel;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(MainActivity.this,
                    "Loading...", "Please Wait", true, false);
			
	
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.SIGNUP_URL);

			String result = null;

			try {

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair(
						WebServices.SIGNUP_NO, PHONE_NUMBER));
			
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("REGISTER.POST", "STATUS OK");

					result = out.toString();
					Log.i("REGISTER.POST", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered", "......");
			}
			Log.i("result", "=" + result);
			
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {

				JSONObject jsonObj = new JSONObject(result);

				web_service_status_for_signup = jsonObj
						.getString(WebServices.signup_result);
				verify_code_for_signup = jsonObj
						.getString(WebServices.JSON_signup_result);
				Log.i("web service status", "=" + web_service_status_for_signup);
				if (web_service_status_for_signup.equals("true")) {
					
					
					sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					Editor editor = sharedpreferences.edit();
					editor.putString("STEP", "2");
					editor.putString("phone_number", PHONE_NUMBER);
					editor.putString("country_code", COUNTRY_CODE);
					editor.putString("phoneNo",WebServices.phoneNo);
					editor.putString("verification_code",verify_code_for_signup);
					editor.commit();
					
					Toast.makeText(MainActivity.this,verify_code_for_signup
							, Toast.LENGTH_LONG)
							.show();
					
						Intent i = new Intent(MainActivity.this, Validation.class);
					/*	i.putExtra("PHONE_NUMBER", PHONE_NUMBER);
						 i.putExtra("COUNTRY_CODE1", COUNTRY_CODE);*/
						 dialog.dismiss();
						startActivity(i);
					 
				} else {

					Toast.makeText(MainActivity.this,
							"You Phone Number is Not Valid ", Toast.LENGTH_SHORT)
							.show();
					dialog.dismiss();
				}

			} catch (JSONException e) {
				Toast.makeText(MainActivity.this,
						"Some error occur while processing this request, Please try again. ", Toast.LENGTH_SHORT)
						.show();
				dialog.dismiss();
				e.printStackTrace();
			}

		//	if (web_service_status_for_signup != ""&&web_service_status_for_signup!= null) {
				
				

		}

	}
	
	


}
