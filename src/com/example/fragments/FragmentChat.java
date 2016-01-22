 package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Timestamp;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.brokenheart.Home1;
import com.example.brokenheart.R;
import com.example.brokenheart.Home1.getChatListTask;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;
import com.example.entity.*;
import com.example.entity.ChatConversation.ChatConversationDetail;
import com.example.utils.DateUtils;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.inputmethodservice.KeyboardView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FragmentChat extends Fragment {
	
	

	Button send, report_user, block_user, chat_rules;
	EditText message;
	ListView chat_listview;
	String TIME;
	String unread;
	View arg1;
	int heightDifference=0;
	getChatListTask getChatListObj;
	private TextView messageTv;
	private TextView messgaeTimeTv;
	Home1 home;

	TextView textView2;
			
		int chatRecordsLength=0;
		
		String blockResult;

	BlockTask BlockObj;
	ReportTask ReportObj;
	String monitorId,phoneNumber;
	Boolean isConnected;
	senderTask senderObj;
	getChatTask getChatObj;
	String chat_message1;
	ArrayList<String> chatIdArray = new ArrayList<String>();
	CountDownTimer newMessageFetcher;

	ArrayList<String> get_messageList = new ArrayList<String>();
//	String get_messageString;

	ChatArrayAdapter chatAdapter;

	ChatConversation chatConversationObj = new ChatConversation();
	ArrayList<ChatConversationDetail> chatConversationList = new ArrayList<ChatConversationDetail>();
	ChatConversation currentChatConversationObj = new ChatConversation();
	ArrayList<ChatConversationDetail> currentChatConversationList = new ArrayList<ChatConversationDetail>();
	Boolean isloadingFirstTime = true;
	Boolean isLoadingInProgress = false;
	LinearLayout ll;

	public FragmentChat() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_chat1
				, null);
		
		
		

		
		
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
				);
		
	
		
		
        
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				Log.i("enterd in","onTouch");
				 InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
				  //  inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
					inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
			                InputMethodManager.RESULT_UNCHANGED_SHOWN);
					
				return false;
			}
		});
		
		
		
		

		/******* getting the bundle *******************/

		Bundle bundle = getArguments();

		monitorId = bundle.getString("chat_id");
		phoneNumber = bundle.getString("phoneNumber");
		
		Log.i("monitorId(chat)==", "==" + monitorId);
		getChat();
		getUnreadMessagefor();
		
		lookUpForNewMessage();
		
		send = (Button) view.findViewById(R.id.send);
		message = (EditText) view.findViewById(R.id.message);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		block_user = (Button) view.findViewById(R.id.block_user);
		chat_rules = (Button) view.findViewById(R.id.chat_rules);
		report_user = (Button) view.findViewById(R.id.report_user);

		chat_listview = (ListView) view.findViewById(R.id.chat_listview);

		textView2.setText(monitorId);
		chatAdapter = new ChatArrayAdapter(getActivity(), R.layout.chat_row);

		chat_listview.setAdapter(chatAdapter);
		
		//message.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		
		
		/*************************** REPORT USER **********************************************/
		/**
		 * report user section
		 */

		report_user.setOnClickListener(new OnClickListener() {

			Dialog dialog;
			Button yes, no;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = new Dialog(getActivity());
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.report_user);

				yes = (Button) dialog.findViewById(R.id.yes);

				no = (Button) dialog.findViewById(R.id.no);

				no.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						dialog.dismiss();

					}
				});

				yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						/**
						 * call a web service to block this person
						 */
						ReportObj = new ReportTask();
						dialog.dismiss();
						/**
						 * Checking if network connection is there or not
						 */
						isConnected = NetConnection
								.checkInternetConnectionn(getActivity());
						if (isConnected == true) {

							ReportObj.execute();

						}

						else {

							// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
							new AlertsUtils(getActivity(),
									AlertsUtils.NO_INTERNET_CONNECTION);

						}

					}
				});
				dialog.show();

			}

		});

		/************************************* CHAT RULES *******************************/

		/**
		 * Chat rules section
		 */

		chat_rules.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentChatList fragment = new FragmentChatList();
			
		       if(fragment != null) {
		           
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		   
		        ft.commit(); */  
			    

			}
		});

		/*********************** BLOCK USER ********************************************/

		/**
		 * block user section
		 */

		block_user.setOnClickListener(new OnClickListener() {

			Dialog dialog;
			Button yes, no;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = new Dialog(getActivity());
				dialog.setCancelable(false);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.fragment_block);

				yes = (Button) dialog.findViewById(R.id.yes);

				no = (Button) dialog.findViewById(R.id.no);

				no.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						dialog.dismiss();

					}
				});

				yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						/**
						 * call a web service to block this person
						 */
						BlockObj = new BlockTask();
						dialog.dismiss();
						/**
						 * Checking if network connection is there or not
						 */
						isConnected = NetConnection
								.checkInternetConnectionn(getActivity());
						if (isConnected == true) {

							BlockObj.execute();

						}

						else {

							// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
							new AlertsUtils(getActivity(),
									AlertsUtils.NO_INTERNET_CONNECTION);

						}

					}
				});
				dialog.show();

			}

		});

		/************************ MESSAGE(chating) ***********************************************/

		/**
		 * message sending section
		 */

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				arg1 = arg0;
				
				hideKeyboard();
				
				

				chat_message1 = message.getText().toString();
				
				Log.i("CHAT MESSAGE1 ***==","**==**"+chat_message1);
				message.setText("");
				// setCreatedMessgaetoChatList();
				
				

				senderSide();
				
			}

			
		});

		return view;
	}
	
	private void updateMyActivity(FragmentActivity activity, String unread) {
		Intent intent = new Intent("android.intent.action.MAIN");
		Log.i("unread message===","=="+unread);
		intent.putExtra("unread_msg", unread);
		
		activity.sendBroadcast(intent);
		
	}


	private void getUnreadMessagefor() {
		
		getChatListObj = new getChatListTask();
		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected == true) {
			getChatListObj.execute();
		}

		else {
			new AlertsUtils(getActivity(), AlertsUtils.NO_INTERNET_CONNECTION);

		}

		
		
	}

	public void addCurrentChatToList() {

		int currentChatListSize = currentChatConversationList.size();

		for (int i = 0; i < currentChatListSize; i++) {

			ChatConversationDetail obj = (ChatConversationDetail) currentChatConversationList
					.get(i);

			if (i == currentChatListSize - 1) {

				String h = obj.getMessage();

			}

			chatAdapter.add(obj);
			scrollChatListToBottom();

		}

	}

	public void setCreatedMessgaetoChatList() {

		ChatConversation mainChatObj = new ChatConversation();
		ChatConversationDetail createdChatobj = mainChatObj.new ChatConversationDetail();
		createdChatobj.setLeft(false);
		createdChatobj.setPhotoUploaded(false);

		// createdChatobj.setMessage("Hi..");
		createdChatobj.setMessage(chat_message1);
		createdChatobj.setChatId("23023");

		createdChatobj.setSenderId("09");
		createdChatobj.setReceiverId("169");

		createdChatobj.setDateCreated("date");
		// controller.insertChatObj(createdChatobj);
		chatAdapter.add(createdChatobj);
		scrollChatListToBottom();
	}

	public void lookUpForNewMessage() {

		newMessageFetcher = new CountDownTimer(5000, 5000) {

			public void onTick(long m) {
				long sec = m / 1000;
			
				if (NetConnection.checkInternetConnectionn(getActivity())) {

					getChat();

					

				}

			}

			public void onFinish() {

				System.out.println("************ Done *************");
				this.start();
			}
		};

		newMessageFetcher.start();

	}

	@Override
	public void onDestroy() {
		System.out.println("************ Cancelling Thread *************");

		if (newMessageFetcher != null) {

			newMessageFetcher.cancel();

		}

		chat_listview.setAdapter(null);

		super.onDestroy();

		// super.onDestroy();
	}

	private void scrollChatListToBottom() {

		chat_listview.post(new Runnable() {

			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				chat_listview.setSelection(chatAdapter.getCount() - 1);
			}
		});
	}

	/*********************** ASYNC TASK TO BLOCK ******************************************/
	/**
	 * 
	 * @author macrew technologies Async task to block the person
	 * 
	 */
	public class BlockTask extends AsyncTask<Void, Integer, String> {
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

				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));

				nameValuePairs.add(new BasicNameValuePair(
						WebServices.BLOCK_USER, monitorId));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("BLOCK.POST", "STATUS OK");

					result = out.toString();
					Log.i("BLOCK.POST result", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in blocking", "......" + e);
			}
			Log.i("result of blocking", "=" + result);
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			try{
			JSONObject jsonObj = new JSONObject(result);
			blockResult=jsonObj
					.getString(WebServices.MONITOR_SECTION_RESULT);
			Log.i("blockResult on blocking", "=" + blockResult);
			
			
			if(blockResult.equals("true"))
			{
				new AlertsUtils(getActivity(), AlertsUtils.USER_IS_BLOCKED_NOW);
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentChatList fragment = new FragmentChatList();
		        
		        Bundle bundle = new Bundle();
		        bundle.putString("monitor_ID", phoneNumber);
		        fragment.setArguments(bundle);
			
		       if(fragment != null) {
		            // Replace current fragment by this new one
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit();   
			    
			        
				
			
			}
				
				else {
					new AlertsUtils(getActivity(), AlertsUtils.ERROR_OCCUR_IN_BLOCKING_THE_USER);
				}
			
			}
			catch(JSONException e){
				e.printStackTrace();
			}
			
			
		}

	}

	/*********************** ASYNC TASK TO REPORT USER ******************************************/
	/**
	 * 
	 * @author macrew technologies Async task to report the person
	 * 
	 */
	public class ReportTask extends AsyncTask<Void, Integer, String> {
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

				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));

				nameValuePairs.add(new BasicNameValuePair(
						WebServices.REPORT_USER, monitorId));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("REPORT", "STATUS OK");

					result = out.toString();
					Log.i("REPORT result", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in reporting", "......" + e);
			}
			Log.i("result of reporting", "=" + result);
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			
			try{
				
				Log.i("report result inside try blk==","=="+result);
				JSONObject jsonObj = new JSONObject(result);
				String reportResult=jsonObj
						.getString(WebServices.MONITOR_SECTION_RESULT);
				Log.i("reportResult =", "=" + reportResult);
				
				
				if(reportResult.equals("true")){
					new AlertsUtils(getActivity(), AlertsUtils.USER_REPORTING_IS_DONE);
					return;
					
					
				}
					
					else {
						new AlertsUtils(getActivity(), AlertsUtils.ERROR_OCCUR_IN_REPORTING_THE_USER);
					}
				
				}
				catch(JSONException e){
					e.printStackTrace();
				}

			

			return;
		}

	}

	public class ChatArrayAdapter extends ArrayAdapter<ChatConversationDetail> {

		//private TextView messageTv;
		//private TextView messgaeTimeTv;
		private List<ChatConversationDetail> chatComments = new ArrayList<ChatConversationDetail>();
		// private LinearLayout chatWrapper;
		private RelativeLayout chatWrapper;

		@Override
		public void add(ChatConversationDetail object) {
			chatComments.add(object);
			super.add(object);
		}

		public ChatArrayAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		public int getCount() {

			return this.chatComments.size();

		}

		public ChatConversationDetail getItem(int index) {
			return this.chatComments.get(index);
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;
			final ChatConversationDetail chatConversationDetailObj = getItem(position);
			// if (row == null) {

			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (chatConversationDetailObj.isLeft()) {

				row = inflater.inflate(R.layout.chat_row, parent, false);

			}

			else {

				row = inflater.inflate(R.layout.chat_row_mine, parent, false);
			}
			
			
			
			chatWrapper = (RelativeLayout) row.findViewById(R.id.chat_wrapper);
			messageTv = (TextView) row.findViewById(R.id.chat_comment);
			messgaeTimeTv = (TextView) row.findViewById(R.id.message_time);
			messageTv.setText(chatConversationDetailObj.getMessage());
			//messageTv.setText(new String(chatConversationDetailObj.getMessage(),"UTF-8"));
			messgaeTimeTv.setText(chatConversationDetailObj.getDateCreated());

			
			RelativeLayout.LayoutParams messageparams = (RelativeLayout.LayoutParams) messageTv
					.getLayoutParams();

			if (chatConversationDetailObj.isPhotoUploaded()) {

			}

			else {

				messageTv.setVisibility(View.VISIBLE);
				

			}

			return row;
		}

		public Bitmap decodeToBitmap(byte[] decodedByte) {

			return BitmapFactory.decodeByteArray(decodedByte, 0,
					decodedByte.length);
		}

	}

	public void senderSide() {
		
		
		senderObj = new senderTask();
		senderObj.execute();
		

	}

	public void getChat() {
		if (isLoadingInProgress)
			return;
		getChatObj = new getChatTask();
		getChatObj.execute();

	}

	/************************************ SENDER SIDE ******************************************/
	public class senderTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			
			 dialog = ProgressDialog.show(getActivity(), "Processing...",
			  "Please Wait", true, false); dialog.show();
			 

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					WebServices.CONTACTS_TO_MONITOR_URL);
			
			

			String result = null;

			try {

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				
				Log.i("url==","=="+"http://touchpixs.com/mobile/webserver.php?"+"store_chat"+
						chat_message1+"suid"+WebServices.USER_ID+"ruid"+monitorId);
				
				Log.i("date GMT","=="+DateUtils.getCurrentTimeStr());

				nameValuePairs.add(new BasicNameValuePair("store_chat",
						chat_message1));

				nameValuePairs.add(new BasicNameValuePair("suid",
						WebServices.USER_ID));
				nameValuePairs.add(new BasicNameValuePair("token_type",
						"ANDRD"));
				
				nameValuePairs.add(new BasicNameValuePair("time",""));
				nameValuePairs.add(new BasicNameValuePair("date",
						DateUtils.getCurrentTimeStr()));
				
				

				nameValuePairs.add(new BasicNameValuePair("ruid", monitorId));
				
				/*UrlEncodedFormEntity form;
				form = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
				form.setContentEncoding(HTTP.UTF_8);
				httppost.setEntity(form);*/


				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				
				
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

				Log.i("error encountered in sender side", "......" + e);
			}
			Log.i("result at sender side", "=" + result);
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
			
			Log.i("get chat result","=="+result);
			getChatResult(result);

		}
	}

	/*********************************** GET CHAT *******************************************/
	public class getChatTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			isLoadingInProgress = true;
			if (isloadingFirstTime) {
				dialog = ProgressDialog.show(getActivity(), "Loading Chat...",
						"Please Wait", true, false);
				dialog.show();
			}

		}

		@Override
		protected String doInBackground(Void... Params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					WebServices.CONTACTS_TO_MONITOR_URL);

			String result = null;

			try {

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				// nameValuePairs
				// .add(new BasicNameValuePair(WebServices.CC, "00"));

				nameValuePairs.add(new BasicNameValuePair("get_chat_for",
			 			WebServices.USER_ID));

				nameValuePairs.add(new BasicNameValuePair("ruid", monitorId));
				
			
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

				Log.i("error encountered in getting message from database",
						"......" + e);
			}
			Log.i("result of getting message from database", "=" + result);
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (isloadingFirstTime) {
				dialog.dismiss();
			}
			isloadingFirstTime = false;
			isLoadingInProgress = false;
			try {
			getChatResult(result);
			
			}catch (Exception e) {
				
			}
		}

		
	}

	public void getChatResult(String response) {

		try {
			Log.i("response inside try blk==","=="+response);
			JSONObject jsonObj = new JSONObject(response);
			
			JSONArray messageString = jsonObj.getJSONArray("chat_data");
			
			JSONArray messageArray = messageString.getJSONArray(0);
			
	for(int i  = 0;i< messageArray.length();i++ ){
			JSONObject c = messageArray.getJSONObject(i);
			
			String msgId = c.getString("tmstmp");
			if (chatIdArray.contains(msgId))
				continue;
			chatIdArray.add(msgId);
			ChatConversation mainChatObj = new ChatConversation();
			
			ChatConversationDetail createdChatobj = mainChatObj.new ChatConversationDetail();
			createdChatobj.setPhotoUploaded(false);
			
			// createdChatobj.setMessage("Hi..");
			createdChatobj.setMessage(c.getString("msg"));
			createdChatobj.setChatId(msgId);

			createdChatobj.setSenderId(WebServices.USER_ID);
			createdChatobj.setReceiverId(c.getString("msg"));
			
			if (c.getString("sender_id").equals(WebServices.USER_ID)) {
				createdChatobj.setLeft(false);
			} else {
				createdChatobj.setLeft(true);
			}
		   
			createdChatobj.setDateCreated(DateUtils.getConvertedLocalTimeStr( c.getString("date")));
			// controller.insertChatObj(createdChatobj);
			chatAdapter.add(createdChatobj);

			// do something here with the value...
		}
		if(chatAdapter.getCount()>chatRecordsLength){
			scrollChatListToBottom();
			
			Log.i("get_messageList.size()=","=="+chatAdapter.getCount());
			Log.i("chatRecordsLength=","=="+chatRecordsLength);
			chatRecordsLength = chatAdapter.getCount();
			Log.i("chatRecordsLength=","=="+chatRecordsLength);
		}
		

	
		}

		catch (JSONException e) {
			e.printStackTrace();

		} /*catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
	
	private void hideKeyboard() {
		// TODO Auto-generated method stub

	    InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
	  //  inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		inputMethodManager.hideSoftInputFromWindow(arg1.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	/******************** get chat list servie *********************************/

	public class getChatListTask extends AsyncTask<Void, Integer, String> {
		
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			/*dialog = ProgressDialog.show(getActivity(),
                    "Loading...", "Please Wait", true, false);
			dialog.show();*/
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
					Log.i("home screen unread msg==", "STATUS OK");

					result = out.toString();
					Log.i("RESULT", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in getting chat list", "......" + e);
			}
			
			return result;

			
		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//dialog.dismiss();
			
			try {
				JSONObject jsonObj1 = new JSONObject(result);
				unread = jsonObj1.getString("unread");
				updateMyActivity(getActivity(), unread);
			}
			
			catch(JSONException e){
				e.printStackTrace();
			}
			
			catch(Exception e){
				
			}
		}
		
	}
/*************************************************************************************/  

}
