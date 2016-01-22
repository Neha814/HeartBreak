package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

public class Utils {
	
	//public  static String Basic_url="http://nearyou.web1.anzleads.com/NearYouWebServiceCRUD.asmx";
	//public  static String Registration_url="http://nearyou.web1.anzleads.com/NearYouWebServiceCRUD.asmx/IRegistration";
	//public  static String Varify_url="http://nearyou.web1.anzleads.com/NearYouWebServiceCRUD.asmx/";
	
	public static Bitmap creatFromEncodedData(String rawImageData) {
		try {
			byte bytes[] = Base64.decode(rawImageData, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * @param context @see Context type
	 * 
	 * @param selectedImageUri @see Uri convert the passed Uri Image into bitmap
	 * also reduce the height and width of image maximum 150
	 * 
	 * @return String data as @see Base64 format
	 */
	public static String convertImageToBase64String(Context context,
			Uri selectedImageUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImageUri,
				proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();

		Bitmap bitmap = BitmapFactory.decodeFile(path);
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		if ((height > 150) || (width > 150)) {
			Log.i("Utils", "image uri bitmap size is large");
			bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte data[] = baos.toByteArray();
		return Base64.encodeToString(data, Base64.DEFAULT);

	}

	/*
	 * @return String convert Base64 format string from @param defaultLogoBitmap
	 * 
	 * @see Bitmap passed
	 */
	public static String convertImageToBase64String(Bitmap defaultLogoBitmap) {
		int height = defaultLogoBitmap.getHeight();
		int width = defaultLogoBitmap.getWidth();
		Bitmap bitmap = null;
		if ((height > 150) || (width > 150)) {

			bitmap = Bitmap.createScaledBitmap(defaultLogoBitmap, 150, 150,
					true);
		} else {
			bitmap = defaultLogoBitmap;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte data[] = baos.toByteArray();
		return Base64.encodeToString(data, Base64.DEFAULT);

	}

	/*
	 * @return Uri of the picked Image by the user imagePicker start the
	 * activity for results to pick image from available intents by andorid os.
	 * 
	 * @param context @see Context
	 * 
	 * @param activity @see Activity from which Action_Get_Content @see Intent
	 * is started
	 * 
	 * @param requestPickCode @see int code for request results
	 */
	public static Uri imagePicker(Context context, Activity activity,
			int requestPickCode) {
		Uri outputFileUri;
		final File root = new File(Environment.getExternalStorageDirectory()
				+ "/Picked Images");
		if (!root.exists()) {
			root.mkdir();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mmm_dd_hh_MM_ss",
				Locale.getDefault());
		String name = sdf
				.format(new Date(SystemClock.currentThreadTimeMillis()));
		final String fname = "profile_pic_" + name + ".jpg";
		final File sdImageMainDirectory = new File(root, fname);
		outputFileUri = Uri.fromFile(sdImageMainDirectory);

		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = context.getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			cameraIntents.add(intent);
		}

		// File system.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
		// Chooser of file system options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Select Source");
		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));
		activity.startActivityForResult(chooserIntent, requestPickCode);
		return outputFileUri;
	}




	public static String encodeTobase64(Bitmap image)
	{
		Bitmap immagex=image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

		Log.e("LOOK", imageEncoded);
		return imageEncoded;
	}
	
	public static Bitmap decodeBase64(String input) 
	{
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}



}
