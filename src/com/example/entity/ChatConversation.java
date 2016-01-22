package com.example.entity;

import java.util.ArrayList;

import android.graphics.Bitmap;


public class ChatConversation {


	int count;
	String status;
	String message;
	

	ArrayList<ChatConversationDetail> chatConversationDetailList = new ArrayList<ChatConversation.ChatConversationDetail>();


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public ArrayList<ChatConversationDetail> getChatConversationDetailList() {
		return chatConversationDetailList;
	}
	public void setChatConversationDetailList(
			ArrayList<ChatConversationDetail> chatConversationDetailList) {
		this.chatConversationDetailList = chatConversationDetailList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}



	public class ChatConversationDetail {

		public boolean left;
		public String chatId;
		public String senderId;
		public String receiverId;
		public String message;
		public String status;
		public String imageName;
		public String dateCreated;
		
		
		public boolean isPhotoUploaded;
		public boolean isPhotoPickedFromLocalStorage;
		
		
		Bitmap downloadedBitmap;
		
		public Bitmap getDownloadedBitmap() {
			return downloadedBitmap;
		}

		public void setDownloadedBitmap(Bitmap downloadedBitmap) {
			this.downloadedBitmap = downloadedBitmap;
		}

		

		public boolean isPhotoPickedFromLocalStorage() {
			return isPhotoPickedFromLocalStorage;
		}

		public void setPhotoPickedFromLocalStorage(boolean isPhotoPickedFromLocalStorage) {
			this.isPhotoPickedFromLocalStorage = isPhotoPickedFromLocalStorage;
		}

		public boolean isPhotoUploaded() {
			return isPhotoUploaded;
		}

		public void setPhotoUploaded(boolean isPhotoUploaded) {
			this.isPhotoUploaded = isPhotoUploaded;
		}


		public boolean isLeft() {
			return left;
		}

		public void setLeft(boolean left) {
			this.left = left;
		}

		public String getChatId() {
			return chatId;
		}

		public void setChatId(String chatId) {
			this.chatId = chatId;
		}


		public String getSenderId() {
			return senderId;
		}

		public void setSenderId(String senderId) {
			this.senderId = senderId;
		}

		public String getReceiverId() {
			return receiverId;
		}

		public void setReceiverId(String receiverId) {
			this.receiverId = receiverId;
		}

		public String getMessage() {
			
			
			return message;
		}

		public void setMessage(String message) {
			
			
			this.message = message;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getImageName() {
			return imageName;
		}

		public void setImageName(String imageName) {
			this.imageName = imageName;
		}

		public String getDateCreated() {
			return dateCreated;
		}

		public void setDateCreated(String dateCreated) {
			this.dateCreated = dateCreated;
		}

	
	}

}