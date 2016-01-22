package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import com.example.brokenheart.R;
import com.example.fragments.FragmentChatList.LazyAdapter;
import com.example.fragments.FragmentChatList.ViewHolder;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentBlockList extends Fragment {
	
	ListView blockedList;
	blockListTask blockListObj;
	unBlockTask unBlockObj;
	Boolean isConnected;
	LazyAdapter mAdapter;
	String blockListResult;
	Integer arg = null;
	
	
	public FragmentBlockList(){
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blocked_listview, null);
        
        blockedList=(ListView) view.findViewById(R.id.blockedList);
        
        WebServices.BlockedList  = new ArrayList<String>();
        
        blockListObj = new blockListTask();
        
        /**
		 * Checking if network connection is there or not
		 */
		isConnected = NetConnection
				.checkInternetConnectionn(getActivity());
		if (isConnected == true) {

			 blockListObj.execute();
		}
			
		
		else {

			// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
			new AlertsUtils(getActivity(),
					AlertsUtils.NO_INTERNET_CONNECTION);

		}
	 
   
        return view;
    }
	/****************************BLOCK LIST ASYNC CLASS*************************************/
public class blockListTask extends AsyncTask<Void, Integer, String> {
		
		
		
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

				
				
				nameValuePairs.add(new BasicNameValuePair("get_blocked_users_for",
						WebServices.USER_ID));
				
				
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("block list.POST", "STATUS OK");

					result = out.toString();
					Log.i("block list.POST", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in getting block list", "......" + e);
			}
			Log.i("result of getting block list", "=" + result);
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

				blockListResult = jsonObj
						.getString(WebServices.MONITOR_SECTION_RESULT);

				Log.i("blockListResult", "=" + blockListResult);
				
				if(blockListResult.equals("true")){
					
					try {
						JSONObject jsonObj1 = new JSONObject(result);
						
						JSONArray BlockListArray = jsonObj1
								.getJSONArray("blocked_users");
						
						for (int i = 0; i < BlockListArray.length(); i++){
							
							WebServices.BlockedList.add(BlockListArray.get(i).toString());
							
							
							
						}  Log.i("BlockedList=","=="+WebServices.BlockedList);
						
						 mAdapter = new LazyAdapter(WebServices.BlockedList, getActivity());

						 blockedList.setAdapter(mAdapter);
					    
						
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				
				}
				
				else{
					mAdapter = new LazyAdapter(WebServices.BlockedList, getActivity());

					 blockedList.setAdapter(mAdapter);
				    
					new AlertsUtils(getActivity(),
							AlertsUtils.NO_BLOCKED_USER);
				}

			} 
			catch (JSONException e) {
				
				new AlertsUtils(getActivity(),
						AlertsUtils.ERROR_BLOCKED_USER);
				e.printStackTrace();
			}
			
					}
	}
/*******************ENDING OF BLOCK LIST ASYNC CLASS********************************/

/***********************ADAPTER CLASS******************************************/

class LazyAdapter extends BaseAdapter {
	
	ArrayList<String> blocklist;

	LayoutInflater mInflater = null;

	
	 
	

	public LazyAdapter(ArrayList<String> BlockedList,
			FragmentActivity activity) {
		
		
		blocklist=BlockedList;
		
		
		mInflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		
		
		return blocklist.size();
	}

	@Override
	public Object getItem(int position) {
		
		return blocklist.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.blockedlist_listitem, null);
			holder.block_user = (TextView) convertView.findViewById(R.id.block_user );
			
			holder.unblock = (Button) convertView.findViewById(R.id.unblock );
			
			
			
			convertView.setTag(holder);

		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.unblock.setTag(position);
		
		holder.block_user.setText(blocklist.get(position));
		
		holder.unblock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				arg = (Integer) arg0.getTag();
				
				 unBlockObj = new unBlockTask();
			        
			        /**
					 * Checking if network connection is there or not
					 */
					isConnected = NetConnection
							.checkInternetConnectionn(getActivity());
					if (isConnected == true) {

						unBlockObj.execute();
					}
						
					
					else {

						// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
						new AlertsUtils(getActivity(),
								AlertsUtils.NO_INTERNET_CONNECTION);

					}

				
				
			}
		});
		
		
					
					return convertView;
	}

}
/******************ENDING OF ADAPTER CLASS ************************************/
class ViewHolder {
	TextView block_user;
	Button unblock;
	

}

/********************** UNBLOCK ASYNC TASK ************************************************/

public class unBlockTask extends AsyncTask<Void, Integer, String> {
	
	
	
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

			
			
			nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
					WebServices.USER_ID));
			
			nameValuePairs.add(new BasicNameValuePair(WebServices.UNBLOCK_USER,WebServices.BlockedList.get(arg)));
			
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				Log.i("unblock", "STATUS OK");

				result = out.toString();
				Log.i("unblock", "STATUS OK===" + result);
			} else {
				// close connection
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {

			Log.i("error encountered in unblocking", "......" + e);
		}
		Log.i("result of unblocking the user", "=" + result);
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

			blockListResult = jsonObj
					.getString(WebServices.MONITOR_SECTION_RESULT);

			Log.i("blockListResult", "=" + blockListResult);
			
			
				
				
					if(blockListResult.equals("true")){
						
						if (WebServices.BlockedList!=null || WebServices.BlockedList.size() >= 0)
							WebServices.BlockedList.clear();
						
						
						 blockListObj = new blockListTask();
					        
					        /**
							 * Checking if network connection is there or not
							 */
							isConnected = NetConnection
									.checkInternetConnectionn(getActivity());
							if (isConnected == true) {

								 blockListObj.execute();
							}
								
							
							else {

								// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
								new AlertsUtils(getActivity(),
										AlertsUtils.NO_INTERNET_CONNECTION);

							}
						
						
					
					
				//	 mAdapter = new LazyAdapter(BlockedList, getActivity());

					// blockedList.setAdapter(mAdapter);
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



}
