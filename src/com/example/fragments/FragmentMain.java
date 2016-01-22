package com.example.fragments;


import com.example.brokenheart.Home1;
import com.example.brokenheart.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.brokenheart.*;


public class FragmentMain extends Fragment {
  
	ImageView red_heart,broken_heart;
    
    public FragmentMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        
     

      	red_heart = (ImageView) view.findViewById(R.id.red_heart);
      	broken_heart= (ImageView) view.findViewById(R.id.broken_heart);
       
        
      	red_heart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FragmentManager fm = FragmentMain.this.getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentContacts fragment = new FragmentContacts();
			
		       if(fragment != null) {
		            // Replace current fragment by this new one
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	// add new fragment
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit();   
			    	}
		});  
      	
      	broken_heart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FragmentManager fm = FragmentMain.this.getFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        FragmentMonitor fragment = new FragmentMonitor();
			
		       if(fragment != null) {
		            // Replace current fragment by this new one
		            ft.replace(R.id.activity_main_content_fragment, fragment);
		       }
		            else{
		            	// add new fragment
		            	 ft.add(R.id.activity_main_content_fragment, fragment);
		            }
		        ft.addToBackStack(null);
		    
		        ft.commit();   
			    	}
		});  
        return view;
    }
}
