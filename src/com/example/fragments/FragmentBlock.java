package com.example.fragments;

import com.example.brokenheart.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentBlock extends Fragment {
	
	 public FragmentBlock() {
	    }
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.fragment_block, null);
	    
	        return view;
	 }
	        
}
