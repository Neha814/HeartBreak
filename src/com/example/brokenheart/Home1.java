package com.example.brokenheart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.example.layout.MainLayout;
import com.example.brokenheart.*;

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
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.fragments.*;

import com.example.parser.*;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;



public class Home1 extends FragmentActivity implements Parser {

	// The MainLayout which will hold both the sliding menu and our main content
	// Main content will holds our Fragment respectively
	MainLayout mainLayout;
	
	String unread_msg;
	CountDownTimer newMessageFetcher;

	// ListView menu
	private ListView lvMenu, listview;
	// private String[] lvMenuItems;
	MyListAdapter mAdapter;

	String PROJECT_NUMBER = "519173785854";
	String regid;
	GoogleCloudMessaging gcm;

	sendRegIdTask sendRegIdObj;
	getChatListTask getChatListObj;
	Boolean isConnected;

	String device;

	public String[] lvMenuItems2;
	String user_id;
	public Integer[] images1;

	SharedPreferences sharedpreferences;

	public LinearLayout red1;

	public LinearLayout red2;

	public LinearLayout red3;

	public LinearLayout red4;

	// Menu button
	Button btMenu, contacts, monitor, settings , chat;

	ImageView red_heart, broken_heart;

	static final String DISPLAY_MESSAGE_ACTION = "com.example.heartLock";

	static final String EXTRA_MESSAGE = "message";

	public static boolean notificationClicked = false;

	// Title according to fragment
	TextView tvTitle;
	public static ContentResolver appContext;
	ArrayList<String> contactlist_to_monitor = new ArrayList<String>();

	String[] lvMenuItems1 = new String[20];


	Integer[] images = { R.drawable.icon1, R.drawable.icon2, R.drawable.icon3,
			R.drawable.icon4, R.drawable.icon5, R.drawable.icon6,
			R.drawable.icon7, R.drawable.icon8 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		 
		device = "ANDRD";
		
		
		IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.MAIN");
        registerReceiver(mHandleMessageReceiver, intentFilter);
        Log.i("====","register again");
		
		
		Log.i("bundle extras==",
				"==" + getIntent().getStringExtra("notification"));

		String extras = getIntent().getStringExtra("notification");

		

		getRegId();		
		getUnreadMessagefor();

		sendRegId();

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Home1.this);

		Editor editor = sharedpreferences.edit();

		editor.putString("isUserUsingApp", "1");

		editor.commit();

		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(Home1.this);
		user_id = sharedpreferences.getString("USER_ID", "");
		WebServices.USER_ID = sharedpreferences.getString("USER_ID", "");

		Log.i("user id from shared preferences", "" + WebServices.USER_ID);

		lvMenuItems1[0] = "Your Profile id:" + user_id;
		lvMenuItems1[1] = "Notifications";
		lvMenuItems1[2] = "Contacts";
		lvMenuItems1[3] = "Monitor";
		lvMenuItems1[4] = "Blocked Users";
		lvMenuItems1[5] = "Chats";
		lvMenuItems1[6] = "Settings (Delete Account)";
		lvMenuItems1[7] = "Terms";

