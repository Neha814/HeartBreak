package com.example.utils;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.animation.AlphaAnimation;



public class StaticUtils {

	private static SharedPreferences snapChatSharedPreferences;
	private static SharedPreferences.Editor snapChatSharedPreferencesEditor;

	
	
	public static Bitmap enlargeBitmap;
	
	
	public static String getStoredUserId(Context cntx) {

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(cntx);
		String userId = sharedPreferences.getString("uid", "");
		return userId;

	}
	


	public static AlphaAnimation onClickAnimation() {
		return new AlphaAnimation(1.0f, 0.6f);
	}


	public static boolean isFriendAlreadyAdded(String[] splittedFriendIdsArray, String friendId){
		
		int arrSize = splittedFriendIdsArray.length;
		boolean isExpired = false;
		
		for(int i=0; i<arrSize; i++)
		{
			
			if(friendId.equalsIgnoreCase(splittedFriendIdsArray[i])){
				
				isExpired = true;
				break;
			}
			
		}
		
		return isExpired;
		
	}
	
	
	/**
     * Function to show settings alert dialog
     * */
    public static void showSettingsAlert(final Context cntxt){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cntxt);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                cntxt.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }
	
	
	public static boolean isGpsOn(Context cntxt){
		
		LocationManager locationManager = (LocationManager) cntxt.getSystemService(Context.LOCATION_SERVICE);
		// getting GPS status
		boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if GPS enabled     
		
		if(isGPSEnabled){
			
			return true;
		}
		
		else{
			
			return false;
		}
		
	}
	
	
	public static Location getCurrentLocation(Context cntxt){
		
		 LocationManager locationManager = (LocationManager) cntxt.getSystemService(Context.LOCATION_SERVICE);
		 Criteria criteria = new Criteria();
	     String provider = locationManager.getBestProvider(criteria, false);
	     Location location = locationManager.getLastKnownLocation(provider);
	     return location;
	
	}

	
	public static int getPickedPictureRotation(Uri outputFileUri){
		
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(outputFileUri.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1); 
		int rotationInDegrees = exifToDegrees(rotation);
		
		return rotationInDegrees;
	}
	
	public static int exifToDegrees(int exifOrientation) {        
	    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; } 
	    else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }            
	    return 0;    
	 }


}
