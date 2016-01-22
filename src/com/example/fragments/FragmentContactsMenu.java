package com.example.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
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
import org.json.simple.parser.JSONParser;
import android.widget.AdapterView.OnItemClickListener;

import com.example.brokenheart.Home1;
import com.example.brokenheart.MainActivity;
import com.example.brokenheart.R;
import com.example.brokenheart.Validation;
import com.example.brokenheart.Validation.MyTask;

import com.example.parser.Parser;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;
import com.example.utils.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.ContentResolver;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.text.InputFilter;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView.OnCloseListener;

@SuppressLint("NewApi")
public class FragmentContactsMenu extends ListFragment implements Parser

{
	public FragmentContactsMenu() {

	}

	// String result;
	String adding_status;

	String result = null;
	ListView listView;
	LazyAdapter mAdapter;
	MyTask objMyTask2;
	MyTask3 objMyTask3;
	Boolean isConnected;
	String List_item;
	int threadCounter = 0;
	int selectedPosition = -1;
	Button arg = null;

	SharedPreferences sharedpreferences;

	public ArrayList<HashMap<String, String>> contact_detail1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_contactmenu_listview,
				null);
		listView = (ListView) view.findViewById(android.R.id.list);

		if (contact_detail.equals(null) || contact_detail.size() <= 0) {

			objMyTask3 = new MyTask3();

			/**
			 * Checking if network connection is there or not
			 */
			isConnected = NetConnection.checkInternetConnectionn(getActivity());
			if (isConnected == true) {

				objMyTask3.execute();

			}

			else {

				new AlertsUtils(getActivity(),
						AlertsUtils.NO_INTERNET_CONNECTION);

			}
		}// end of if statement

		else {
			mAdapter = new LazyAdapter(contact_detail, getActivity());

			listView.setAdapter(mAdapter);
		}

		return view;

	}

	protected void fetchMonitoredContacts(Void... Params) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(WebServices.CONTACTS_TO_MONITOR_URL);

		try {

			/**
			 * to get contacts of a user
			 * 
			 */

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs.add(new BasicNameValuePair(
					WebServices.GET_CONTACTS_OF, WebServices.USER_ID));
			nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
					WebServices.USER_ID));
			nameValuePairs.add(new BasicNameValuePair(WebServices.REMARKS,
					"looser"));

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

			Log.i("error encountered", "......" + e);
		}
		Log.i("result", "=" + result);
		// return result;

		try {

			String response = result.toString();

			JSONObject jsonObj = new JSONObject(response);
			JSONArray AddedContacts = jsonObj.getJSONArray("contact_results");
			for (int i = 0; i < AddedContacts.length(); i++) {
				Map<String, String> map1 = new HashMap<String, String>();
				JSONObject c = AddedContacts.getJSONObject(i);
				// String added_phoneNo = c.getString("contact_number");

				String added_phoneNo = StringUtils.replaceWords(c
						.getString("contact_number"));
				map1.put(ADDED_PHONE_NO, added_phoneNo);

				String added_phoneId = c.getString("contact_id");

				map1.put(ADDED_PHONE_ID, added_phoneId);

				WebServices.contact_to_monitor_details = new ArrayList<HashMap<String, String>>();

				WebServices.contact_to_monitor_details.add((HashMap) map1);

				AddedContactList.add(added_phoneNo);

				AddedContactUserId.add(added_phoneId);

			}

			Log.i("contact_detail==", "==" + contact_detail);

		} catch (JSONException e) {
			e.printStackTrace();

		}

	}

	public void fetchContacts() {

		String phoneNumber = null;
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = Home1.appContext;

		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				null);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				Map<String, String> map = new HashMap<String, String>();

				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));
				
				//AlertsUtils.convertToupperCase(name);
				

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));
				
				

				if (hasPhoneNumber > 0 && name.length() > 0) {
					// Contact_names.add(name);

					map.put(NAME, name);

					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						// Contact_phone.add(phoneNumber);

						map.put(PHONE, phoneNumber);

						break;
					}
					contact_detail.add((HashMap) map);

					phoneCursor.close();

				}

			}

		}

	}

	

	/**
	 * 
	 * @author Macrew Technology LazyAdapter class for setting up listview with
	 *         contact list and add friend button.
	 */

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public LazyAdapter(ArrayList<HashMap<String, String>> contact_detail,
				FragmentActivity activity) {

			contact_detail1 = contact_detail;
			mInflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {
			return contact_detail1.size();
		}

		@Override
		public Object getItem(int position) {

			return contact_detail1.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;

			String added_phoneNo = StringUtils.replaceWords(contact_detail1
					.get(position).get(PHONE));
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.simple_list_item_3,
						null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.addFriend = (Button) convertView
						.findViewById(R.id.addFriend);

				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			holder.name.setText(contact_detail1.get(position).get(NAME));

			holder.phone.setText(contact_detail1.get(position).get(PHONE));
			if (AddedContactList.contains(added_phoneNo)) {

				holder.addFriend.setBackgroundResource(R.drawable.tick_icon);
				holder.addFriend.setEnabled(false);
			} else {
				holder.addFriend.setBackgroundResource(R.drawable.addfriend);
				holder.addFriend.setEnabled(true);
			}

			holder.addFriend.setTag(position);

			holder.addFriend.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					selectedPosition = (Integer) arg0.getTag();

					objMyTask2 = new MyTask();
					arg = (Button) arg0;
					/**
					 * Checking if network connection is there or not
					 */
					isConnected = NetConnection
							.checkInternetConnectionn(getActivity());
					if (isConnected == true) {

						objMyTask2.execute();

						/**
						 * Comparing phone contact list and added contact list
						 */

					}

					else {

						new AlertsUtils(getActivity(),
								AlertsUtils.NO_INTERNET_CONNECTION);

					}

				}
			});

			return convertView;
		}

	}

	class ViewHolder {
		TextView name, phone;
		Button addFriend;

	}

	/**
	 * On Click
	 * 
	 * To add a contact number for a user
	 * 
	 * @author Macrew Technology
	 * 
	 */
	public class MyTask extends AsyncTask<Void, Integer, String> {
		Dialog dialog;
		ProgressBar progressBar;
		TextView tvLoading;
		Button btnCancel;

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

				// getting values from user n then sending

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				String added_phoneNo = StringUtils.replaceWords(contact_detail1
						.get(selectedPosition).get(PHONE));
				nameValuePairs.add(new BasicNameValuePair(
						WebServices.ADD_CONTACT_NO, added_phoneNo));

				
				nameValuePairs.add(new BasicNameValuePair(
						WebServices.NAME,contact_detail1
						.get(selectedPosition).get(NAME)));
				nameValuePairs.add(new BasicNameValuePair("lname",""));
				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));

				nameValuePairs.add(new BasicNameValuePair(WebServices.REMARKS,
						"looser"));

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

				Log.i("error encountered", "......" + e);
			}
			Log.i("result of added contacts", "=" + result);
			return result;

		}

		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();

			/**
			 * Checking if network connection is there or not
			 */
			isConnected = NetConnection.checkInternetConnectionn(getActivity());
			if (isConnected == true) {

				try {

					JSONObject jsonObj = new JSONObject(result);

					adding_status = jsonObj
							.getString(WebServices.ADDING_RESULT);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			if (adding_status.equals("true")) {

				String added_phoneNo = StringUtils.replaceWords(contact_detail1
						.get(selectedPosition).get(PHONE));

				AddedContactList.add(added_phoneNo);

				arg.setBackgroundResource(R.drawable.tick_icon);
				arg.setEnabled(false);
				new AlertsUtils(getActivity(), AlertsUtils.CONTACT_ADDED);

			}

		}

	}

	/**
	 * 
	 * @author Macrew Technology
	 * 
	 *         for fetchContacts(); fetchMonitoredContacts();
	 * 
	 * 
	 */

	public class MyTask3 extends AsyncTask<Void, Void, String> {

		Dialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(getActivity(), "Loading...",
					"Please Wait", true, false);
		}// End of onPreExecute method

		@Override
		protected String doInBackground(Void... params) {
			fetchContacts();
			fetchMonitoredContacts();

			return result;

		}// End of doInBa

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dialog.dismiss();

			mAdapter = new LazyAdapter(contact_detail, getActivity());

			listView.setAdapter(mAdapter);

		}// End of onPostExecute method

	}

}