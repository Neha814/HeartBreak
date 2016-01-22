package com.example.fragments;

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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.brokenheart.Home1;
import com.example.brokenheart.R;
import com.example.fragments.FragmentPhoneNumber.addContactTask;
import com.example.layout.MainLayout;
import com.example.parser.Parser;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

public class FragmentCupidId extends Fragment implements Parser{

	public FragmentCupidId() {

	}

	Button back, monitor , button1;
	EditText nick_name , id;
	String nickName , cupidId, contact_type;
	addContactTask addContactObj;
	Boolean isConnected;
	String status , contact_id;
	
	Home1 home;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cupid_id_screen, null);
		back = (Button) view.findViewById(R.id.back);
		monitor = (Button) view.findViewById(R.id.monitor);
		nick_name = (EditText) view.findViewById(R.id.nick_name);
		id = (EditText) view.findViewById(R.id.id);
		button1 = (Button) view.findViewById(R.id.button1);		
		contact_type = "cupid";
		
		home = new Home1();
		
		
		
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				//InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				  //imm.hideSoftInputFromWindow(arg0.getApplicationWindowToken(), 0);
				//home.toggleMenu(arg0);
				
				
			}

			
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeyboard();
				onBackPressed();

			}
			private void hideKeyboard() {
				
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				 imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
				
				
				
			}

			private void onBackPressed() {
				FragmentManager fm = getActivity().getSupportFragmentManager();
				fm.popBackStack();

			}
		});
		
		monitor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cupidId = id.getText().toString();
				nickName = nick_name.getText().toString();
				if(cupidId.equalsIgnoreCase("")||nickName.equalsIgnoreCase("")){
					new AlertsUtils(getActivity(),"Please enter the fields to continue.");
					
					}
					else{
						callAddContactWebService();
					}
				
			}
		});

		return view;

	}
	
	protected void callAddContactWebService() {
		addContactObj = new addContactTask();
		
		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected == true) {
			addContactObj.execute();
		}

		else {
						new AlertsUtils(getActivity(),
					AlertsUtils.NO_INTERNET_CONNECTION);

		}
		
	}
	
public class addContactTask extends AsyncTask<Void, Integer, String> {
		
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(getActivity(), "Processing...",
					"Please Wait", true, false);
			dialog.show();

		}
		
		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					WebServices.URL);

			String result = null;
			
				try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));
				nameValuePairs.add(new BasicNameValuePair(WebServices.ADD_CONTACT,
						cupidId));
				nameValuePairs.add(new BasicNameValuePair(WebServices.CONTACT_TYPE,
						contact_type));
				nameValuePairs.add(new BasicNameValuePair(WebServices.NICKNAME,
						nickName));
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("Add contact", "STATUS OK");

					result = out.toString();
					Log.i("add contact", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in ADDContactWebService", "......" + e);
			}
			Log.i("result of AddContactWebService", "=" + result);
			return result;
			
		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			
			dialog.dismiss();
			
			try{
				
				JSONObject jsonObj = new JSONObject(result);
				status = jsonObj.getString("monitor_status");
				if(status.equals("new")){
					
					

					new AlertsUtils(getActivity(),
							AlertsUtils.CONTACT_ADDED_SUCCESSFULLY);
					
					nick_name.setText("");
					id.setText("");
					
				}
				else if(status.equals("already_exists")){
					new AlertsUtils(getActivity(),
							AlertsUtils.CONTACT_EXISTS_FOR_USER);
					
					nick_name.setText("");
					id.setText("");
				}
				
				else{
					new AlertsUtils(getActivity(),"Some error occured.Please try again");
					nick_name.setText("");
					id.setText("");
				}
				
			}
			
			catch(JSONException e) {
				new AlertsUtils(getActivity(),
						AlertsUtils.ERROR_ENCOUNTERED);
				e.printStackTrace();
			}
			
		}
		
	}


}
