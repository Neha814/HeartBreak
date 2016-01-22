package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.brokenheart.Home1;
import com.example.brokenheart.MainActivity;
import com.example.brokenheart.R;
import com.example.brokenheart.Validation;
import com.example.fragments.FragmentBlockList.blockListTask;
import com.example.fragments.FragmentChatList.LazyAdapter;
import com.example.parser.Parser;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.FileUtils;
import com.example.utils.NetConnection;
import com.example.utils.StaticUtils;
import com.example.utils.Utils;




import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSettings extends Fragment implements Parser {
	Button password, delete_account, username;
	TextView currentUsername;
	SharedPreferences sharedpreferences;
	
	private static final String ARG_SECTION_NUMBER = "Section";
	
	
	userNameTask userNameObj;
	deleteAccountTask deleteAccountObj;
	passwordTask passwordObj;
	
	String  newPassword_text, confirmPassword_text;
	
	Boolean isConnected;
	
	String New_username;
	String PHONE_NO, USERNAME;
	String STATUS;
	
	
	 /**
     * Default empty constructor.
     */
    public FragmentSettings(){
        super();
    }

    /**
     * Static factory method
     * @param sectionNumber
     * @return	
     */
    public static FragmentSettings newInstance(int sectionNumber) {
    	FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		
		

		View view = inflater.inflate(R.layout.setting, null);
		
		sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		PHONE_NO=sharedpreferences.getString("phoneNo","");
		
		
		Log.i("phone number in setting screen","PHONE_NO=="+PHONE_NO);

		password = (Button) view.findViewById(R.id.password);
		
		delete_account = (Button) view.findViewById(R.id.delete_account);
		username = (Button) view.findViewById(R.id.username);
		currentUsername=(TextView) view.findViewById(R.id.currentUsername);
		
		//currentUsername.setText(PHONE_NO);
		
		currentUsername.setText(WebServices.USER_NAME);
		
		/***************************** USERNAME *******************************************/

		/**
		 * username section
		 */

		username.setOnClickListener(new OnClickListener() {
			Dialog dialog;
			EditText set_username;
			Button save, cancel;
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = new Dialog(getActivity());
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.username1);

				
				save = (Button) dialog.findViewById(R.id.save);
				cancel = (Button) dialog.findViewById(R.id.cancel);
				set_username =(EditText) dialog.findViewById(R.id.set_username);
				
				cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				save.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						New_username = set_username.getText().toString();
						Log.i("New_username","=="+New_username);
						dialog.dismiss();
						
						if(New_username.equals("")||New_username.equals(" ")){
							
							new AlertsUtils(getActivity(),
									AlertsUtils.INVALID_USERNAME_WARNING);
							
							return;
							
						}
						
						sharedpreferences = PreferenceManager
								.getDefaultSharedPreferences(getActivity());

						Editor editor = sharedpreferences.edit();
						
						editor.putString("NEW_USERNAME",New_username );
						editor.commit();
						
						userNameObj = new userNameTask();
						/**
						 * Checking if network connection is there or not
						 */
						isConnected = NetConnection
								.checkInternetConnectionn(getActivity());
						if (isConnected == true) {

							userNameObj.execute();
						}
							
						
						else {

							// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
							new AlertsUtils(getActivity(),
									AlertsUtils.NO_INTERNET_CONNECTION);

						}

					}
				});dialog.show();
			}
			
		});
		
		/******************************** DELETE ACCOUNT ****************************************/

		/**
		 * delete Account section
		 */
		delete_account.setOnClickListener(new OnClickListener() {

			Dialog dialog;
			Button delete_yes, delete_no;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = new Dialog(getActivity());
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.delete_account_dialog);

				delete_yes = (Button) dialog.findViewById(R.id.delete_yes);
				delete_no = (Button) dialog.findViewById(R.id.delete_no);

				delete_no.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.i("NOOOO","NOOOOO");
						dialog.dismiss();
					}
				});

				delete_yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						Log.i("YESSS","YESSSS");
					
						dialog.dismiss();
						
						deleteAccountObj = new deleteAccountTask();
				        
				        /**
						 * Checking if network connection is there or not
						 */
						isConnected = NetConnection
								.checkInternetConnectionn(getActivity());
						if (isConnected == true) {

							deleteAccountObj.execute();
						}
							
						
						else {

							// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
							new AlertsUtils(getActivity(),
									AlertsUtils.NO_INTERNET_CONNECTION);

						}

						
					}
				});dialog.show();
			}
		});
		
		
		/*************************************** PASSWORD ****************************************/
		/**
		 * password section
		 */
		password.setOnClickListener(new OnClickListener() {

			Dialog dialog;
			EditText new_password, confirm_password;
			
			Button save, cancel;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog = new Dialog(getActivity());
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.password_dialog);

				
				new_password = (EditText) dialog
						.findViewById(R.id.new_password);
				confirm_password = (EditText) dialog
						.findViewById(R.id.confirm_password);
				save = (Button) dialog.findViewById(R.id.save);
				cancel = (Button) dialog.findViewById(R.id.cancel);

				save.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
						newPassword_text = new_password.getText().toString();
						confirmPassword_text = confirm_password.getText()
								.toString();
						Log.i("confirmPassword_text","=="+confirmPassword_text);
						
						sharedpreferences = PreferenceManager
								.getDefaultSharedPreferences(getActivity());

						Editor editor = sharedpreferences.edit();
						
						editor.putString("PASSWORD",confirmPassword_text);
						editor.commit();
						
						
						
						
						if(newPassword_text.equals("")||newPassword_text.equals(" ")){
							
							new AlertsUtils(getActivity(),
									AlertsUtils.INVALID_PASSWORD_WARNING);
							
							return;
							
						}
						
						if (newPassword_text.equals(confirmPassword_text)) {
							
							passwordObj = new passwordTask();
							
							/**
							 * Checking if network connection is there or not
							 */
							isConnected = NetConnection
									.checkInternetConnectionn(getActivity());
							if (isConnected == true) {

								passwordObj.execute();
							}
								
							
							else {

								new AlertsUtils(getActivity(),
										AlertsUtils.PASSWORD_DID_NOT_MATCH_WARNING);
								
								return;
							}

						}

						else {
							new AlertsUtils(getActivity(),
									AlertsUtils.TRY_AGAIN);
							
							return;
						}
					}
				});

				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();

			}
		});
		return view;

	}
		
	
