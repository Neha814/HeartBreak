package com.example.parser;

import java.util.ArrayList;
import java.util.HashMap;

public interface Parser {
	
	public final String NAME= "name";
	public final String PHONE = "phone";
	
	public final String USERNAME= "username";
	public final String PASSWORD= "password";
	
	
	public final String USER_ID= "user_id";
	
	public final String USER_NUMBER= "user_number";
	public final String USER_NAME= "username";
	
	public final String BLOCKED= "blocked";
	
	
	
	public final String ADDED_PHONE_NO = "added_phone_no";
	public final String ADDED_NAME = "added_name";
	
	public final String ADDED_PHONE_ID="added_phone_id";
	
	//public final ArrayList<String> Contact_phone = new ArrayList<String>();
	
	/**
	 * array list for contact section
	 */
	
	public final ArrayList<String> AddedContactList = new ArrayList<String>();
	public final ArrayList<String> AddedContactName = new ArrayList<String>();
	
	public final ArrayList<String> MonitorContactList = new ArrayList<String>();
	
	public final ArrayList<String> MonitorContactId = new ArrayList<String>();
	public final ArrayList<String> MonitorMatchTotal = new ArrayList<String>();
	
	public final ArrayList<String> AddedContactUserId = new ArrayList<String>();
	
	public final ArrayList<String> NotificationResultList = new ArrayList<String>();
	
//	public final ArrayList<HashMap<String, String>> chatListDetail = new ArrayList<HashMap<String, String>>();
	
	
	public final ArrayList<HashMap<String, String>> added_monitor_contacts = new ArrayList<HashMap<String, String>>();
	/**
	 * array list for monitor section
	 */
	
	public final ArrayList<String> AddedContactList1 = new ArrayList<String>();
	
	public final ArrayList<HashMap<String, String>> contact_detail = new ArrayList<HashMap<String, String>>();
	//public final ArrayList<HashMap<String, String>> contact_to_monitor_details = new ArrayList<HashMap<String, String>>();
	
	//public final ArrayList<HashMap<String, String>> contact_to_monitor_details ;
	//public final String USER_ID="";
	
	
}