		appContext = getContentResolver();
		// Inflate the mainLayout
		mainLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.home1, null);

		setContentView(mainLayout);
		
		

		// Init menu

		// lvMenuItems = getResources().getStringArray(R.array.menu_items);
		// lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
		listview = (ListView) findViewById(R.id.activity_main_menu_listview);

		mAdapter = new MyListAdapter(lvMenuItems1, images, Home1.this);

		listview.setAdapter(mAdapter);

		// lvMenu.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.simple_list_item_1,R.id.simple_list_item_1, lvMenuItems));
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);

				view.setSelected(true);
			}

		});

		red1 = (LinearLayout) findViewById(R.id.red1);
		red2 = (LinearLayout) findViewById(R.id.red2);
		red3 = (LinearLayout) findViewById(R.id.red3);
		red4 = (LinearLayout) findViewById(R.id.red4);

		red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
		red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
		red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
		red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

		contacts = (Button) findViewById(R.id.contacts);
		contacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				red1.setBackgroundColor(Color.parseColor("#c22026"));
				red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

				FragmentManager fm = Home1.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentContacts fragment = new FragmentContacts();
				// ft.add(R.id.activity_main_content_fragment, fragment);
				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
				} else {
					ft.add(R.id.activity_main_content_fragment, fragment);
				}
				ft.addToBackStack(null);
				// tvTitle.setText("Contacts");
				ft.commit();

			}
		});

		monitor = (Button) findViewById(R.id.monitor);
		monitor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
					  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					  imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
				


				red2.setBackgroundColor(Color.parseColor("#c22026"));
				red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

				FragmentManager fm = Home1.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentMonitor fragment = new FragmentMonitor();
				// ft.add(R.id.activity_main_content_fragment, fragment);
				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
				} else {
					ft.add(R.id.activity_main_content_fragment, fragment);
				}
				ft.addToBackStack(null);
				// tvTitle.setText("Contacts");
				ft.commit();

			}

			
		});
		settings = (Button) findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				  imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			


				red3.setBackgroundColor(Color.parseColor("#c22026"));
				red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

				FragmentManager fm = Home1.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentSettings fragment = new FragmentSettings();
				// ft.add(R.id.activity_main_content_fragment, fragment);
				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
				} else {
					ft.add(R.id.activity_main_content_fragment, fragment);
				}
				ft.addToBackStack(null);
				// tvTitle.setText("Settings");
				ft.commit();

			}
		});
		
		
		
		chat = (Button) findViewById(R.id.chat);
		Log.i("chat number==","=="+unread_msg);
		
		chat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				  imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			


				red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
				red4.setBackgroundColor(Color.parseColor("#c22026"));

				FragmentManager fm = Home1.this.getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentChatInbox fragment = new FragmentChatInbox();
				// ft.add(R.id.activity_main_content_fragment, fragment);
				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
				} else {
					ft.add(R.id.activity_main_content_fragment, fragment);
				}
				ft.addToBackStack(null);
				// tvTitle.setText("Settings");
				ft.commit();

			}
		});

		// Get menu button
		btMenu = (Button) findViewById(R.id.activity_main_content_button_menu);
		

		 

		
		  btMenu.setOnClickListener(new OnClickListener() {
		  
		  @Override public void onClick(View v) {
			  InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			  imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		// Show/hide the menu
		  toggleMenu(v); } });
		 

		// Get title textview
		// tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

		// Add FragmentMain as the initial fragment
		FragmentManager fm = Home1.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		String extras1 = getIntent().getStringExtra("notification");

		if (extras1 != null) {
			FragmentChatInbox fragment = new FragmentChatInbox();
			ft.add(R.id.activity_main_content_fragment, fragment);
			if (fragment != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragment);

				ft.commit();
			}
		} else {
			FragmentContacts fragment = new FragmentContacts();

			ft.add(R.id.activity_main_content_fragment, fragment);
			if (fragment != null) {
				// Replace current fragment by this new one
				ft.replace(R.id.activity_main_content_fragment, fragment);

				ft.commit();
			}

		}
		processExtraData();
		
		
		
	}
	
	

	private void getUnreadMessagefor() {
		getChatListObj = new getChatListTask();
		isConnected = NetConnection.checkInternetConnectionn(Home1.this);
		if (isConnected == true) {
			getChatListObj.execute();
		}

		else {
			new AlertsUtils(Home1.this, AlertsUtils.NO_INTERNET_CONNECTION);

		}

		
	}



	/**
	 * FOR NOTIFICATION
	 */
	private void processExtraData() {
		Intent intent = getIntent();
		
		
		
		unread_msg = intent.getStringExtra("unread_msg");
		
		Log.i("unread mesg in home screeen====","=="+unread_msg);
		
		//chat.setText(unread_msg);
		
		if(unread_msg!=null){
			
			chat.setText(unread_msg);
		
		FragmentManager fm = Home1.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FragmentChatInbox fragment = new FragmentChatInbox();

		if (fragment != null) {

			ft.replace(R.id.activity_main_content_fragment, fragment);
		} else {
			ft.add(R.id.activity_main_content_fragment, fragment);
		}
		ft.addToBackStack(null);

		ft.commit();
		
		}
		
		else {
			return;
		}
	}
	
	
	private void sendRegId() {
		sendRegIdObj = new sendRegIdTask();
		isConnected = NetConnection.checkInternetConnectionn(Home1.this);
		if (isConnected == true) {
			sendRegIdObj.execute();
		}

		else {
			new AlertsUtils(Home1.this, AlertsUtils.NO_INTERNET_CONNECTION);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void toggleMenu(View v) {
		mainLayout.toggleMenu();
	}

	// Perform action when a menu item is clicked
	private void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = lvMenuItems1[position];
		// String currentItem = tvTitle.getText().toString();

		// Do nothing if selectedItem is currentItem
		/*
		 * if(selectedItem.compareTo(currentItem) == 0) {
		 * mainLayout.toggleMenu(); return; }
		 * 
		 * 
		 */
		
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		  imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	

		FragmentManager fm = Home1.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (selectedItem.compareTo("Your Profile id:" + user_id) == 0) {
			red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

			fragment = new FragmentMain();
		} else if (selectedItem.compareTo("Notifications") == 0) {
			red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));
			

			 

			fragment = new FragmentNotification();
		} else if (selectedItem.compareTo("Contacts") == 0) {
			red1.setBackgroundColor(Color.parseColor("#c22026"));
			red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

			fragment = new FragmentContacts();
		} else if (selectedItem.compareTo("Monitor") == 0) {
			red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red2.setBackgroundColor(Color.parseColor("#c22026"));
			red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

			fragment = new FragmentMonitor();
		} else if (selectedItem.compareTo("Blocked Users") == 0) {
			red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

			fragment = new FragmentBlockList();
		} else if (selectedItem.compareTo("Chats") == 0) {
			red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red3.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

			fragment = new FragmentChatInbox();
		} else if (selectedItem.compareTo("Settings (Delete Account)") == 0) {
			red1.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red2.setBackgroundColor(Color.parseColor("#FFFFFF"));
			red3.setBackgroundColor(Color.parseColor("#c22026"));
			red4.setBackgroundColor(Color.parseColor("#FFFFFF"));

			fragment = new FragmentSettings();

		} else if (selectedItem.compareTo("Terms") == 0) {
			fragment = new FragmentTerms();
		}

		if (fragment != null) {
			// Replace current fragment by this new one
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();

			// Set title accordingly
			// tvTitle.setText(selectedItem);
		}

		// Hide menu anyway
		mainLayout.toggleMenu();
	}

	@Override
	public void onBackPressed() {

		if (mainLayout.isMenuShown()) {
			mainLayout.toggleMenu();
		} else {

			super.onBackPressed();
		}
	}

	public void setAppContext() {

	}

	public static void getAppContext() {

	}

	class MyListAdapter extends BaseAdapter {
		Resources res = getResources();
		LayoutInflater mInflater = null;

		public MyListAdapter(String[] lvMenuItems1, Integer[] images,
				Home1 context) {
			// TODO Auto-generated constructor stub
			lvMenuItems2 = lvMenuItems1;
			images1 = images;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return images1.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return images1[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.simple_list_item_1,
						null);
				holder.textview = (TextView) convertView
						.findViewById(R.id.simple_list_item_1);
				holder.image = (ImageView) convertView
						.findViewById(R.id.simple_image_item_1);

				// holder.textview.setText(Arrays.toString(lvMenuItems2));
				holder.textview.setText(lvMenuItems2[position]);
				holder.image.setImageResource(images1[position]);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				convertView = mInflater.inflate(R.layout.simple_list_item_1,
						null);
				holder.textview = (TextView) convertView
						.findViewById(R.id.simple_list_item_1);
				holder.image = (ImageView) convertView
						.findViewById(R.id.simple_image_item_1);

				// holder.textview.setText(Arrays.toString(lvMenuItems2));
				holder.textview.setText(lvMenuItems2[position]);
				holder.image.setImageResource(images1[position]);

				convertView.setTag(holder);
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView textview;
		ImageView image;

	}

	/******************************** get registration id ********************************/

	public void getRegId() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					msg = "Device registered, registration ID=" + regid;
					Log.i("GCM", msg);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

				Log.i("registration id ==", "==" + msg);

			}
		}.execute(null, null, null);
	}

	public class sendRegIdTask extends AsyncTask<Void, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... Params) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.URL);

			String result = null;

			try {

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				Log.i("user id==", "==" + WebServices.USER_ID);
				Log.i("reg id==", "==" + regid);

				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));
				nameValuePairs.add(new BasicNameValuePair("add_token", regid));

				nameValuePairs.add(new BasicNameValuePair("tt", device));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("sending reg id", "STATUS OK");

					result = out.toString();
					Log.i("sending reg id", "STATUS OK===" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in sending reg id", "......" + e);
			}
			Log.i("result of sending reg id", "=" + result);
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// new
			// AlertsUtils(Home1.this,"request has been processed successfully");

		}

	}

	
	private  BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("<>BROADCAST<>","<>RECEIVER<>");
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			
			 unread_msg = intent.getStringExtra("unread_msg");
			
			Log.i("BROADCAST MESSAGE","=="+unread_msg);
			Log.i("BROADCAST NEW MESSAGE","=="+newMessage);
			
			//chat.setText(unread_msg);
			
			if(unread_msg!=null){
				
				chat.setText(unread_msg);
			
			
			}
			
			else {
				return;
			}

			
			
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");
			/*Toast.makeText(getApplicationContext(),
					"New Message: " + newMessage, Toast.LENGTH_LONG).show();*/

			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	
	@Override
    protected void onResume() {
		 super.onResume();
		Log.i("onResume","onResume");
		IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.MAIN");
        registerReceiver(mHandleMessageReceiver, intentFilter);
        Log.i("===","register in on resume");
        Intent intent = getIntent();
        unread_msg = intent.getStringExtra("unread_msg");
		
		Log.i("BROADCAST MESSAGE","=="+unread_msg);
		
		
		
		
		if(unread_msg!=null){
			
			chat.setText(unread_msg);
		
		
		}
		
		else {
			return;
		}

       
    }

    @Override
    protected void onPause() {
    	super.onPause();
        unregisterReceiver(mHandleMessageReceiver);
        
        Log.i("====","unregister");
    }

	@Override
	protected void onDestroy() {

		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		
		System.out.println("************ Cancelling Thread *************");

		if (newMessageFetcher != null) {

			newMessageFetcher.cancel();

		}

		
		

		
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent); //must store the new intent unless getIntent() will return the old one
		
		Log.i("ENTER","222");
		unread_msg = intent.getStringExtra("unread_msg");
		
		Log.i("unread mesg in home screeen====","=="+unread_msg);
		
		chat.setText(unread_msg);
		
		FragmentManager fm = Home1.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FragmentChatInbox fragment = new FragmentChatInbox();

		if (fragment != null) {

			ft.replace(R.id.activity_main_content_fragment, fragment);
		} else {
			ft.add(R.id.activity_main_content_fragment, fragment);
		}
		ft.addToBackStack(null);

		ft.commit();

	}
	
	/******************** get chat list servie *********************************/

	public class getChatListTask extends AsyncTask<Void, Integer, String> {
		
		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			
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
			
			
			try {
				JSONObject jsonObj1 = new JSONObject(result);
				String unread_msg = jsonObj1.getString("unread");
				Log.i("WEB SERVICE===","UNREAD MSG=="+unread_msg);
				
				
				if(unread_msg!=null){
					
					chat.setText(unread_msg);
				
				}
				
				else {
					return;
				}
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
