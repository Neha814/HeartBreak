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

import com.example.brokenheart.R;
import com.example.fragments.FragmentContactsMenu.LazyAdapter;
import com.example.fragments.FragmentMonitor.MyTask;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentBlockedUsers extends Fragment {
	
	ListView Blocke_User_listview;
	LazyAdapter mAdapter;
	MyTask objMyTask;
	Boolean isConnected;
	String Blocked_user_result;
	
	public FragmentBlockedUsers(){
		
	}
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blocked_user, null);
        Blocke_User_listview = (ListView) view.findViewById(android.R.id.list);
        objMyTask = new MyTask();
        /**
		 * Checking if network connection is there or not
		 */
		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected == true) {

			objMyTask.execute();

		}

		else {

			// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
			new AlertsUtils(getActivity(),
					AlertsUtils.NO_INTERNET_CONNECTION);

		}
		
		//mAdapter = new LazyAdapter(AddedContactList1, getActivity());

		//Blocke_User_listview.setAdapter(mAdapter);
    

        
        return view;
	}
	
	public class MyTask extends AsyncTask<Void, Integer, String> {
		
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
					WebServices.CONTACTS_TO_MONITOR_URL);

			String result = null;

			try {

				

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair("GET_BLOCKED_USERS_OF",
						WebServices.USER_ID));
				
				
				
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

				Log.i("error encountered in getting value of monitor section", "......" + e);
			}
			Log.i("result of monitor section", "=" + result);
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

				Blocked_user_result = jsonObj
						.getString("Blocked_user_result");

				Log.i("Blocked_user_result", "=" + Blocked_user_result);
				
				if(Blocked_user_result =="true"){
					
					//mAdapter = new LazyAdapter(AddedContactList1, getActivity());

					//Blocke_User_listview.setAdapter(mAdapter);
					
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		
		}

		
	}


}
