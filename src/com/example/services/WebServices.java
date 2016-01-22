package com.example.services;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;

public class WebServices  {
	
	
	
	public static ArrayList<HashMap<String, String>> contact_to_monitor_details ;
	public static ArrayList<String> BlockedList;
	public static ArrayList<HashMap<String, String>> chatListDetail ;
	public static ArrayList<HashMap<String, String>> ChatInboxDetail;
	


	public static String VALIDATION_URL = "http://touchpixs.com/mobile/webserver.php";
	public static String SIGNUP_URL = "http://touchpixs.com/mobile/webserver.php";
	public static String CONTACTS_TO_MONITOR_URL = "http://touchpixs.com/mobile/webserver.php";
	public static String URL = "http://touchpixs.com/mobile/webserver.php";
	public static String STATUS = "True";

	public static String JSON_signup_result = "sc";
	public static String signup_result = "status";
	public static String REMARKS = "remarks";
	
	public static String phoneNo = new String() ;

	public static String USER_ID = new String();
	
	public static String USER_NAME = new String();
	
	public static String PASS_WORD = new String();
	

	public static String JSON_verify_result = "status";
	public static String JSON_user_id = "user_id";
	public static String Phone = new String();
	public static String Code = new String();
	public static final String UID = "uid";
	public static final String UNBLOCK_USER = "unblock_user";
	public static final String BLOCK_USER = "block_user";
	public static final String REPORT_USER = "report_user";
	public static final String ADDING_RESULT = "status";
	public static final String MONITOR_SECTION_RESULT = "status";
	public static final String SIGNED_IN = "user_already_signed_in";
	public static final String VERIFIED = "verification already done";
	public static final String VERIFY_CODE = "verify_code";
	public static final String SIGNUP_NO = "signup_number";
	public static final String PHONE_NO = "phone_no";
	public static final String CONTACT_NUMBER = "contact_number";
	public static final String GET_MONITORS_OF1 = "get_monitors_of1";
	public static final String GET_CONTACTS_OF = "get_contacts_of";
	public static final String ADD_CONTACT_NO = "add_contact_no";
	public static final String NAME = "fname";
	public static final String ADD_TO_MONITOR = "add_to_monitor";
	public static final String SUB_TO_MONITOR1 = "sub_to_monitor1";
	public static final String GET_USER_FOR_NUM1 = "get_user_for_num1";
	public static final String UPDATE_SETTINGS = "update_settings_for_user";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String ADD_CONTACT = "add_contact";
	public static final String CONTACT_TYPE = "contact_type";
	public static final String NICKNAME = "nickname";
	
	
	
	public static final String CC = "cc";

	public String USER_LOGIN_PROCESS = "STEP";

}
