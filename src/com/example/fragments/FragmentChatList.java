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
import com.example.fragments.FragmentMonitor.ChatListTask;

import com.example.parser.Parser;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;




public class  FragmentChatList extends Fragment implements Parser{
	
	ListView chatList_listview;
	LazyAdapter mAdapter;
	ChatListTask objChatList;
	Boolean isConnected;
	String monitor_id;
	
	Integer arg = null;
	
	String chatListResult;
	String chat_id;
	
	
	
	
	
	public FragmentChatList() {
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatlist, null);
       
        
        Bundle bundle = getArguments();
        WebServices.chatListDetail = new ArrayList<HashMap<String, String>>();
        monitor_id = bundle.getString("monitor_ID");
        
      objChatList = new ChatListTask();
		 /**
			 * Checking if network connection is there or not
			 */
			isConnected = NetConnection
					.checkInternetConnectionn(getActivity());
			if (isConnected == true) {

				objChatList.execute();
			}
				
			
			else {

				// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
				new AlertsUtils(getActivity(),
						AlertsUtils.NO_INTERNET_CONNECTION);

			}
		 
        
        
        chatList_listview = (ListView) view.findViewById(R.id.chatList_listview);
        Log.i("chatListDetail","=="+  WebServices.chatListDetail);
       
       
        return view;
    }
	
	class LazyAdapter extends BaseAdapter {
		
		ArrayList<HashMap<String, String>> chatListDetail_insideAdapter;

		LayoutInflater mInflater = null;

		

		

		public LazyAdapter(ArrayList<HashMap<String, String>> chatListDetail,
				FragmentActivity activity) {
			
			chatListDetail_insideAdapter=chatListDetail;
			
			mInflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {
			
			
			return chatListDetail_insideAdapter.size();
		}

		@Override
		public Object getItem(int position) {
			
			return chatListDetail_insideAdapter.get(position);
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
						.inflate(R.layout.chatlistitem, null);
				holder.user_number = (TextView) convertView.findViewById(R.id.user_number);
				holder.id = (TextView) convertView.findViewById(R.id.id);
				holder.chat =(Button) convertView.findViewById(R.id.chat);
				
				
				
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.chat.setTag(position);
			
			holder.user_number.setText(chatListDetail_insideAdapter.get(position).get(USER_NUMBER));
			holder.id.setText("Chat with user: "+chatListDetail_insideAdapter.get(position).get(USER_ID));
			
			holder.chat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					
					
					arg = (Integer) arg0.getTag();
					
					Log.i("blocked status=","==<>"+  WebServices.chatListDetail.get(arg).get(BLOCKED));
					if (  WebServices.chatListDetail.get(arg).get(BLOCKED).equals("blocked")){
						new AlertsUtils(getActivity(),
								AlertsUtils.UNBLOCK_WARNING);
					 }
					else {
					chat_id=  WebServices.chatListDetail.get(arg).get(USER_ID);
					Log.i("chat_id on click","=="+chat_id);
					
					Bundle bundle = new Bundle();
					bundle.putString("chat_id",chat_id);
					bundle.putString("phoneNumber",monitor_id);
				
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

	class ViewHolder {
		TextView user_number , id;
		Button chat;

	}
	
/*****************ASYNC CLASS FOR GETTING CHAT LIST********************************/
	
	public class ChatListTask extends AsyncTask<Void, Integer, String> {
		
		
		
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

				Log.i("get user from num=","=="+monitor_id);
				Log.i("user id =","=="+WebServices.USER_ID);
				
				Log.i("",""+WebServices.CONTACTS_TO_MONITOR_URL+"?"+"get_user_for_num="+monitor_id+"&uid="+WebServices.USER_ID);
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.GET_USER_FOR_NUM1,monitor_id));
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("chat list.POST", "STATUS OK");

					result = out.toString();
					Log.i("chat list.POST", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in getting chta list", "......" + e);
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

				Log.i("chatListResult", "=" + chatListResult);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if(chatListResult.equals("true")){
				
				try {
					JSONObject jsonObj = new JSONObject(result);
					
					JSONArray ChatList = jsonObj
							.getJSONArray("users");
					
					for (int i = 0; i < ChatList.length(); i++){
						
						Map<String, String> chatListMap = new HashMap<String, String>();
						
						JSONObject c = ChatList.getJSONObject(i);
						String user_id = c.getString("user_id");
						chatListMap.put(USER_ID,user_id);
						
						String user_number = c.getString("user_number");
						chatListMap.put(USER_NUMBER,user_number);
						
						String username = c.getString("username");
						chatListMap.put(USER_NAME,username);
						
						String status = c.getString("status");
						chatListMap.put(BLOCKED,status);
						
						
						  WebServices.chatListDetail.add((HashMap)chatListMap);
						
						
						
					}  Log.i("chatListDetail(for loop)","=="+  WebServices.chatListDetail);
					
					 mAdapter = new LazyAdapter(  WebServices.chatListDetail, getActivity());

				        chatList_listview.setAdapter(mAdapter);
				    
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				
				
				
				
			}
			
			else{
				new AlertsUtils(getActivity(),
						AlertsUtils.CHAT_LIST_RESULT_IS_EMPTY);
			}

			
			
			
		}
	}
/*******************ENDING OF GETTING CHAT LIST********************************/


}
	

