package com.example.brokenheart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.example.parser.Parser;
import com.example.services.*;


public class Validation extends Activity implements Parser {

	TextView code_text;
	// EditText code;
	EditText code1, code2, code3, code4;
	Button verify;
	MyTask objMyTask;
	String PHONE, COUNTRY_CODE, PHONE_NUMBER, phone_number, code_number;
	String VERIFY_CODE, CODE1, CODE2, CODE3, CODE4, verifyCode;
	Boolean isConnected;
	String web_service_for_verification, user_id;
	int screen_width , screen_height;
	LinearLayout ll;
	int heightDifference;

	SharedPreferences sharedpreferences;

	public static final String MyPREFERENCES = "MyPrefs";

	public ArrayList<String> stringArray_user_id = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screen_width = size.x;
		screen_height = size.y;
		Log.i("screen_width", "==" + screen_width);
		Log.i("screen_height", "==" + screen_height);
		
		
	/*ll = (LinearLayout) findViewById(R.id.ll);
		
		ll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                Rect r = new Rect();
                ll.getWindowVisibleDisplayFrame(r);

                int screenHeight = ll.getRootView().getHeight();
                 heightDifference = screenHeight - (r.bottom - r.top);
                Log.d("Keyboard Size", "Size: " + heightDifference);

                //boolean visible = heightDiff > screenHeight / 3;
            }
        });*/

		setContentView(R.layout.validation);
	
		
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Validation.this);
		phone_number = sharedpreferences.getString("phone_number", "");
		code_number = sharedpreferences.getString("country_code", "");
		verifyCode =sharedpreferences.getString("verification_code", "");

		Log.i("sp phone _number", "" + phone_number);
		Log.i("sp code_number", "" + code_number);
		Log.i("sp verify_number", "" + verifyCode);

		
		code1 = (EditText) findViewById(R.id.code1);
		code2 = (EditText) findViewById(R.id.code2);
		code3 = (EditText) findViewById(R.id.code3);
		code4 = (EditText) findViewById(R.id.code4);
		verify = (Button) findViewById(R.id.verify);
		//code1.setFocusable(true);
		
		/****************************************/
		
		
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(code1, InputMethodManager.SHOW_FORCED);

		
		
		code1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                	code2.requestFocus();
                }

            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }
        });

		code2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                	code3.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }
        });
		code3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                	code4.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }
        });
		/*******************************************/
		
		/*code1.requestFocus();
		
		
				
		code1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(final View v, final boolean hasFocus) {

				if (hasFocus) {
					final InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					// only will trigger it if no physical keyboard is
					// open
					mgr.showSoftInput(code1, InputMethodManager.SHOW_IMPLICIT);
				}

			}
		});

		code1.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {
				Log.i("===","test");
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DEL) {
						code1.requestFocus();
					} else {
						code2.requestFocus();
					}
				}
				return false;
			}
		});
		
		
		
		code2.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DEL) {
						code1.requestFocus();
					} else {
						code3.requestFocus();
					}
				}
				return false;
			}
		});
		code3.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DEL) {
						code2.requestFocus();
					} else {
						code4.requestFocus();
					}

				}
				return false;
			}
		});

		code4.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(final View v, final int keyCode,
					final KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_DEL) {
						code3.requestFocus();
					}

				}

				return false;

			}
		});*/
		
		

		/************************************************************/

		verify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				CODE1 = code1.getText().toString();
				CODE2 = code2.getText().toString();
				CODE3 = code3.getText().toString();
				CODE4 = code4.getText().toString();
				VERIFY_CODE = CODE1 + CODE2 + CODE3 + CODE4;
				Log.d("verification code", "=" + VERIFY_CODE);
				
				if (VERIFY_CODE.equalsIgnoreCase("")) {

					new AlertsUtils(Validation.this,
							AlertsUtils.ENTER_VERIFY_CODE_WARNING);
					Log.i("", "Please enter verification code.");
					return;

				}
				
				else if ((VERIFY_CODE.length() < 4))
					 {

					new AlertsUtils(Validation.this,
							AlertsUtils.INVALID_VERIFY_CODE);
					Log.i("", "Please Enter valid verification code.");
					return;
				}

				else if(VERIFY_CODE.equals(verifyCode)){
				objMyTask = new MyTask();

				/**
				 * Checking if network connection is there or not
				 */
				isConnected = NetConnection
						.checkInternetConnectionn(Validation.this);
				if (isConnected == true) {

					objMyTask.execute();

				}

				else {

					// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
					new AlertsUtils(Validation.this,
							AlertsUtils.NO_INTERNET_CONNECTION);

				}}
				
				else{
					new AlertsUtils(Validation.this,
							AlertsUtils.WRONG_VERIFICATION_CODE);
					Log.i("","Wrong verification code");
				}
			}
		});
	}

	public class MyTask extends AsyncTask<Void, Integer, String> {

		Dialog dialog;
		ProgressBar progressBar;
		TextView tvLoading;
		Button btnCancel;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(Validation.this,
                    "Loading...", "Please Wait", true, false);
			

		
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... Params) {
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.VALIDATION_URL);
			
			String result = null;

			try {
				
				
				
				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

				nameValuePairs.add(new BasicNameValuePair(WebServices.VERIFY_CODE,VERIFY_CODE));
				nameValuePairs.add(new BasicNameValuePair(WebServices.PHONE_NO,phone_number));
			//	nameValuePairs.add(new BasicNameValuePair(WebServices.CC,code_number));
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

				web_service_for_verification = jsonObj
						.getString(WebServices.JSON_verify_result);

							if (web_service_for_verification.equals("true")) {
					
					user_id = jsonObj.getString(WebServices.JSON_user_id);
					Log.i("web service status for verification", "="
							+ web_service_for_verification);
					dialog.dismiss();
					sharedpreferences = PreferenceManager
							.getDefaultSharedPreferences(Validation.this);

					Editor editor = sharedpreferences.edit();

					editor.putString("STEP", "3");
				//	editor.putString("isUserUsingApp","1");
					editor.putString("USER_ID", user_id);
					editor.commit();
					Intent i = new Intent(Validation.this, Home1.class);
					startActivity(i);
				} else {
					dialog.dismiss();
					Toast.makeText(Validation.this,
							"Your Phone Number is Not Valid ", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
				dialog.dismiss();
				Toast.makeText(Validation.this,
						"Something went wrong, Please check your Internet connection and try again.  ", Toast.LENGTH_SHORT)
						.show();
			}

			

		}

	}

	/*
	 * @Override protected void onResume() {
	 * sharedpreferences=getSharedPreferences(MyPREFERENCES,
	 * Context.MODE_PRIVATE); if
	 * (sharedpreferences.contains(WebServices.VERIFIED)) { Intent i = new
	 * Intent(this,Home1.class); startActivity(i);
	 * 
	 * } super.onResume(); }
	 */
	

}
