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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.brokenheart.R;

import com.example.fragments.FragmentBlockList.LazyAdapter;
import com.example.fragments.FragmentBlockList.ViewHolder;
import com.example.fragments.FragmentBlockList.blockListTask;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

public class FragmentChatInbox extends Fragment {
	
	ListView chatinbox_Listview;
	blockListTask blockListObj;
	Boolean isConnected;
	LazyAdapter mAdapter;
	String chatListResult;
	Integer arg = null;
	String receiver_id;
	
//	public static ArrayList<HashMap<String, String>> ChatInboxDetail = new ArrayList<HashMap<String, String>>();
	
	public FragmentChatInbox() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		
		
        View view = inflater.inflate(R.layout.chatinboxlistview, null);
        
        chatinbox_Listview=(ListView) view.findViewById(R.id.chatinbox_Listview);
        WebServices.ChatInboxDetail = new ArrayList<HashMap<String, String>>();
       
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

				
				
				nameValuePairs.add(new BasicNameValuePair("get_chat_list_for",
						WebServices.USER_ID));
				
				
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("to get chat list status", "STATUS OK");

					result = out.toString();
					Log.i("to get chat list", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in getting chat list", "......" + e);
			}
			Log.i("result of getting chat list", "=" + result);
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

				chatListResult = jsonObj
						.getString(WebServices.MONITOR_SECTION_RESULT);

				Log.i("blockListResult", "=" + chatListResult);
				
				if(chatListResult.equals("true")){
					
					try {
						JSONObject jsonObj1 = new JSONObject(result);
						
						JSONArray chatListArray = jsonObj1
								.getJSONArray("user_chat");
						
						for (int i = 0; i < chatListArray.length(); i++){
							
				Map<String, String> ChatListDetail = new HashMap<String, String>();
				
				JSONObject c = chatListArray.getJSONObject(i);
				String chat_id = c.getString("chat_id");
				String reciever_id = c.getString("reciever_id");
				String username = c.getString("username");
				String status = c.getString("status");
							
				ChatListDetail.put("chat_id",chat_id);	
				ChatListDetail.put("reciever_id",reciever_id);
				ChatListDetail.put("username",username);
				ChatListDetail.put("status",status);
				
				 WebServices.ChatInboxDetail
				.add((HashMap) ChatListDetail);

							
							
						}  Log.i("ChatInboxDetail=","=="+ WebServices.ChatInboxDetail);
						
						 mAdapter = new LazyAdapter( WebServices.ChatInboxDetail, getActivity());

						 chatinbox_Listview.setAdapter(mAdapter);
					    
						
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
				
				}
				
				else{
					new AlertsUtils(getActivity(),
							AlertsUtils.INBOX_IS_EMPTY);
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
	
	

	LayoutInflater mInflater = null;

	
	public LazyAdapter(ArrayList<HashMap<String, String>> ChatInboxDetail,
			FragmentActivity activity) {
		
		
		
		
		
		mInflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		
		
		return  WebServices.ChatInboxDetail.size();
	}

	@Override
	public Object getItem(int position) {
		
		return  WebServices.ChatInboxDetail.get(position);
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
					.inflate(R.layout.chatinbox, null);
		//	holder.chat_username = (TextView) convertView.findViewById(R.id.chat_username );
			holder.chat=(Button) convertView.findViewById(R.id.chat);
			holder.receiver_id = (TextView) convertView.findViewById(R.id.id);
			
			
			
			convertView.setTag(holder);

		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.chat.setTag(position);
		
		holder.receiver_id.setText( WebServices.ChatInboxDetail.get(position).get("reciever_id"));
	//	holder.chat_username.setText(ChatInboxDetail.get(position).get("username"));
		
		holder.chat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				
				arg = (Integer) arg0.getTag();
				if (  WebServices.ChatInboxDetail.get(arg).get("status").equals("blocked")){
					new AlertsUtils(getActivity(),
							AlertsUtils.UNBLOCK_WARNING);
				 }
				else {
				receiver_id= WebServices.ChatInboxDetail.get(arg).get("reciever_id");
				Log.i("chat_id on click","=="+receiver_id);
				
				Bundle bundle = new Bundle();
				bundle.putString("chat_id",receiver_id);
				
			
			  FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentChat fragment = new FragmentChat();
		        fragment.setArguments(bundle);

		       
		       if(fragment != null) {
		            // Replace current fragment by this new one
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    //    tvTitle.setText("Contacts");
		        ft.commit();   
			    
				}
			}
		});
		
	
		
		
					
					return convertView;
	}

}
/******************ENDING OF ADAPTER CLASS ************************************/
class ViewHolder {
	TextView chat_username , receiver_id;
	Button chat;
	

}


}
	

