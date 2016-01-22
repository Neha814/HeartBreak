package com.example.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.example.fragments.FragmentSettings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

public class ImageUtils {
	/**
	 * Get a thumbnail bitmap.
	 * @param uri
	 * @return a thumbnail bitmap
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static Bitmap getThumbnail(Context context, Uri uri, int thumbnailSize) throws FileNotFoundException, IOException {
		InputStream input = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;
		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
		double ratio = (originalSize > thumbnailSize) ? (originalSize / thumbnailSize) : 1.0;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = context.getContentResolver().openInputStream(uri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}
	
	public static Bitmap getThumbnailFromInputStream(Context context, InputStream is, int thumbnailSize) throws FileNotFoundException, IOException {
		InputStream input = is;
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;
		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
		double ratio = (originalSize > thumbnailSize) ? (originalSize / thumbnailSize) : 1.0;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = is;
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}
	/**
	 * Resolve the best value for inSampleSize attribute.
	 * @param ratio
	 * @return
	 */
	private static int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		if (k == 0)
			return 1;
		else
			return k;
	}
	public static Bitmap rotateBitmap(Bitmap image){
		int width=image.getHeight();
		int height=image.getWidth();
		Bitmap srcBitmap=Bitmap.createBitmap(width, height, image.getConfig());
		for (int y=width-1;y>=0;y--)
			for(int x=0;x<height;x++)
				srcBitmap.setPixel(width-y-1, x,image.getPixel(x, y));
		return srcBitmap;

	}

	
	
	public static Bitmap fixOrientation(Bitmap mBitmap) {
	    if (mBitmap.getWidth() > mBitmap.getHeight()) {
	        Matrix matrix = new Matrix();
	        matrix.postRotate(90);
	        mBitmap = Bitmap.createBitmap(mBitmap , 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
	    
	    }
	    
	    return mBitmap;
	        
	    }
	
	
	public static int exifToDegrees(int exifOrientation) {        
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; } 
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; } 
		else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }            
		return 0;    
	}

	
	
	public static Bitmap rotateImage(Bitmap bitmap, String filePath)
	{
	    Bitmap resultBitmap = bitmap;

	    try
	    {
	        ExifInterface exifInterface = new ExifInterface(filePath);
	        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

	        Matrix matrix = new Matrix();

	        if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
	        {
	            matrix.postRotate(ExifInterface.ORIENTATION_ROTATE_90);
	        }
	        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
	        {
	            matrix.postRotate(ExifInterface.ORIENTATION_ROTATE_180);
	        }
	        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
	        {
	            matrix.postRotate(ExifInterface.ORIENTATION_ROTATE_270);
	        }

	        // Rotate the bitmap
	        resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	    }
	    catch (Exception exception)
	    {
	       //Logger.d("Could not rotate the image");
	    }
	    return resultBitmap;
	}

	
	
	
	
	
	
	
	
	

}
