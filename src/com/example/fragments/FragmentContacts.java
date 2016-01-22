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

import com.example.brokenheart.MainActivity;
import com.example.brokenheart.R;
import com.example.brokenheart.Validation;

import com.example.parser.Parser;
import com.example.services.WebServices;
import com.example.utils.AlertsUtils;
import com.example.utils.NetConnection;
import com.example.utils.StringUtils;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentContacts extends Fragment implements Parser {
	
	Button phone_no , match_id, cupid_id, fb_friend;
	
	public FragmentContacts() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_contact_screen, null);
		
		phone_no = (Button) view.findViewById(R.id.phone_no);
		match_id = (Button) view.findViewById(R.id.match_id);
		cupid_id = (Button) view.findViewById(R.id.cupid_id);
		fb_friend = (Button) view.findViewById(R.id.fb_friend);
		
		phone_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FragmentManager fm = FragmentContacts.this.getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentPhoneNumber fragment = new FragmentPhoneNumber();
			
		       if(fragment != null) {
		            
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit(); 
				
			}
		});
		
		match_id.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FragmentManager fm = FragmentContacts.this.getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentMatchId fragment = new FragmentMatchId();
			
		       if(fragment != null) {
		            
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit(); 
				
			}
		});
		
		cupid_id.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				FragmentManager fm = FragmentContacts.this.getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentCupidId fragment = new FragmentCupidId();
			
		       if(fragment != null) {
		            
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit(); 
				
				
				
			}
		});
		
		fb_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = FragmentContacts.this.getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentFacebookId fragment = new FragmentFacebookId();
			
		       if(fragment != null) {
		            
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit(); 
				
				
				
			}
		});
		
		return view;
	}

	
}
