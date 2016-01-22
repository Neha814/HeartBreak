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

import com.example.parser.Parser;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class FragmentMonitor extends Fragment  implements Parser{
	
	ListView listview;
	LazyAdapter mAdapter;
	MyTask objMyTask;
	Boolean isConnected;
	String monitor_section_result;
	String chatListResult;
	deleteTask deleteObj;
	String delete_result;
	
	
	ChatListTask objChatList;
	
	String id;
	String noId;
	
	Integer arg = null;
	
	public FragmentMonitor() {
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		Log.i("monitor contact list ",""+MonitorContactList);
		
        View view = inflater.inflate(R.layout.fragment_monitor_listview, null);
        
        listview = (ListView) view.findViewById(android.R.id.list);
        
        
        
        
      //  if (MonitorContactList.equals(null) || MonitorContactList.size() <= 0) {

        	objMyTask = new MyTask();

			isConnected = NetConnection.checkInternetConnectionn(getActivity());
			if (isConnected == true) {

				objMyTask.execute();

			}

			else {

				new AlertsUtils(getActivity(),
						AlertsUtils.NO_INTERNET_CONNECTION);

			}
	//}
        
    /*   else{
        	mAdapter = new LazyAdapter(MonitorContactList,MonitorMatchTotal, getActivity());

			listview.setAdapter(mAdapter);
        }*/
        
     
          return view;
    }
	
	
	/**
	 * 
	 * @author macrew technologies
	 * async class for getting monitored contacts
	 *
	 */
	
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

				nameValuePairs.add(new BasicNameValuePair(WebServices.GET_MONITORS_OF1,
						WebServices.USER_ID));
				
				
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("Monitor.POST", "STATUS OK");

					result = out.toString();
					Log.i("Monitor.POST", "STATUS OK===" + result);
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
			
		/*	mAdapter = new LazyAdapter(MonitorContactList, getActivity());

			listview.setAdapter(mAdapter);*/
			
			
			if (MonitorContactList!=null || MonitorContactList.size() >= 0)
				MonitorContactList.clear();
			if (MonitorContactId!=null || MonitorContactId.size() >= 0)
				MonitorContactId.clear();
			if (MonitorMatchTotal!=null || MonitorMatchTotal.size() >= 0)
				MonitorMatchTotal.clear();
				
			try {

				/**
				 * WebServices.MONITOR_SECTION_RESULT = status
				 */
				JSONObject jsonObj = new JSONObject(result);

				monitor_section_result = jsonObj
						.getString(WebServices.MONITOR_SECTION_RESULT);

				Log.i("monitor section result", "=" + monitor_section_result);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
				
				try {
					if(monitor_section_result.equals("true")){
					JSONObject jsonObj = new JSONObject(result);
					
					JSONArray MonitoredContacts = jsonObj
							.getJSONArray("contact_results");
					for (int i = 0; i < MonitoredContacts.length(); i++){
						
						JSONObject c = MonitoredContacts.getJSONObject(i);
						String monitored_phoneNo = c.getString("contact_text");
						String monitored_id = c.getString("contact_id");
						String match_total = c.getString("contact_matches");
						MonitorContactList.add(monitored_phoneNo);
						
						MonitorContactId.add(monitored_id);
						
						MonitorMatchTotal.add(match_total);
						
						
						
						
					}
					
					Log.i("MonitorContactList==",""+MonitorContactList);
					Log.i("MonitorMatchTotal==",""+MonitorMatchTotal);
					
					
					
					mAdapter = new LazyAdapter(MonitorContactList,MonitorMatchTotal, getActivity());

					listview.setAdapter(mAdapter);
				}
					else{
						
						/**********************************/
						mAdapter = new LazyAdapter(MonitorContactList,MonitorMatchTotal, getActivity());

						listview.setAdapter(mAdapter);
						/**********************************/
						
						
					}
				
					
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				
				catch (Exception e) {
					new AlertsUtils(getActivity(),"Some error occured.Please try again");
					
					e.printStackTrace();
				}
	
		}

	}
	
	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public ArrayList<String> MonitorContactList1 = new ArrayList<String>();

		public LazyAdapter(ArrayList<String> MonitorContactList,ArrayList<String> MonitorMatchTotal,
				FragmentActivity activity) {
			// TODO Auto-generated constructor stub

			MonitorContactList1 = MonitorContactList;
			mInflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {
			
			
			return MonitorContactList1.size();
		}

		@Override
		public Object getItem(int position) {
			
			return MonitorContactList1.get(position);
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
						.inflate(R.layout.monitor_listitem, null);
				holder.Phone = (TextView) convertView.findViewById(R.id.phone);
				holder.monitoring = (TextView) convertView.findViewById(R.id.monitoring);
				holder.chat =(Button) convertView.findViewById(R.id.chat);
				holder.delete =(Button) convertView.findViewById(R.id.delete);
				holder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
				
				
				holder.Phone.setText(MonitorContactList1.get(position));

				holder.monitoring.setText("");

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView1.setTag(position);
			holder.chat.setTag(position);
			Log.i("People Moniot","==="+MonitorMatchTotal.get(position));
			
			holder.delete.setTag(position);

			holder.Phone.setText(MonitorContactList1.get(position));
			
			int monitor  = Integer.parseInt(MonitorMatchTotal.get(position));
			
			if(monitor > 0){
			 monitor = monitor ;
			 holder.monitoring.setText(((String.valueOf(monitor)))+" other people monitoring");
			 holder.imageView1.setImageResource(R.drawable.hrt6);
			 holder.chat.setVisibility(View.VISIBLE);
			}
			
			
			else{
			
			holder.monitoring.setText(((String.valueOf(monitor)))+" people monitoring");
			holder.chat.setVisibility(View.INVISIBLE);
			holder.imageView1.setImageResource(R.drawable.hrt7);
			}
			
			
			/*************DELETE BUTTON **********************************/
			
			holder.delete.setOnClickListener(new OnClickListener() {
				
				Dialog dialog;
				Button delete_yes, delete_no;
				
				
				@Override
				public void onClick(View arg0) {
					
					arg = (Integer) arg0.getTag();
					/***********************************/
					
					dialog = new Dialog(getActivity());
					dialog.setCancelable(false);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.delete_monitoring_user);

					delete_yes = (Button) dialog.findViewById(R.id.delete_yes);
					delete_no = (Button) dialog.findViewById(R.id.delete_no);
					
					
					
					delete_no.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					
					delete_yes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							
						//	arg = (Integer) arg0.getTag();
							
							
							
							 id=	MonitorContactId.get(arg);
								deleteObj = new deleteTask();
								
								/**
								 * Checking if network connection is there or not
								 */
								isConnected = NetConnection
										.checkInternetConnectionn(getActivity());
								if (isConnected == true) {

									deleteObj.execute();

								}

								else {

									// Toast.makeText(getApplicationContext(),"Check your internet connection.",400).show();
									new AlertsUtils(getActivity(),
											AlertsUtils.NO_INTERNET_CONNECTION);

								}
								
						}
					});dialog.show();


					
					 /*************************************/
					
					
										
				}
			});
			
			
			/***************** CHAT BUTTON *****************************************/
			
			holder.chat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Log.i("chat button","clicked");
					
					arg = (Integer) arg0.getTag();
					
					
					noId=	MonitorContactId.get(arg);
					
					Log.i("noId=","=="+noId);
					if( MonitorMatchTotal.get(arg).equals("0")) {	
						return;
					}
								Bundle bundle = new Bundle();
					bundle.putString("monitor_ID",noId );
					
				
				  FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        FragmentChatList fragment = new FragmentChatList();
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
			});
			

						return convertView;
		}

	}

	class ViewHolder {
		TextView Phone,monitoring;
		Button delete , chat;
		ImageView imageView1;

	}
	
	/*****************TO DELETE A CONTACT TO MONITOR*************************************/
	
	public class deleteTask extends AsyncTask<Void, Integer, String> {
		
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
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(WebServices.VALIDATION_URL);
			
			String result = null;

			try {
				
				
				
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				
				Log.i("id to delete","=="+id);
				nameValuePairs.add(new BasicNameValuePair(WebServices.SUB_TO_MONITOR1,id));
				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,WebServices.USER_ID));
				nameValuePairs.add(new BasicNameValuePair("contact_no",""));
			
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			
				
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				StatusLine statusLine = response.getStatusLine();

				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					Log.i("DELETE monitor contact", "STATUS OK");

					result = out.toString();
					Log.i("DELETE monitor contact", "==" + result);
				} else {
					// close connection
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {

				Log.i("error encountered in DELETE monitor contact", "......");
			}
			Log.i("result", "=" + result);
			return result;

		}
		
		protected void onProgressUpdate(Integer... values) {

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();
			try {

				JSONObject jsonObj = new JSONObject(result);

				delete_result = jsonObj
						.getString(WebServices.JSON_verify_result);

							if (delete_result.equals("true")) {
								
								new AlertsUtils(getActivity(),
										AlertsUtils.CONTACT_DELETED);
						/*****************************************************/		
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
								/*****************************************************/		
					
				} else {
					new AlertsUtils(getActivity(),
							AlertsUtils.ERROR_IN_DELETING_USERS_CONTACT);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				dialog.dismiss();
				
			}


		}
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

				Log.i("get user from num=","=="+noId);
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.UID,
						WebServices.USER_ID));
				
				nameValuePairs.add(new BasicNameValuePair(WebServices.GET_USER_FOR_NUM1,noId));
				
				
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
		
		}
	}
/*******************ENDING OF GETTING CHAT LIST********************************/
	
	}
