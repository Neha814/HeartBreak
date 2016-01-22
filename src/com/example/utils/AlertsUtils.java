package com.example.utils;

import com.example.fragments.FragmentContactsMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertsUtils {
	Context context;
	AlertDialog.Builder alert;
	
	public static final String INVALID_PHONE_NUMBER = "Enter 10 digits phone number.";
	public static final String INVALID_PHONE_NUMBER_WARNING = "Please enter valid Phone number.";
	public static final String ENTER_PHONE_NUMBER_WARNING = "Please enter contact number.";
	public static final String NO_INTERNET_CONNECTION="No Internet Connection.";
	public static final String ENTER_COUNTRY_CODE_WARNING="Please enter country code.";
	public static final String ENTER_PHONE_AND_CODE_NUMBER="Please enter country code and phone number.";
	public static final String INVALID_COUNTRY_CODE = "Please Enter valid country code.";
	public static final String ENTER_NUMBER_WARNING = "Either coutry code field or phone number field is empty.";
	public static final String INVALID_NUMBER = "either country code or phone number is not correct.";
	public static final String ENTER_VERIFY_CODE_WARNING = "Please enter verification code.";
	public static final String INVALID_VERIFY_CODE = "Please enter valid verification code.";
	public static final String WRONG_VERIFICATION_CODE = "Wrong verification code..please check the verification code again.";
	public static final String CONTACT_ADDED = "Your request has been processed succesfully .";
	public static final String ENTER_USERNAME_PASSWORD_WARNING = "Please enter username and password.";
	public static final String USERNAME_WARNING = "Please enter username.";
	public static final String PASSWORD_WARNING = "Please enter password.";
	public static final String NO_SPACE_ALLOWED = "No space allowed in username.";
	public static final String ENTER_LOGIN_WARNING = "Either username or password is missing.";
	public static final String USERNAME_STARTS_WITH_SPACE_WARNING = "Username cannot starts with space.";
	public static final String USER_IS_BLOCKED_NOW ="User is blocked now.";
	public static final String USER_REPORTING_IS_DONE ="Your request has been submitted.";
	public static final String CONTACT_DELETED ="User contact is deleted.";
	public static final String ERROR_IN_DELETING_USERS_CONTACT="Error in deleting user contact.";
	public static final String CHAT_LIST_RESULT_IS_EMPTY="chat list is empty.";
	public static final String ERROR_OCCUR_IN_BLOCKING_THE_USER="Error occured while processing this request.";
	public static final String NO_BLOCKED_USER="No blocked user exists.";
	public static final String ERROR_BLOCKED_USER="Error occured.";
	public static final String ERROR_OCCURED_WHILE_PROCESSING_REQUEST="Error occured while processing the request.";
	public static final String USERNAME_UPDATED="Username updated.";
	public static final String ERROR_ENCOUNTERED_IN_UPDATING_USERNAME="Error occured while processing the request.";
	public static final String ERROR_ENCOUNTERED_IN_GETTING_USER_SETTIGNS="Error occured while getting user settings.";
	public static final String INVALID_USERNAME_WARNING="Invalid username.";
	public static final String INVALID_PASSWORD_WARNING="Please enter password.";
	public static final String PASSWORD_DID_NOT_MATCH_WARNING="New password and Confirm password did not match.";
	public static final String TRY_AGAIN="Some error occured.Please try again.";
	public static final String PASSWORD_UPDATED="Password updated.";
	public static final String ERROR_ENCOUNTERED_IN_UPDATING_PASSWORD="Error occured while processing the request.";
	public static final String ERROR_IN_LOGIN="Error occured while login.";
	public static final String PLEASE_SET_USERNAME_AND_PASSWORD="Please set username and password.";
	public static final String ERROR_ENCOUNTERED_IN_UPDATING_PROFILE="Error encountered in updating profile.";
	public static final String PLEASE_ENTER_THE_CORRECT_DETAILS="Please enter the correct details.";
	public static final String PLEASE_ENTER_THE_DETAILS="Please enter the details.";
	public static final String EITHER_PASSWORD_OR_USERNAME_IS_INCORRECT= "Either password or username is incorrect.";
	public static final String INBOX_IS_EMPTY= "Inbox is Empty.";
	public static final String UNBLOCK_WARNING= "You have blocked this user. Please unblock to chat.";
	public static final String ERROR_OCCURED= "Error occured while processing this request. Please try again.";
	public static final String ACCOUNT_DELETED= "Account deleted successfully.";
	public static final String ERROR_OCCUR_IN_REPORTING_THE_USER= "Error occured in reporting the user.";
	public static final String CONTACT_EXISTS_FOR_USER= "Contact already exists for user.";
	public static final String CONTACT_ADDED_SUCCESSFULLY= "Contact added successfully.";
	public static final String ERROR_ENCOUNTERED= "Error encountered while processing the request.Please try again.";
	
	
	public AlertsUtils(Context cxt, String message)
	{
		this.context = cxt;
		
		alert=new AlertDialog.Builder(context);
		alert.setMessage(message)
			 .setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
									     
					}
		}).show();
		
	}
	
	
	/****************
	 * 
	 * public AlertsUtils(Context cxt, String message)
{
	this.context = cxt;
	
	alert=new AlertDialog.Builder(context);
	alert.setMessage(message)
		 .setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
								     
				}
	}).show();
	
}
	 */
	 /*******************/
	
	
	
	public AlertsUtils(FragmentContactsMenu fragmentContacts1,
			String noInternetConnection) {
		// TODO Auto-generated constructor stub
		alert=new AlertDialog.Builder(context);
		alert.setMessage("NO INTERNET CONNECTION")
			 .setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
									     
					}
		}).show();
	}
	
		public static  String  convertToupperCase(String NAME) {
			
		
		    String[] exampleSplit = NAME.split(" ");
		    
		    StringBuffer sb = new StringBuffer();
		    for (String word : exampleSplit) {
		        sb.append(word.substring(0, 1).toUpperCase() + word.substring(1));
		        sb.append(" ");
		    }
		   
			return sb.toString();
		
	}
}