/****************** USERNAME WEB SERVICE ********************************************************/
	
public class userNameTask extends AsyncTask<Void, Integer, String> 
		
{
		
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(getActivity(),
                    "Loading...", "Please Wait", true, false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					WebServices.CONTACTS_TO_MONITOR_URL);

			String result = null;

			try {

				

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Log.i("new username==","=="+New_username);
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.UPDATE_SETTINGS,
						WebServices.USER_ID));
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.USERNAME,New_username));
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.PASSWORD,""));
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("username.POST", "STATUS OK");

					result = out.toString();
					Log.i("username.POST", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in username setting", "......" + e);
			}
			Log.i("result of username setting", "=" + result);
			return result;

			
		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			
			try {

				/**
				 * WebServices.MONITOR_SECTION_RESULT = status
				 */
				JSONObject jsonObj = new JSONObject(result);

				STATUS = jsonObj
						.getString(WebServices.MONITOR_SECTION_RESULT);

				Log.i("STATUS(username)", "=" + STATUS);
				
				
				if(STATUS.equals("true")){
					
					
					
					new AlertsUtils(getActivity(),
							AlertsUtils.USERNAME_UPDATED);
					
					
					
					
					sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
					
					WebServices.USER_NAME=sharedpreferences.getString("NEW_USERNAME","");
					
					currentUsername.setText(WebServices.USER_NAME);
					
				}
				
				else{
					new AlertsUtils(getActivity(),
							AlertsUtils.ERROR_ENCOUNTERED_IN_UPDATING_USERNAME);
				}

				
				

			} catch (JSONException e) {
				
				new AlertsUtils(getActivity(),
						AlertsUtils.ERROR_ENCOUNTERED_IN_UPDATING_USERNAME);
				
				Log.i("error in updating username==","=="+e);
				e.printStackTrace();
			}
			
			
			
		}
	}
/*******************ENDING OF USERNAME WEB SRVICE****************************************************/



/****************** PASSWORD WEB SERVICE ********************************************************/

public class passwordTask extends AsyncTask<Void, Integer, String> {
		
		
		
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = ProgressDialog.show(getActivity(),
                    "Loading...", "Please Wait", true, false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					WebServices.CONTACTS_TO_MONITOR_URL);

			String result = null;

