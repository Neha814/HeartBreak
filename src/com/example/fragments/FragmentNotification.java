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

import com.example.brokenheart.R;
import com.example.parser.Parser;
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
import android.webkit.WebView.FindListener;
import android.widget.ListView;

public class FragmentNotification  extends Fragment implements Parser{
	
	ListView listview;
	NotificationTask NotificationObj;
	Boolean isConnected;
	public FragmentNotification(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_notification_listview, null);
		
		listview = (ListView) view.findViewById(android.R.id.list);
		
		getNotificationResult();
		
		return view;
		
	}

	private void getNotificationResult() {
		
		NotificationObj = new NotificationTask();
		 /**
		 * Checking if network connection is there or not
		 */
		isConnected = NetConnection
				.checkInternetConnectionn(getActivity());
			if (isConnected == true) {

			NotificationObj.execute();
				}
			
		
		else {

			new AlertsUtils(getActivity(),
					AlertsUtils.NO_INTERNET_CONNECTION);

			}
	 
		}
	
	public class NotificationTask extends AsyncTask<Void, Integer, String> {
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

				
				
				nameValuePairs.add(new BasicNameValuePair("get_notification_result",
						WebServices.USER_ID));
				
				
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("notification result", "STATUS OK");

					result = out.toString();
					Log.i("notification result", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in notification result", "......" + e);
			}
			Log.i("result of getting notification result", "=" + result);
			return result;

			
		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}

}
