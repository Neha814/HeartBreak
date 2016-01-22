package com.example.fragments;

import com.example.brokenheart.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class FragmentTerms extends Fragment {
	
public FragmentTerms() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.terms, null);
        
        getActivity().getWindow().setSoftInputMode(
		WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
		);

        
		return view;
        
	}

}