			try {

				

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Log.i("new password==","=="+confirmPassword_text );
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.UPDATE_SETTINGS,
						WebServices.USER_ID));
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.USERNAME,""));
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.PASSWORD,confirmPassword_text));
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("password.POST", "STATUS OK");

					result = out.toString();
					Log.i("password.POST", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in password setting", "......" + e);
			}
			Log.i("result of password setting", "=" + result);
			return result;

			
		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			
			try {

				/**
				 * WebServices.MONITOR_SECTION_RESULT = status
				 */
				JSONObject jsonObj = new JSONObject(result);

				STATUS = jsonObj
						.getString(WebServices.MONITOR_SECTION_RESULT);

				Log.i("STATUS(password)", "=" + STATUS);
				
				
				if(STATUS.equals("true")){
					
					
					
					new AlertsUtils(getActivity(),
							AlertsUtils.PASSWORD_UPDATED);
					
					
					
					
					sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
					
					WebServices.PASS_WORD=sharedpreferences.getString("PASSWORD","");
					
					Log.i("passwrod from sp==","=="+WebServices.PASS_WORD);
					
					
					
				}
				
				else{
					new AlertsUtils(getActivity(),
							AlertsUtils.ERROR_ENCOUNTERED_IN_UPDATING_PASSWORD);
				}

				
				

			} catch (JSONException e) {
				
				new AlertsUtils(getActivity(),
						AlertsUtils.ERROR_ENCOUNTERED_IN_UPDATING_PASSWORD);
				
				Log.i("error in updating pasword==","=="+e);
				e.printStackTrace();
			}
			
			
			
		}
	}
/*******************ENDING OF PASSWORD WEB SRVICE****************************************************/

/******************* DELETE ACCOUNT TASK *************************************************************/

public class deleteAccountTask extends AsyncTask<Void, Integer, String> {
	
	
	
	Dialog dialog;
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		dialog = ProgressDialog.show(getActivity(),
                "Loading...", "Please Wait", true, false);
		dialog.show();
	}
	
	@Override
	protected String doInBackground(Void... Params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				WebServices.CONTACTS_TO_MONITOR_URL);

		String result = null;

		try {

			

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);

			
			
			nameValuePairs.add(new BasicNameValuePair("delete_account",
					WebServices.USER_ID));
		
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("delete account", "STATUS OK");

				result = out.toString();
				Log.i("delete account", "STATUS OK===" + result);
			} else {
				// close connection
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered in deleting account", "......" + e);
		}
		Log.i("result of deleting account", "=" + result);
		return result;

		
	}
	
	protected void onProgressUpdate(Integer... values) {

	}

	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		dialog.dismiss();
		
		try {

			/**
			 * WebServices.MONITOR_SECTION_RESULT = status
			 */
			
			JSONObject jsonObj = new JSONObject(result);
			String deleteAccoutnResult;
			deleteAccoutnResult = jsonObj
					.getString(WebServices.MONITOR_SECTION_RESULT);

			Log.i("deleteAccoutnResult", "=" + deleteAccoutnResult);
			
			
				
				
					if(deleteAccoutnResult.equals("true")){
						new AlertsUtils(getActivity(),
								AlertsUtils.ACCOUNT_DELETED);
						sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
						sharedpreferences.edit().clear().commit();
						if (AddedContactUserId!=null)
						AddedContactUserId.clear();
						
						if (AddedContactList!=null)
						AddedContactList.clear();
						
						if (MonitorContactList!=null)
						MonitorContactList.clear();
						
						if (MonitorMatchTotal!=null )
						MonitorMatchTotal.clear();
						
						if (MonitorContactId!=null )
						MonitorContactId.clear();
						
						if (AddedContactList!=null)
						AddedContactList .clear();
						
						if (AddedContactName!=null)
						AddedContactName.clear();
						
						if (WebServices.BlockedList!=null )
						WebServices.BlockedList.clear();
						
						if (added_monitor_contacts!=null )
						added_monitor_contacts.clear();
						
						if (contact_detail!=null)
						contact_detail.clear();
						
						if (WebServices.chatListDetail!=null)
						WebServices.chatListDetail.clear();
						
						if (WebServices.ChatInboxDetail!=null)
						WebServices.ChatInboxDetail.clear();
						
						if (AddedContactList1!=null)
						AddedContactList1.clear();
						
						if (WebServices.contact_to_monitor_details!=null)
						WebServices.contact_to_monitor_details .clear();
						
						Intent i = new Intent(getActivity(),MainActivity.class);
						startActivity(i);
						
				} 
					
					else{
						new AlertsUtils(getActivity(),
								AlertsUtils.ERROR_OCCURED);
					}
					
				
			

		} 
		catch (JSONException e) {
			
			new AlertsUtils(getActivity(),
					AlertsUtils.ERROR_OCCURED);
			e.printStackTrace();
		}
		
				}
}


/*******************************************************************************************/



/*************************************************************************************/

}
	
	


